package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllShapes;
import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Diapason {

    public static class DiapasonBlock extends DoublePipeBlock {
        public DiapasonBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.DIAPASON_EXTENSION,
                    AllBlockEntities.DIAPASON_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);

        }
    }

    public static class DiapasonExtensionBlock extends DoubleExtensionBlock {
        public DiapasonExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.DIAPASON,
                    AllShapes::genericExtensionShape);
        }
    }

    public static class DiapasonBlockEntity extends GenericPipeBlockEntity {
        public DiapasonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.DIAPASON, AllBlocks.DIAPASON_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new DiapasonSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }
    }

    public static class DiapasonRenderer extends SafeBlockEntityRenderer<DiapasonBlockEntity> {

        public DiapasonRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(DiapasonBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof DiapasonBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.DIAPASON_MOUTH_TINY;
                case SMALL -> AllPartialModels.DIAPASON_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.DIAPASON_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.DIAPASON_MOUTH_LARGE;
                case HUGE -> AllPartialModels.DIAPASON_MOUTH_HUGE;
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

    public static class DiapasonSoundInstance extends GenericSoundInstance {

        public DiapasonSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> DIAPASON_SUPERHIGH;
                        case SMALL -> DIAPASON_HIGH;
                        case MEDIUM -> DIAPASON_MEDIUM;
                        case LARGE -> DIAPASON_LOW;
                        case HUGE -> DIAPASON_DEEP;
                    }).get()
            );
        }
    }
}
