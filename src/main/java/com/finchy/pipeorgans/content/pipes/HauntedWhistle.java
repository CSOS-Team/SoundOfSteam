package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.particles.hauntedJet.HauntedJetParticleData;
import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.*;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class HauntedWhistle {

    public static class HauntedWhistleBlock extends DoublePipeBlock {
        public HauntedWhistleBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.HAUNTED,
                    AllBlocks.HAUNTED_WHISTLE_EXTENSION,
                    AllBlockEntities.HAUNTED_WHISTLE_BLOCK_ENTITY,
                    AllShapes::genericPipeShape);

        }
    }

    public static class HauntedWhistleExtensionBlock extends DoubleExtensionBlock {
        public HauntedWhistleExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.HAUNTED_WHISTLE,
                    AllShapes::genericExtensionShape);
        }
    }

    public static class HauntedWhistleBlockEntity extends GenericPipeBlockEntity {
        public HauntedWhistleBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.HAUNTED_WHISTLE, AllBlocks.HAUNTED_WHISTLE_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected HauntedWhistleSoundInstance soundInstance;

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
                        .play(soundInstance = new HauntedWhistleSoundInstance(size, worldPosition));

                level.playLocalSound(worldPosition, AllSoundEvents.HAUNTED_CHIFF.get(), SoundSource.RECORDS, maxVolume * .6f, f, false);

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createSteamJet(size);
        }
        
        @Override
        public void createSteamJet(PipeSize size) {
            Direction facing = getBlockState().getOptionalValue(GenericPipeBlock.FACING)
                    .orElse(Direction.SOUTH);
            float angle = 180 + AngleHelper.horizontalAngle(facing);
            Vec3 sizeOffset = VecHelper.rotate(new Vec3(0, -0.4f, 1 / 16f * size.ordinal()), angle, Direction.Axis.Y);
            Vec3 offset = VecHelper.rotate(new Vec3(0, 1, 0.75f), angle, Direction.Axis.Y);
            Vec3 v = offset.scale(.45f)
                    .add(sizeOffset)
                    .add(Vec3.atCenterOf(worldPosition));
            Vec3 m = offset.subtract(Vec3.atLowerCornerOf(facing.getNormal())
                    .scale(.75f));
            level.addParticle(new HauntedJetParticleData(1), v.x, v.y, v.z, m.x, m.y, m.z);
        }
    }

    public static class HauntedWhistleRenderer extends SafeBlockEntityRenderer<HauntedWhistleBlockEntity> {

        public HauntedWhistleRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(HauntedWhistleBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof HauntedWhistleBlock))
                return;

            Direction direction = blockState.getValue(HauntedWhistleBlock.FACING);
            PipeSize size = blockState.getValue(HauntedWhistleBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.HAUNTED_WHISTLE_MOUTH_TINY;
                case SMALL -> AllPartialModels.HAUNTED_WHISTLE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.HAUNTED_WHISTLE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.HAUNTED_WHISTLE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.HAUNTED_WHISTLE_MOUTH_HUGE;
            };
            PartialModel goggles = switch (size) {
                case TINY -> AllPartialModels.WHISTLE_GOGGLES_TINY;
                case SMALL -> AllPartialModels.WHISTLE_GOGGLES_SMALL;
                case MEDIUM -> AllPartialModels.WHISTLE_GOGGLES_MEDIUM;
                case LARGE -> AllPartialModels.WHISTLE_GOGGLES_LARGE;
                case HUGE -> AllPartialModels.WHISTLE_GOGGLES_HUGE;
            };

            float offset = be.animation.getValue(partialTicks);
            if (be.animation.getChaseTarget() > 0 && be.animation.getValue() > 0.5f) {
                float wiggleProgress = (AnimationTickHolder.getTicks(be.getLevel()) + partialTicks) /8f;
                offset -= (float) (Math.sin(wiggleProgress * (2 * Mth.PI) * (4 - size.ordinal())) / 16f);
            }

            CachedBuffers.partial(mouth, blockState)
                    .center()
                    .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                    .uncenter()
                    .translate(0, offset * 4 / 16f, 0)
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));
            if (be.hasGoggles()) {
                CachedBuffers.partial(goggles, blockState)
                        .center()
                        .rotateYDegrees(AngleHelper.horizontalAngle(direction))
                        .uncenter()
                        .translate(0, offset * 4 / 16f, 0)
                        .light(light)
                        .renderInto(ms, bufferSource.getBuffer(RenderType.cutout()));
            }

        }
    }

    public static class HauntedWhistleSoundInstance extends GenericSoundInstance {

        public HauntedWhistleSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> HAUNTED_WHISTLE_SUPERHIGH;
                        case SMALL -> HAUNTED_WHISTLE_HIGH;
                        case MEDIUM -> HAUNTED_WHISTLE_MEDIUM;
                        case LARGE -> HAUNTED_WHISTLE_LOW;
                        case HUGE -> HAUNTED_WHISTLE_DEEP;
                    }).get()
            );
        }
    }
}
