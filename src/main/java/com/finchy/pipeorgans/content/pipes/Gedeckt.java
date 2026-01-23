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

public class Gedeckt {

    public static class GedecktBlock extends DoublePipeBlock {
        public GedecktBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.WOOD,
                    AllBlocks.GEDECKT_EXTENSION,
                    AllBlockEntities.GEDECKT_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);

        }
    }

    public static class GedecktExtensionBlock extends DoubleExtensionBlock {
        public GedecktExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.GEDECKT,
                    AllShapes::genericExtensionShape);
        }
    }

    public static class GedecktBlockEntity extends GenericPipeBlockEntity {
        public GedecktBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.GEDECKT, AllBlocks.GEDECKT_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected GedecktSoundInstance soundInstance;

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
                        .play(soundInstance = new GedecktSoundInstance(size, worldPosition));

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

    public static class GedecktRenderer extends SafeBlockEntityRenderer<GedecktBlockEntity> {

        public GedecktRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(GedecktBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof GedecktBlock))
                return;

            Direction direction = blockState.getValue(GedecktBlock.FACING);
            PipeSize size = blockState.getValue(GedecktBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.GEDECKT_MOUTH_TINY;
                case SMALL -> AllPartialModels.GEDECKT_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.GEDECKT_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.GEDECKT_MOUTH_LARGE;
                case HUGE -> AllPartialModels.GEDECKT_MOUTH_HUGE;
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
                    .translateY(4/16f)
                    .translateZ(switch (size) {
                        case TINY -> 6;
                        case SMALL -> 5;
                        case MEDIUM -> 4;
                        case LARGE -> 3;
                        case HUGE -> 2;
                    } /16f)
                    .rotateXDegrees(-offset*16)
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

    public static class GedecktSoundInstance extends GenericSoundInstance {

        public GedecktSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> GEDECKT_SUPERHIGH;
                        case SMALL -> GEDECKT_HIGH;
                        case MEDIUM -> GEDECKT_MEDIUM;
                        case LARGE -> GEDECKT_LOW;
                        case HUGE -> GEDECKT_DEEP;
                    }).get()
            );
        }
    }
}
