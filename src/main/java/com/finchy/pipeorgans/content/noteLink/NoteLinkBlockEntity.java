package com.finchy.pipeorgans.content.noteLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.ClipboardAssistedPlacement;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.ClipboardAssistedPlacementBehaviour;
import com.finchy.pipeorgans.infrastructure.itemValueBox.ItemValueBoxBehaviour;
import com.finchy.pipeorgans.infrastructure.pipePitchScrollValue.PipePitchScrollValueBehaviour;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.util.PipePitch;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import net.createmod.catnip.math.AngleHelper;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Set;

public class NoteLinkBlockEntity extends SmartBlockEntity implements ClipboardAssistedPlacement {

    public static final ValueBoxTransform KEY_SLOT_TRANSFORM = new ValueBoxTransform() {
        // TODO: adjust to actual model, this is just a placeholder from the RedstoneLink
        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(NoteLinkBlock.FACING);
            Vec3 location = VecHelper.voxelSpace(8f, 3.01f, 11f);

            if (facing.getAxis().isHorizontal()) {
                location = VecHelper.voxelSpace(8f, 11f, 3.01f);
                return rotateHorizontally(state, location);
            }

            location = VecHelper.rotateCentered(location, facing == Direction.DOWN ? 180 : 0, Direction.Axis.X);
            return location;
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            Direction facing = state.getValue(NoteLinkBlock.FACING);
            float yRot = facing.getAxis()
                    .isVertical() ? 0 : AngleHelper.horizontalAngle(facing) + 180;
            float xRot = facing == Direction.UP ? 90 : facing == Direction.DOWN ? 270 : 0;
            TransformStack.of(ms)
                    .rotateYDegrees(yRot)
                    .rotateXDegrees(xRot);
        }
    };
    public static final ValueBoxTransform PITCH_SLOT_TRANSFORM = new ValueBoxTransform() {
        // TODO: adjust to actual model, this is just a placeholder from the RedstoneLink
        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(NoteLinkBlock.FACING);
            Vec3 location = VecHelper.voxelSpace(8f, 3.01f, 6f);

            if (facing.getAxis().isHorizontal()) {
                location = VecHelper.voxelSpace(8f, 6f, 3.01f);
                return rotateHorizontally(state, location);
            }

            location = VecHelper.rotateCentered(location, facing == Direction.DOWN ? 180 : 0, Direction.Axis.X);
            return location;
        }

        @Override
        public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
            Direction facing = state.getValue(NoteLinkBlock.FACING);
            float yRot = facing.getAxis()
                    .isVertical() ? 0 : AngleHelper.horizontalAngle(facing) + 180;
            float xRot = facing == Direction.UP ? 90 : facing == Direction.DOWN ? 270 : 0;
            TransformStack.of(ms)
                    .rotateYDegrees(yRot)
                    .rotateXDegrees(xRot);
        }
    };

    public NoteLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private boolean receivedSignalChanged;
    private int receivedSignal;
    private int transmittedSignal;
    private boolean transmitter;

    private ItemStack key = ItemStack.EMPTY;
    private PipePitch pitch = PipePitch.DEFAULT;

    private NoteLinkBehaviour link;
    private ItemValueBoxBehaviour keySlot;
    private PipePitchScrollValueBehaviour pitchSlot;

    @Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.putBoolean("Transmitter", transmitter);
		compound.putInt("Receive", receivedSignal);
		compound.putBoolean("ReceivedChanged", receivedSignalChanged);
		compound.putInt("Transmit", transmittedSignal);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		transmitter = compound.getBoolean("Transmitter");
		super.read(compound, clientPacket);

		receivedSignal = compound.getInt("Receive");
		receivedSignalChanged = compound.getBoolean("ReceivedChanged");
		if (level == null || level.isClientSide || !link.hasNewPos())
			transmittedSignal = compound.getInt("Transmit");
	}

    public int getTransmittedSignal() {
        return transmittedSignal;
    }

    public void setReceivedSignal(int power) {
        if (receivedSignal != power)
            receivedSignalChanged = true;
        receivedSignal = power;
    }

    public void transmit(int strength) {
        transmittedSignal = strength;
        if (link != null)
            link.notifySignalChange();
    }

    @Override
    public void tick() {
        super.tick();

        if (isTransmitterBlock() != transmitter) {
            transmitter = isTransmitterBlock();
            if (link != null) {
                NoteLinkBehaviour prev = link;
                removeBehaviour(NoteLinkBehaviour.TYPE);
                link = prev.withCycledMode();
            } else {
                link = new NoteLinkBehaviour(this,
                        this::getTransmittedSignal,
                        this::setReceivedSignal,
                        transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE
                ).withOnLoadedCallback(this::onNoteLinkBehaviorLoaded);
                PipeOrgans.LOGGER.warn("NoteLinkBlockEntity.tick: link behaviour was null when updating mode, recreated at {} (shouldn't happen)", worldPosition);
            }
            attachBehaviourLate(link);
        }

        if (transmitter) return;
        if (level == null || level.isClientSide) return;

        BlockState bs = getBlockState();
        if (!AllBlocks.NOTE_LINK.has(bs)) return;

        if ((receivedSignal > 0) != bs.getValue(NoteLinkBlock.POWERED)) {
            receivedSignalChanged = true;
            level.setBlockAndUpdate(worldPosition, bs.cycle(NoteLinkBlock.POWERED));
        }

        if (receivedSignalChanged) {
            updateSelfAndAttached(bs);
        }
    }

    /**
     * Block destroyed or picked up by a contraption. Usually detaches kinetics
     */
    @Override
    public void remove() {
        super.remove();

        updateSelfAndAttached(getBlockState());
    }

    public void updateSelfAndAttached(BlockState blockState) {
        if (level == null)
            return; // safety check, also so IDEA stops complaining
        Direction attachedFace = blockState.getValue(NoteLinkBlock.FACING)
                .getOpposite();
        BlockPos attachedPos = worldPosition.relative(attachedFace);
        level.blockUpdated(worldPosition, level.getBlockState(worldPosition)
                .getBlock());
        level.blockUpdated(attachedPos, level.getBlockState(attachedPos)
                .getBlock());
        receivedSignalChanged = false;
    }

    protected Boolean isTransmitterBlock() {
        return !getBlockState().getValue(NoteLinkBlock.RECEIVER);
    }

    public int getReceivedSignal() {
        return receivedSignal;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        ItemValueBoxBehaviour.ItemValueBoxGroup keySlotGroup = new ItemValueBoxBehaviour.ItemValueBoxGroup(
                Set.of(KEY_SLOT_TRANSFORM),
                (held, player) -> {
                    setKey(held);
                    return InteractionResult.SUCCESS;
                },
                this::getKey,
                Component.translatable("block.pipeorgans.note_link.key_slot.label"),
                List.of()
        );

        behaviours.add(new ClipboardAssistedPlacementBehaviour(this));
        behaviours.add(keySlot = new ItemValueBoxBehaviour(this, List.of(keySlotGroup)));
        behaviours.add(pitchSlot = new PipePitchScrollValueBehaviour(this, PITCH_SLOT_TRANSFORM)
                .withPipePitchCallback(this::setPitch)
        );


    }
    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(link = new NoteLinkBehaviour(this,
                this::getTransmittedSignal,
                this::setReceivedSignal,
                transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE)
                .withOnLoadedCallback(this::onNoteLinkBehaviorLoaded)
        );
    }

    public void reset() {
        transmittedSignal = 0;
        receivedSignal = 0;
        receivedSignalChanged = false;
        link.changeKeyFrequency(ItemStack.EMPTY);
        link.changePitch(PipePitch.DEFAULT);
    }

    public ItemStack getKey() {
        return key;
    }

    public void setKey(ItemStack key) {
        this.key = key;
        key.setCount(1);
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.setKey: key set to {}", key);
        link.changeKeyFrequency(key);
    }

    public PipePitch getPitch() {
        return pitch;
    }

    public void setPitch(PipePitch pitch) {
        this.pitch = pitch;
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.setPitch: pitch set to {}", pitch);
        link.changePitch(pitch);
    }

    protected void onNoteLinkBehaviorLoaded() {
        key = link.getKey();
        pitch = link.getPitch();
        pitchSlot.setValue(pitch);
    }

    @Override
    public void applyPlacementMutation() {
        setPitch(pitch.next());
    }
}
