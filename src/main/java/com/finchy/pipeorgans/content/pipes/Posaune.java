package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SingleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SinglePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllShapes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Posaune {

    public static class PosauneBlock extends SinglePipeBlock {
        public PosauneBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.WOOD,
                    AllBlocks.POSAUNE_EXTENSION,
                    AllBlockEntities.POSAUNE_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class PosauneExtensionBlock extends SingleExtensionBlock {
        public PosauneExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.POSAUNE,
                    AllShapes::slimExtensionShape);
        }

        @Override
        public boolean isDirectional() {
            return true;
        }
    }

    public static class PosauneBlockEntity extends GenericPipeBlockEntity {
        public PosauneBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.POSAUNE, AllBlocks.POSAUNE_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected PosauneSoundInstance soundInstance;

        @Override
        @OnlyIn(Dist.CLIENT)
        protected void tickAudio(PipeSize size, boolean powered) {
            if (!powered) {
                if (soundInstance != null) {
                    soundInstance.fadeOut();
                    soundInstance = null;
                }
                return;
            }

            float f = (float) Math.pow(2, -pitch / 12.0);
            boolean particle = level.getGameTime() % 8 == 0;
            Vec3 eyePosition = Minecraft.getInstance().cameraEntity.getEyePosition();
            float maxVolume = (float) Mth.clamp((64 - eyePosition.distanceTo(Vec3.atCenterOf(worldPosition))) / 64, 0, 1);

            if (soundInstance == null || soundInstance.isStopped() || soundInstance.getOctave() != size) {
                Minecraft.getInstance()
                        .getSoundManager()
                        .play(soundInstance = new PosauneSoundInstance(size, worldPosition));

                AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f, f, false);

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createReedSteamJet();
        }
    }

    public static class PosauneRenderer extends SafeBlockEntityRenderer<PosauneBlockEntity> {

        public PosauneRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(PosauneBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof PosauneBlock))
                return;

            Direction direction = blockState.getValue(PosauneBlock.FACING);
            PipeSize size = blockState.getValue(PosauneBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.POSAUNE_MOUTH_TINY;
                case SMALL -> AllPartialModels.POSAUNE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.POSAUNE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.POSAUNE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.POSAUNE_MOUTH_HUGE;
            };
            PartialModel goggles = switch (size) {
                case TINY -> AllPartialModels.GOGGLES_TINY;
                case SMALL -> AllPartialModels.GOGGLES_SMALL;
                case MEDIUM -> AllPartialModels.GOGGLES_MEDIUM;
                case LARGE -> AllPartialModels.GOGGLES_LARGE;
                case HUGE -> AllPartialModels.GOGGLES_HUGE;
            };

            float chaseTarget = be.animation.getChaseTarget();

            CachedBuffers.partial(mouth, blockState)
                    .center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                    .uncenter()
                    .scale(chaseTarget)
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
            if (be.hasGoggles()) {
                CachedBuffers.partial(goggles, blockState)
                        .center()
                        .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                        .uncenter()
                        .translate(0, -1f / 16f, 0)
                        .light(light)
                        .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
            }

        }
    }

    public static class PosauneSoundInstance extends GenericSoundInstance {

        public PosauneSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> POSAUNE_SUPERHIGH;
                        case SMALL -> POSAUNE_HIGH;
                        case MEDIUM -> POSAUNE_MEDIUM;
                        case LARGE -> POSAUNE_LOW;
                        case HUGE -> POSAUNE_DEEP;
                    }).get()
            );
        }
    }
}
