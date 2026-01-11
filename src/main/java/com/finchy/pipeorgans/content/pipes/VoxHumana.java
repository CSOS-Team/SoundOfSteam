package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes2.VerticalPipeBlock;
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

public class VoxHumana {

    public static class VoxHumanaBlock extends VerticalPipeBlock {
        public VoxHumanaBlock(Properties pProperties) {
            super(pProperties,
                    ExtensionMode.QUADRUPLE, PipeMaterial.METAL,
                    AllBlocks.VOX_HUMANA_EXTENSION,
                    AllBlockEntities.VOX_HUMANA_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class VoxHumanaExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Quadruple> {
        public VoxHumanaExtensionBlock(Properties pProperties) {
            super(pProperties,
                    ExtensionShapes.Quadruple.class,
                    AllBlocks.VOX_HUMANA,
                    AllShapes::slimExtensionShape,
                    false);
        }
    }

    public static class VoxHumanaBlockEntity extends GenericPipeBlockEntity {
        public VoxHumanaBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.VOX_HUMANA, AllBlocks.VOX_HUMANA_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected VoxHumanaSoundInstance soundInstance;

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
                        .play(soundInstance = new VoxHumanaSoundInstance(size, worldPosition));

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

    public static class VoxHumanaRenderer extends SafeBlockEntityRenderer<VoxHumanaBlockEntity> {

        public VoxHumanaRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(VoxHumanaBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof VoxHumanaBlock))
                return;

            Direction direction = blockState.getValue(VoxHumanaBlock.FACING);
            PipeSize size = blockState.getValue(VoxHumanaBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.VOX_HUMANA_MOUTH_TINY;
                case SMALL -> AllPartialModels.VOX_HUMANA_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.VOX_HUMANA_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.VOX_HUMANA_MOUTH_LARGE;
                case HUGE -> AllPartialModels.VOX_HUMANA_MOUTH_HUGE;
            };

            float chaseTarget = be.animation.getChaseTarget();

            CachedBuffers.partial(mouth, blockState)
                    .center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                    .uncenter()
                    .scale(chaseTarget)
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

        }
    }

    public static class VoxHumanaSoundInstance extends GenericSoundInstance {

        public VoxHumanaSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> VOX_HUMANA_SUPERHIGH;
                        case SMALL -> VOX_HUMANA_HIGH;
                        case MEDIUM -> VOX_HUMANA_MEDIUM;
                        case LARGE -> VOX_HUMANA_LOW;
                        case HUGE -> VOX_HUMANA_DEEP;
                    }).get()
            );
        }
    }
}
