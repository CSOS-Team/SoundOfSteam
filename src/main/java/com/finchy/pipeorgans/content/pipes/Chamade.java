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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Chamade {

    public static class ChamadeBlock extends DoublePipeBlock {
        public ChamadeBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.HORIZONTAL, PipeMaterial.METAL,
                    AllBlocks.CHAMADE_EXTENSION,
                    AllBlockEntities.CHAMADE_BLOCK_ENTITY,
                    AllShapes::horizontalPipeShape);

        }
    }

    public static class ChamadeExtensionBlock extends DoubleExtensionBlock {
        public ChamadeExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.CHAMADE,
                    AllShapes::horizontalExtensionShape);
        }

        @Override
        public boolean isDirectional() {
            return true;
        }
    }

    public static class ChamadeBlockEntity extends GenericPipeBlockEntity {
        public ChamadeBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.CHAMADE, AllBlocks.CHAMADE_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new ChamadeSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            createHorizontalReedSteamJet();
        }
    }

    public static class ChamadeRenderer extends SafeBlockEntityRenderer<ChamadeBlockEntity> {

        public ChamadeRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(ChamadeBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof ChamadeBlock))
                return;

            Direction direction = blockState.getValue(ChamadeBlock.FACING);
            PipeSize size = blockState.getValue(ChamadeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.CHAMADE_MOUTH_TINY;
                case SMALL -> AllPartialModels.CHAMADE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.CHAMADE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.CHAMADE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.CHAMADE_MOUTH_HUGE;
            };

            PartialModel goggles = switch (size) {
                case TINY -> AllPartialModels.HORIZONTAL_GOGGLES_TINY;
                case SMALL -> AllPartialModels.HORIZONTAL_GOGGLES_SMALL;
                case MEDIUM -> AllPartialModels.HORIZONTAL_GOGGLES_MEDIUM;
                case LARGE -> AllPartialModels.HORIZONTAL_GOGGLES_LARGE;
                case HUGE -> AllPartialModels.HORIZONTAL_GOGGLES_HUGE;
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
                        .light(light)
                        .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
            }

        }
    }

    public static class ChamadeSoundInstance extends GenericSoundInstance {

        public ChamadeSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> CHAMADE_SUPERHIGH;
                        case SMALL -> CHAMADE_HIGH;
                        case MEDIUM -> CHAMADE_MEDIUM;
                        case LARGE -> CHAMADE_LOW;
                        case HUGE -> CHAMADE_DEEP;
                    }).get()
            );
        }
    }
}
