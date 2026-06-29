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

public class Clairon {

    public static class ClaironBlock extends DoublePipeBlock {
        public ClaironBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.CLAIRON_EXTENSION,
                    AllBlockEntities.CLAIRON_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class ClaironExtensionBlock extends DoubleExtensionBlock {
        public ClaironExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.CLAIRON,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class ClaironBlockEntity extends GenericPipeBlockEntity {
        public ClaironBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.CLAIRON, AllBlocks.CLAIRON_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new ClaironSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            createReedSteamJet();
        }
    }

    public static class ClaironRenderer extends SafeBlockEntityRenderer<ClaironBlockEntity> {

        public ClaironRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(ClaironBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof ClaironBlock))
                return;

            Direction direction = blockState.getValue(ClaironBlock.FACING);
            PipeSize size = blockState.getValue(ClaironBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.CLAIRON_MOUTH_TINY;
                case SMALL -> AllPartialModels.CLAIRON_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.CLAIRON_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.CLAIRON_MOUTH_LARGE;
                case HUGE -> AllPartialModels.CLAIRON_MOUTH_HUGE;
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

    public static class ClaironSoundInstance extends GenericSoundInstance {

        public ClaironSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> CLAIRON_SUPERHIGH;
                        case SMALL -> CLAIRON_HIGH;
                        case MEDIUM -> CLAIRON_MEDIUM;
                        case LARGE -> CLAIRON_LOW;
                        case HUGE -> CLAIRON_DEEP;
                    }).get()
            );
        }
    }
}
