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

public class Piccolo {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static class PiccoloBlock extends QuadruplePipeBlock implements ProperWaterloggedBlock {
        public PiccoloBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.PICCOLO_EXTENSION,
                    AllBlockEntities.PICCOLO_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);

            registerDefaultState(defaultBlockState()
                    .setValue(WATERLOGGED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(WATERLOGGED);
        }

        @Override
        public FluidState getFluidState(BlockState pState) {
            return fluidState(pState);
        }

        @Override
        public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
            BlockState placementState = super.getStateForPlacement(context);
            return withWater(placementState, context);
        }

        @Override
        public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
            updateWater(pLevel, pState, pCurrentPos);
            return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
        }
    }

    public static class PiccoloExtensionBlock extends QuadrupleExtensionBlock implements ProperWaterloggedBlock {
        public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

        public PiccoloExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.PICCOLO,
                    AllShapes::genericExtensionShape);
            registerDefaultState(defaultBlockState()
                    .setValue(WATERLOGGED, false));
        }

        @Override
        protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
            super.createBlockStateDefinition(builder);
            builder.add(WATERLOGGED);
        }

        @Override
        public FluidState getFluidState(BlockState pState) {
            return fluidState(pState);
        }

        @Override
        public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
            BlockState placementState = super.getStateForPlacement(context);
            return withWater(placementState, context);
        }
    }

    public static class PiccoloBlockEntity extends GenericPipeBlockEntity {

        public PiccoloBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.PICCOLO, AllBlocks.PICCOLO_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected PiccoloSoundInstance.PiccoloWaterSoundInstance waterSound;

        @Override
        @OnlyIn(Dist.CLIENT)
        protected void tickAudio(PipeSize size, boolean powered) {
            BlockState state = getBlockState();

            if (!powered) {
                if (soundInstance != null) {
                    soundInstance.fadeOut();
                    soundInstance = null;
                }
                if (waterSound != null) {
                    waterSound.fadeOut();
                    waterSound = null;
                }
                return;
            }

            if (state.getValue(Piccolo.WATERLOGGED)) {

                if (soundInstance != null) {
                    soundInstance.fadeOut();
                    soundInstance = null;
                }

                tickWaterlogged(size);
                return;
            }
            if (waterSound != null) {
                waterSound.fadeOut();
                waterSound = null;
            }

            float f = (float) Math.pow(2, -pitch / 12.0);
            boolean particle = level.getGameTime() % 8 == 0;

            Vec3 eyePosition = Minecraft.getInstance().cameraEntity.getEyePosition();
            float maxVolume = (float) Mth.clamp(
                    (64 - eyePosition.distanceTo(Vec3.atCenterOf(worldPosition))) / 64,
                    0, 1
            );

            if (soundInstance == null || soundInstance.isStopped() || soundInstance.getOctave() != size) {

                if (!isVirtual()) {

                    handleSoundInstance(size);
                }

                particle = true;
            }

            if (soundInstance != null) {
                soundInstance.keepAlive();
                soundInstance.setPitch(f);
            }

            if (!particle)
                return;

            createSteamJet(size);
        }

        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new PiccoloSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }

        @OnlyIn(Dist.CLIENT)
        private void tickWaterlogged(PipeSize size) {
            if (waterSound == null || waterSound.isStopped()) {
                Minecraft.getInstance()
                        .getSoundManager()
                        .play(waterSound =
                                new PiccoloSoundInstance.PiccoloWaterSoundInstance(worldPosition));
            }
            waterSound.keepAlive();

            if (level.getGameTime() % 2 == 0) { // medium-fast stream
                Direction facing = getBlockState().getValue(GenericPipeBlock.FACING);

                Vec3 dir = Vec3.atLowerCornerOf(facing.getNormal());

                double offset = 0.45 - (3.0 / 16.0); // move 3 pixels toward center

                double x = worldPosition.getX() + 0.5 + -dir.x * offset;
                double y = worldPosition.getY() + 0.5;
                double z = worldPosition.getZ() + 0.5 + -dir.z * offset;


                double speed = 0.05;
                double vx = -dir.x * speed;
                double vy = 0.03; // rise to surface
                double vz = -dir.z * speed;

                for (int i = 0; i < 2; i++) {
                    level.addParticle(
                            net.minecraft.core.particles.ParticleTypes.BUBBLE, x, y, z,
                            vx + (level.random.nextGaussian() * 0.005),
                            vy + (level.random.nextGaussian() * 0.005),
                            vz + (level.random.nextGaussian() * 0.005)
                    );
                }
            }

        }

        private final PipePowerState powerState = new PipePowerState();

        @Override
        public void tick() {
            super.tick();

            if (level == null)
                return;

            if (!level.isClientSide) {
                if (powerState.tickAndCheckRisingEdge(level, this)
                        && getBlockState().getValue(Piccolo.WATERLOGGED)) {
                    RangedPowerAdvancement.trigger(level, worldPosition, 16,
                            AllTriggers.WATER_PIPE::trigger
                    );
                }
                return;
            }
        }
    }

    public static class PiccoloRenderer extends SafeBlockEntityRenderer<PiccoloBlockEntity> {

        public PiccoloRenderer(BlockEntityRendererProvider.Context context) {
        }

        @Override
        protected void renderSafe(PiccoloBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof PiccoloBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.PICCOLO_MOUTH_TINY;
                case SMALL -> AllPartialModels.PICCOLO_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.PICCOLO_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.PICCOLO_MOUTH_LARGE;
                case HUGE -> AllPartialModels.PICCOLO_MOUTH_HUGE;
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

    public static class PiccoloSoundInstance extends GenericSoundInstance {

        public PiccoloSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> PICCOLO_SUPERHIGH;
                        case SMALL -> PICCOLO_HIGH;
                        case MEDIUM -> PICCOLO_MEDIUM;
                        case LARGE -> PICCOLO_LOW;
                        case HUGE -> PICCOLO_DEEP;
                    }).get()
            );
        }

        @OnlyIn(Dist.CLIENT)
        public static class PiccoloWaterSoundInstance extends GenericSoundInstance {

            public PiccoloWaterSoundInstance(BlockPos pos) {
                super(
                        PipeSize.TINY, // size is irrelevant here
                        pos,
                        PICCOLO_WATER.get()
                );

                looping = true;
                volume = 0.35f;
                pitch = 1.0f;
            }
        }
    }
}
