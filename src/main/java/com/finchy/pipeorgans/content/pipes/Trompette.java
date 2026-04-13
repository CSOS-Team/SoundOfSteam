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
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Trompette {

    public static class TrompetteBlock extends DoublePipeBlock {
        public TrompetteBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.TROMPETTE_EXTENSION,
                    AllBlockEntities.TROMPETTE_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class TrompetteExtensionBlock extends DoubleExtensionBlock {
        public TrompetteExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.TROMPETTE,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class TrompetteBlockEntity extends GenericPipeBlockEntity {
        public TrompetteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.TROMPETTE, AllBlocks.TROMPETTE_EXTENSION);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new TrompetteSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            createReedSteamJet();
        }
    }

    public static class TrompetteRenderer extends SafeBlockEntityRenderer<TrompetteBlockEntity> {

        public TrompetteRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(TrompetteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof TrompetteBlock))
                return;

            Direction direction = blockState.getValue(TrompetteBlock.FACING);
            PipeSize size = blockState.getValue(TrompetteBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.TROMPETTE_MOUTH_TINY;
                case SMALL -> AllPartialModels.TROMPETTE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.TROMPETTE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.TROMPETTE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.TROMPETTE_MOUTH_HUGE;
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

    public static class TrompetteSoundInstance extends GenericSoundInstance {

        public TrompetteSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> TROMPETTE_SUPERHIGH;
                        case SMALL -> TROMPETTE_HIGH;
                        case MEDIUM -> TROMPETTE_MEDIUM;
                        case LARGE -> TROMPETTE_LOW;
                        case HUGE -> TROMPETTE_DEEP;
                    }).get()
            );
        }
    }
}
