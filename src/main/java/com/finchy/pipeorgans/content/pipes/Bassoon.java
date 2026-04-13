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

public class Bassoon {

    public static class BassoonBlock extends SinglePipeBlock {
        public BassoonBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.WOOD,
                    AllBlocks.BASSOON_EXTENSION,
                    AllBlockEntities.BASSOON_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class BassoonExtensionBlock extends SingleExtensionBlock {
        public BassoonExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.BASSOON,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class BassoonBlockEntity extends GenericPipeBlockEntity {
        public BassoonBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.BASSOON, AllBlocks.BASSOON_EXTENSION);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new BassoonSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            createReedSteamJet();
        }
    }

    public static class BassoonRenderer extends SafeBlockEntityRenderer<BassoonBlockEntity> {

        public BassoonRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(BassoonBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof BassoonBlock))
                return;

            Direction direction = blockState.getValue(BassoonBlock.FACING);
            PipeSize size = blockState.getValue(BassoonBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.BASSOON_MOUTH_TINY;
                case SMALL -> AllPartialModels.BASSOON_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.BASSOON_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.BASSOON_MOUTH_LARGE;
                case HUGE -> AllPartialModels.BASSOON_MOUTH_HUGE;
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
                        .translate(0, -2f / 16f, 0)
                        .light(light)
                        .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
            }


        }

    }

    public static class BassoonSoundInstance extends GenericSoundInstance {

        public BassoonSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> BASSOON_SUPERHIGH;
                        case SMALL -> BASSOON_HIGH;
                        case MEDIUM -> BASSOON_MEDIUM;
                        case LARGE -> BASSOON_LOW;
                        case HUGE -> BASSOON_DEEP;
                    }).get()
            );
        }
    }
}
