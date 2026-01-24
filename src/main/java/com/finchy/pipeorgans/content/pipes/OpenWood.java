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
import net.createmod.catnip.animation.AnimationTickHolder;
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

public class OpenWood {

    public static class OpenWoodBlock extends SinglePipeBlock {
        public OpenWoodBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.WOOD,
                    AllBlocks.OPEN_WOOD_EXTENSION,
                    AllBlockEntities.OPEN_WOOD_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class OpenWoodExtensionBlock extends SingleExtensionBlock {
        public OpenWoodExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.OPEN_WOOD,
                    AllShapes::slimExtensionShape);
        }

        @Override
        public boolean isDirectional() {
            return true;
        }
    }

    public static class OpenWoodBlockEntity extends GenericPipeBlockEntity {
        public OpenWoodBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.OPEN_WOOD, AllBlocks.OPEN_WOOD_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected OpenWoodSoundInstance soundInstance;

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
                        .play(soundInstance = new OpenWoodSoundInstance(size, worldPosition));

                playChiffSound(0.1f);

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createSteamJet(size);
        }
    }

    public static class OpenWoodRenderer extends SafeBlockEntityRenderer<OpenWoodBlockEntity> {

        public OpenWoodRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(OpenWoodBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof OpenWoodBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.OPEN_WOOD_MOUTH_TINY;
                case SMALL -> AllPartialModels.OPEN_WOOD_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.OPEN_WOOD_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.OPEN_WOOD_MOUTH_LARGE;
                case HUGE -> AllPartialModels.OPEN_WOOD_MOUTH_HUGE;
            };
            PartialModel goggles = switch (size) {
                case TINY -> AllPartialModels.GOGGLES_TINY;
                case SMALL -> AllPartialModels.GOGGLES_SMALL;
                case MEDIUM -> AllPartialModels.GOGGLES_MEDIUM;
                case LARGE -> AllPartialModels.GOGGLES_LARGE;
                case HUGE -> AllPartialModels.GOGGLES_HUGE;
            };

            float offset = be.animation.getValue(partialTicks);
            if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
                float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
                offset -= (float) (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 8f);
            }

            CachedBuffers.partial(mouth, blockState)
                    .center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                    .uncenter()
                    .translate(0, -offset*2 / 16f, 0)
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
            if (be.hasGoggles()) {
                CachedBuffers.partial(goggles, blockState)
                        .center()
                        .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                        .uncenter()
                        .light(light)
                        .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
            }

        }
    }

    public static class OpenWoodSoundInstance extends GenericSoundInstance {

        public OpenWoodSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> OPEN_WOOD_SUPERHIGH;
                        case SMALL -> OPEN_WOOD_HIGH;
                        case MEDIUM -> OPEN_WOOD_MEDIUM;
                        case LARGE -> OPEN_WOOD_LOW;
                        case HUGE -> OPEN_WOOD_DEEP;
                    }).get()
            );
        }
    }
}
