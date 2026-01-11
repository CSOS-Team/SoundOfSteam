package com.finchy.pipeorgans.content.pipes;

import com.finchy.pipeorgans.content.pipes.generic.*;
import com.finchy.pipeorgans.content.pipes.generic.subtypes2.VerticalPipeBlock;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllPartialModels;
import com.finchy.pipeorgans.init.AllShapes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllSoundEvents;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Hohlflute {

    public static class HohlfluteBlock extends VerticalPipeBlock {
        public HohlfluteBlock(Properties pProperties) {
            super(pProperties,
                    ExtensionMode.DOUBLE, PipeMaterial.WOOD,
                    AllBlocks.HOHLFLUTE_EXTENSION,
                    AllBlockEntities.HOHLFLUTE_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);

        }
    }

    public static class HohlfluteExtensionBlock extends GenericExtensionBlock<ExtensionShapes.Double> {
        public HohlfluteExtensionBlock(Properties pProperties) {
            super(pProperties,
                    ExtensionShapes.Double.class,
                    AllBlocks.HOHLFLUTE,
                    AllShapes::slimExtensionShape,
                    false);
        }
    }

    public static class HohlfluteBlockEntity extends GenericPipeBlockEntity {
        public HohlfluteBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.HOHLFLUTE, AllBlocks.HOHLFLUTE_EXTENSION);
        }

        @OnlyIn(Dist.CLIENT)
        protected HohlfluteSoundInstance soundInstance;

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
                        .play(soundInstance = new HohlfluteSoundInstance(size, worldPosition));

                AllSoundEvents.WHISTLE_CHIFF.playAt(level, worldPosition, maxVolume * .1f, f, false);

                particle = true;
            }

            soundInstance.keepAlive();
            soundInstance.setPitch(f);

            if (!particle)
                return;

            createSteamJet(size);
        }
    }

    public static class HohlfluteRenderer extends SafeBlockEntityRenderer<HohlfluteBlockEntity> {

        public HohlfluteRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(HohlfluteBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {

            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof HohlfluteBlock))
                return;

            Direction direction = blockState.getValue(GenericPipeBlock.FACING);
            PipeSize size = blockState.getValue(GenericPipeBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.HOHLFLUTE_MOUTH_TINY;
                case SMALL -> AllPartialModels.HOHLFLUTE_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.HOHLFLUTE_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.HOHLFLUTE_MOUTH_LARGE;
                case HUGE -> AllPartialModels.HOHLFLUTE_MOUTH_HUGE;
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
                    .translateY(4/16f)
                    .translateZ(switch (size) {
                        case TINY -> 6;
                        case SMALL -> 5;
                        case MEDIUM -> 4;
                        case LARGE -> 3;
                        case HUGE -> 2;
                    } /16f)
                    .rotateXDegrees(-offset*16)
                    .light(light)
                    .renderInto(ms, bufferSource.getBuffer(RenderType.solid()));

        }
    }

    public static class HohlfluteSoundInstance extends GenericSoundInstance {

        public HohlfluteSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> HOHLFLUTE_SUPERHIGH;
                        case SMALL -> HOHLFLUTE_HIGH;
                        case MEDIUM -> HOHLFLUTE_MEDIUM;
                        case LARGE -> HOHLFLUTE_LOW;
                        case HUGE -> HOHLFLUTE_DEEP;
                    }).get()
            );
        }
    }
}
