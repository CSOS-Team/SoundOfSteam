package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadruplePipeBlock;
import com.finchy.pipeorgans.init.*;
import com.finchy.pipeorgans.util.RangedPowerAdvancement;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.block.ProperWaterloggedBlock;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;
import static com.finchy.pipeorgans.init.AllSoundEvents.OKTAV_DEEP;

public class Oktav {

    public static class OktavBlock extends QuadruplePipeBlock {
        public OktavBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.OKTAV_EXTENSION,
                    AllBlockEntities.OKTAV_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);
        }
    }

    public static class OktavExtensionBlock extends QuadrupleExtensionBlock{
        public OktavExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.OKTAV,
                    AllShapes::genericExtensionShape);
        }
    }

    public static class OktavBlockEntity extends GenericPipeBlockEntity {
        public OktavBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.OKTAV, AllBlocks.OKTAV_EXTENSION);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new Oktav.OktavSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }
    }


    public static class OktavRenderer extends SafeBlockEntityRenderer<OktavBlockEntity> {

        public OktavRenderer(BlockEntityRendererProvider.Context context) {
        }

        @Override
        protected void renderSafe(OktavBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof OktavBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.OKTAV_MOUTH_TINY;
                case SMALL -> AllPartialModels.OKTAV_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.OKTAV_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.OKTAV_MOUTH_LARGE;
                case HUGE -> AllPartialModels.OKTAV_MOUTH_HUGE;
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
                float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) / 8f;
                offset -= (float) (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
            }

            CachedBuffers.partial(mouth, blockState)
                    .center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                    .uncenter()
                    .translate(0, -offset / 8f, 0)
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

    public static class OktavSoundInstance extends GenericSoundInstance {

        public OktavSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> OKTAV_SUPERHIGH;
                        case SMALL -> OKTAV_HIGH;
                        case MEDIUM -> OKTAV_MEDIUM;
                        case LARGE -> OKTAV_LOW;
                        case HUGE -> OKTAV_DEEP;
                    }).get()
            );
        }

    }
}
