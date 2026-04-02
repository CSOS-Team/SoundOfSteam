package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoublePipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllShapes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
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
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class EnglishHorn {

    public static class EnglishHornBlock extends DoublePipeBlock {
        public EnglishHornBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.ENGLISH_HORN_EXTENSION,
                    AllBlockEntities.ENGLISH_HORN_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class EnglishHornExtensionBlock extends DoubleExtensionBlock {
        public EnglishHornExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.ENGLISH_HORN,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class EnglishHornBlockEntity extends GenericPipeBlockEntity {
        public EnglishHornBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.ENGLISH_HORN, AllBlocks.ENGLISH_HORN_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected EnglishHornSoundInstance soundInstance;

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

                if (!isVirtual()) {

                    Minecraft.getInstance()
                            .getSoundManager()
                            .play(soundInstance = new EnglishHornSoundInstance(size, worldPosition));

                    playChiffSound(0.1f);
                }

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createReedSteamJet();
        }
    }

    public static class EnglishHornRenderer extends SafeBlockEntityRenderer<EnglishHornBlockEntity> {

        public EnglishHornRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(EnglishHornBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof EnglishHornBlock))
                return;

            Direction direction = blockState.getValue(EnglishHornBlock.FACING);
            PipeSize size = blockState.getValue(EnglishHornBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.ENGLISH_HORN_MOUTH_TINY;
                case SMALL -> AllPartialModels.ENGLISH_HORN_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.ENGLISH_HORN_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.ENGLISH_HORN_MOUTH_LARGE;
                case HUGE -> AllPartialModels.ENGLISH_HORN_MOUTH_HUGE;
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

    public static class EnglishHornSoundInstance extends GenericSoundInstance {

        public EnglishHornSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> ENGLISH_HORN_SUPERHIGH;
                        case SMALL -> ENGLISH_HORN_HIGH;
                        case MEDIUM -> ENGLISH_HORN_MEDIUM;
                        case LARGE -> ENGLISH_HORN_LOW;
                        case HUGE -> ENGLISH_HORN_DEEP;
                    }).get()
            );
        }
    }
}
