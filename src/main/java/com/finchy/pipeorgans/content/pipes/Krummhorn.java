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

public class Krummhorn {

    public static class KrummhornBlock extends DoublePipeBlock {
        public KrummhornBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.KRUMMHORN_EXTENSION,
                    AllBlockEntities.KRUMMHORN_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class KrummhornExtensionBlock extends DoubleExtensionBlock {
        public KrummhornExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.KRUMMHORN,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class KrummhornBlockEntity extends GenericPipeBlockEntity {
        public KrummhornBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.KRUMMHORN, AllBlocks.KRUMMHORN_EXTENSION);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new KrummhornSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            createReedSteamJet();
        }
    }

    public static class KrummhornRenderer extends SafeBlockEntityRenderer<KrummhornBlockEntity> {

        public KrummhornRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(KrummhornBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof KrummhornBlock))
                return;

            Direction direction = blockState.getValue(KrummhornBlock.FACING);
            PipeSize size = blockState.getValue(KrummhornBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.KRUMMHORN_MOUTH_TINY;
                case SMALL -> AllPartialModels.KRUMMHORN_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.KRUMMHORN_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.KRUMMHORN_MOUTH_LARGE;
                case HUGE -> AllPartialModels.KRUMMHORN_MOUTH_HUGE;
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

    public static class KrummhornSoundInstance extends GenericSoundInstance {

        public KrummhornSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> KRUMMHORN_SUPERHIGH;
                        case SMALL -> KRUMMHORN_HIGH;
                        case MEDIUM -> KRUMMHORN_MEDIUM;
                        case LARGE -> KRUMMHORN_LOW;
                        case HUGE -> KRUMMHORN_DEEP;
                    }).get()
            );
        }
    }
}
