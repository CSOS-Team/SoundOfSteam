package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SingleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SinglePipeBlock;
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

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Untersatz {

    public static class UntersatzBlock extends SinglePipeBlock {
        public UntersatzBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.WOOD,
                    AllBlocks.UNTERSATZ_EXTENSION,
                    AllBlockEntities.UNTERSATZ_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);

        }
    }

    public static class UntersatzExtensionBlock extends SingleExtensionBlock {
        public UntersatzExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.UNTERSATZ,
                    AllShapes::genericExtensionShape);
        }
    }

    public static class UntersatzBlockEntity extends GenericPipeBlockEntity {
        public UntersatzBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.UNTERSATZ, AllBlocks.UNTERSATZ_EXTENSION);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new UntersatzSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }
    }

    public static class UntersatzRenderer extends SafeBlockEntityRenderer<UntersatzBlockEntity> {

        public UntersatzRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(UntersatzBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof UntersatzBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.UNTERSATZ_MOUTH_TINY;
                case SMALL -> AllPartialModels.UNTERSATZ_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.UNTERSATZ_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.UNTERSATZ_MOUTH_LARGE;
                case HUGE -> AllPartialModels.UNTERSATZ_MOUTH_HUGE;
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
                    .translate(0, -offset*3 / 16f, 0)
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

    public static class UntersatzSoundInstance extends GenericSoundInstance {

        public UntersatzSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> UNTERSATZ_SUPERHIGH;
                        case SMALL -> UNTERSATZ_HIGH;
                        case MEDIUM -> UNTERSATZ_MEDIUM;
                        case LARGE -> UNTERSATZ_LOW;
                        case HUGE -> UNTERSATZ_DEEP;
                    }).get()
            );
        }
    }
}
