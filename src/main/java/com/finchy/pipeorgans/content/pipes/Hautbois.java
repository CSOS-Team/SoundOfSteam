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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.finchy.pipeorgans.init.AllSoundEvents.*;

public class Hautbois {

    public static class HautboisBlock extends DoublePipeBlock {
        public HautboisBlock(Properties pProperties) {
            super(pProperties,
                    PipeDirection.VERTICAL, PipeMaterial.METAL,
                    AllBlocks.HAUTBOIS_EXTENSION,
                    AllBlockEntities.HAUTBOIS_BLOCK_ENTITY,
                    AllShapes::slimPipeShape);
        }

        @Override
        public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
            ItemStack heldItem = player.getItemInHand(hand);
            BlockEntity be = level.getBlockEntity(pos);

            if (be instanceof HautboisBlockEntity hautboisBe) {
                if (hautboisBe.hasFlower() && heldItem.isEmpty()) {
                    if (!level.isClientSide) {
                        hautboisBe.setHasFlower(false);
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }

                if (heldItem.is(ItemTags.FLOWERS)) {
                  if (!level.isClientSide) {
                      int color = 0xFF00FF;
                      if (heldItem.is(net.minecraft.world.item.Items.POPPY)) color = 0xFF0000;
                      else if (heldItem.is(net.minecraft.world.item.Items.DANDELION)) color = 0xFFFF00;
                      else if (heldItem.is(net.minecraft.world.item.Items.BLUE_ORCHID)) color = 0x00A3FF;
                      else if (heldItem.is(net.minecraft.world.item.Items.OXEYE_DAISY)) color = 0xFFFFFF;
                      else if (heldItem.is(net.minecraft.world.item.Items.CORNFLOWER)) color = 0x0000FF;
                      else if (heldItem.is(net.minecraft.world.item.Items.WITHER_ROSE)) color = 0x222222;
                      else if (heldItem.is(net.minecraft.world.item.Items.ALLIUM)) color = 0xCC44FF;
                      else if (heldItem.is(net.minecraft.world.item.Items.ORANGE_TULIP)) color = 0xFF6A00;
                      else if (heldItem.is(net.minecraft.world.item.Items.PINK_TULIP)) color = 0xFFB2D6;
                      else if (heldItem.is(net.minecraft.world.item.Items.RED_TULIP)) color = 0xDD1111;
                      else if (heldItem.is(net.minecraft.world.item.Items.WHITE_TULIP)) color = 0xEAF2F2;
                      else if (heldItem.is(net.minecraft.world.item.Items.SUNFLOWER)) color = 0xFFFF00;

                      hautboisBe.setHasFlower(true);
                      hautboisBe.settintColor(color);

                      if (player instanceof ServerPlayer serverPlayer) {
                          com.finchy.pipeorgans.init.AllTriggers.HAUTBOIS_FLOWER.trigger(serverPlayer);
                      }
                  }
                  return InteractionResult.sidedSuccess(level.isClientSide);
              }
            }

            return super.use(state, level, pos, player, hand, hit);
        }
    }

    public static class HautboisExtensionBlock extends DoubleExtensionBlock {
        public HautboisExtensionBlock(Properties pProperties) {
            super(pProperties,
                    AllBlocks.HAUTBOIS,
                    AllShapes::slimExtensionShape);
        }
    }

    public static class HautboisBlockEntity extends GenericPipeBlockEntity {
        private boolean hasFlower = false;
        private int tintColor = -1;

        public HautboisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
            super(type, pos, blockState,
                    AllBlocks.HAUTBOIS, AllBlocks.HAUTBOIS_EXTENSION);
        }

        public boolean hasFlower() { return this.hasFlower; }
        public void setHasFlower(boolean hasFlower) { this.hasFlower = hasFlower; notifyUpdate(); }
        public int gettintColor() { return this.tintColor; }
        public void settintColor(int color) { this.tintColor = color; notifyUpdate(); }

        @Override
        public void write(CompoundTag compound, boolean clientPacket) {
            compound.putBoolean("HasFlower", hasFlower);
            compound.putInt("tintColor", tintColor);
            super.write(compound, clientPacket);
        }

        @Override
        public void read(CompoundTag compound, boolean clientPacket) {
            this.hasFlower = compound.getBoolean("HasFlower");
            this.tintColor = compound.getInt("tintColor");
            super.read(compound, clientPacket);
        }

        @Override
        public void createSteamJet(PipeSize size) {
            if (this.hasFlower()) {
                if (level != null && level.isClientSide) {
                    double x = worldPosition.getX() + 0.5;
                    double y = worldPosition.getY() + 1.2;
                    double z = worldPosition.getZ() + 0.5;

                    int c = this.gettintColor() != -1 ? this.gettintColor() : 0xFF00FF;
                    float r = ((c >> 16) & 0xFF) / 255.0f;
                    float g = ((c >> 8) & 0xFF) / 255.0f;
                    float b = (c & 0xFF) / 255.0f;

                    net.minecraft.core.particles.DustParticleOptions flowerDust =
                        new net.minecraft.core.particles.DustParticleOptions(
                            new org.joml.Vector3f(r, g, b), 1.4f
                        );

                    for (int i = 0; i < 4; i++) {
                        double xSpeed = (level.random.nextDouble() - 0.5) * 0.05;
                        double ySpeed = 0.4 + (level.random.nextDouble() * 0.2);
                        double zSpeed = (level.random.nextDouble() - 0.5) * 0.05;

                        level.addParticle(flowerDust, x, y, z, xSpeed, ySpeed, zSpeed);

                        if (level.random.nextBoolean()) {
                            level.addParticle(ParticleTypes.HEART, x, y, z, xSpeed, ySpeed, zSpeed);
                        }
                    }
                }
            } else {
                createReedSteamJet();
            }
        }

        @OnlyIn(Dist.CLIENT)
        @Override
        protected void handleSoundInstance(PipeSize size) {
            Minecraft.getInstance()
                    .getSoundManager()
                    .play(soundInstance = new HautboisSoundInstance(size, worldPosition));

            playChiffSound(0.1f);
        }
    }

    public static class HautboisRenderer extends SafeBlockEntityRenderer<HautboisBlockEntity> {
        public HautboisRenderer(BlockEntityRendererProvider.Context context) {}

        @Override
        protected void renderSafe(HautboisBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay) {
            BlockState blockState = be.getBlockState();
            if (!(blockState.getBlock() instanceof HautboisBlock))
                return;

            Direction direction = blockState.getValue(HautboisBlock.FACING);
            PipeSize size = blockState.getValue(HautboisBlock.SIZE);

            PartialModel mouth = switch (size) {
                case TINY -> AllPartialModels.HAUTBOIS_MOUTH_TINY;
                case SMALL -> AllPartialModels.HAUTBOIS_MOUTH_SMALL;
                case MEDIUM -> AllPartialModels.HAUTBOIS_MOUTH_MEDIUM;
                case LARGE -> AllPartialModels.HAUTBOIS_MOUTH_LARGE;
                case HUGE -> AllPartialModels.HAUTBOIS_MOUTH_HUGE;
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

    public static class HautboisSoundInstance extends GenericSoundInstance {
        public HautboisSoundInstance(PipeSize size, BlockPos worldPosition) {
            super(size, worldPosition,
                    (switch (size) {
                        case TINY -> HAUTBOIS_SUPERHIGH;
                        case SMALL -> HAUTBOIS_HIGH;
                        case MEDIUM -> HAUTBOIS_MEDIUM;
                        case LARGE -> HAUTBOIS_LOW;
                        case HUGE -> HAUTBOIS_DEEP;
                    }).get()
            );
        }
    }
}
