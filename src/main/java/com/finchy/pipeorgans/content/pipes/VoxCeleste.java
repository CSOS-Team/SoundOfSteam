package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
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

public class VoxCeleste {

    public static class VoxCelesteBlock extends DoublePipeBlock {
        public VoxCelesteBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.VOX_CELESTE_EXTENSION,
                    AllBlockEntities.VOX_CELESTE_BLOCK_ENTITY,
                    AllShapes::stringPipeShape);

        }
    }

    public static class VoxCelesteExtensionBlock extends DoubleExtensionBlock {
        public VoxCelesteExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.VOX_CELESTE,
                    AllShapes::stringExtensionShape);
        }

        @Override
        public boolean isDirectional() {
            return true;
        }
    }

    public static class VoxCelesteBlockEntity extends GenericPipeBlockEntity {
        public VoxCelesteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.VOX_CELESTE, AllBlocks.VOX_CELESTE_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected VoxCelesteSoundInstance soundInstance;

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
                        .play(soundInstance = new VoxCelesteSoundInstance(size, worldPosition));

                AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f, f, false);

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createSteamJet(size);
        }
    }

    public static class VoxCelesteRenderer extends SafeBlockEntityRenderer<VoxCelesteBlockEntity> {

        public VoxCelesteRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(VoxCelesteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof VoxCelesteBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.VOX_CELESTE_MOUTH_TINY;
                case SMALL -> AllPartialModels.VOX_CELESTE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.VOX_CELESTE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.VOX_CELESTE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.VOX_CELESTE_MOUTH_HUGE;
            };
            PartialModel goggles = switch (size) {
                case TINY -> AllPartialModels.STRING_GOGGLES_TINY;
                case SMALL -> AllPartialModels.STRING_GOGGLES_SMALL;
                case MEDIUM -> AllPartialModels.STRING_GOGGLES_MEDIUM;
                case LARGE -> AllPartialModels.STRING_GOGGLES_LARGE;
                case HUGE -> AllPartialModels.STRING_GOGGLES_HUGE;
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
                    .translate(0, -offset / 16f, 0)
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

    public static class VoxCelesteSoundInstance extends GenericSoundInstance {

        public VoxCelesteSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> VOX_CELESTE_SUPERHIGH;
                        case SMALL -> VOX_CELESTE_HIGH;
                        case MEDIUM -> VOX_CELESTE_MEDIUM;
                        case LARGE -> VOX_CELESTE_LOW;
                        case HUGE -> VOX_CELESTE_DEEP;
                    }).get()
            );
        }
    }
}
