package com.finchy.pipeorgans.content.noteLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.infrastructure.itemValueBox.ItemValueBoxBehaviour;
import com.finchy.pipeorgans.infrastructure.pipePitchScrollValue.PipePitchScrollValueBehaviour;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.util.PipePitch;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class NoteLinkBlockEntity extends SmartBlockEntity implements NoteLinkBehaviourSubscriber {

    protected static final float SLOT_OUTWARD_OFFSET = 2.5f;

    public static final ValueBoxTransform KEY_SLOT_TRANSFORM = new ValueBoxTransform() {
        @Override
        public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
            Direction facing = state.getValue(NoteLinkBlock.FACING);
            Vec3 location = VecHelper.voxelSpace(8f, SLOT_OUTWARD_OFFSET, 11f);

            if (facing.getAxis().isHorizontal()) {
                location = VecHelper.voxelSpace(8f, 11f, SLOT_OUTWARD_OFFSET);
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
            Vec3 location = VecHelper.voxelSpace(8f, SLOT_OUTWARD_OFFSET, 6f);

            if (facing.getAxis().isHorizontal()) {
                location = VecHelper.voxelSpace(8f, 6f, SLOT_OUTWARD_OFFSET);
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

    private boolean receivedSignalChanged;
    private int receivedSignal;
    private int transmittedSignal;
    private boolean transmitter;

    private ItemStack key = ItemStack.EMPTY;
    private PipePitch pitch = PipePitch.DEFAULT;

    private NoteLinkBehaviour link;
    private ItemValueBoxBehaviour keySlot;
    private PipePitchScrollValueBehaviour pitchSlot;

    public NoteLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        ItemValueBoxBehaviour.ItemValueBoxGroup keySlotGroup = new ItemValueBoxBehaviour.ItemValueBoxGroup(
                Set.of(KEY_SLOT_TRANSFORM),
                (held, player) -> {
                    setKey(held);
                    updateHeldClipboard(player);
                    return InteractionResult.SUCCESS;
                },
                this::getKey,
                Component.translatable("block.pipeorgans.note_link.key_slot.label"),
                List.of()
        );

        behaviours.add(keySlot = new ItemValueBoxBehaviour(this, List.of(keySlotGroup)));
        behaviours.add(pitchSlot = new PipePitchScrollValueBehaviour(this, PITCH_SLOT_TRANSFORM, Component.translatable("block.pipeorgans.note_link.pitch_slot.label"))
                .withPipePitchCallback(this::setPitch)
        );
    }
    @Override
    public void addBehavioursDeferred(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(link = new NoteLinkBehaviour(this,
                this::getTransmittedSignal,
                this::setReceivedSignal,
                transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE,
                key, pitch)
                .withOnLoadedCallback(this::onNoteLinkBehaviorLoaded)
        );
    }

    @Override
	public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
		compound.putBoolean("Transmitter", transmitter);
		compound.putInt("Receive", receivedSignal);
		compound.putBoolean("ReceivedChanged", receivedSignalChanged);
		compound.putInt("Transmit", transmittedSignal);
        PipeOrgans.LOGGER.debug("Stored NoteLink ({}): '{}'", getBlockPos(), compound.getAsString());
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		transmitter = compound.getBoolean("Transmitter");
		super.read(compound, clientPacket);

		receivedSignal = compound.getInt("Receive");
		receivedSignalChanged = compound.getBoolean("ReceivedChanged");
		if (level == null || level.isClientSide || !link.hasNewPos())
			transmittedSignal = compound.getInt("Transmit");

        PipeOrgans.LOGGER.debug("Loaded NoteLink ({}): '{}'", getBlockPos(), compound.getAsString());
	}

    @Nullable
    public ItemStack getKey() {
        if (key == null && link != null) {
            key = link.getKey();
        }
        return key;
    }

    public void setKey(ItemStack key) {
        key.setCount(1);
        this.key = key;
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.setKey: key set to {}", key);
        if (link != null)
            link.changeKeyFrequency(key);
    }

    @Nullable
    public PipePitch getPitch() {
        if (pitch == null && link != null) {
            pitch = link.getPitch();
        }
        return pitch;
    }

    public void setPitch(PipePitch pitch) {
        this.pitch = pitch;
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.setPitch: pitch set to {}", pitch);
        if (link != null)
            link.changePitch(pitch);
    }

    public void onNoteLinkBehaviorLoaded() {
        key = link.getKey();
        pitch = link.getPitch();
        pitchSlot.setValueSilent(pitch);
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.onNoteLinkBehaviorLoaded: synced key and pitch from NoteLinkBehaviour at {}, now key={}, pitch={}", worldPosition, key, pitch.getNormalizedName());
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
                        transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE,
                        key, pitch
                ).withOnLoadedCallback(this::onNoteLinkBehaviorLoaded);
                PipeOrgans.LOGGER.warn("NoteLinkBlockEntity.tick: NoteLinkBehaviour link was null when updating mode, recreated at {} (shouldn't happen)", worldPosition);
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



    public void applyClipboardSettings(CompoundTag clipboardTag) {

        PipePitch next = PipePitch.fromNormalizedName(clipboardTag.getString("Pitch")).next(); // get the pitch above what's written on the clipboard
        if (next == null) next = PipePitch.HIGHEST; // if it's the maximum pitch, just stay at the maximum
        setPitch(next); // set the new pitch
        pitchSlot.setValueSilent(next); // set the new pitch on the scroll box
        setKey(Objects.requireNonNull(ItemStack.of(clipboardTag.getCompound("Key")))); // set the new key

        notifyUpdate();

        removeBehaviour(NoteLinkBehaviour.TYPE);
        attachBehaviourLate(link = new NoteLinkBehaviour(this,
                this::getTransmittedSignal,
                this::setReceivedSignal,
                transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE,
                key, pitch
        ).withOnLoadedCallback(this::onNoteLinkBehaviorLoaded));
        notifyUpdate();

        updateSelfAndAttached(getBlockState());

        level.scheduleTick(getBlockPos(), getBlockState().getBlock(), 1);
    }

    public void updateHeldClipboard(Player player) {
        ItemStack mainhand = player.getMainHandItem(); // get item in mainhand
        boolean mainhandIsClipboard = mainhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem());
        ItemStack offhand = player.getOffhandItem(); // get item in offhand
        boolean offhandIsClipboard = offhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem());
        if (!mainhandIsClipboard && !offhandIsClipboard) return; // if the player isn't holding any clipboards, return

        ItemStack clipboardStack;
        if (mainhandIsClipboard) // if there's a clipboard in the mainhand, prioritise that
            clipboardStack = mainhand;
        else // otherwise use the clipboard in the offhand
            clipboardStack = offhand;

        if (clipboardStack.hasTag() &&
                clipboardStack.getTag().contains("CopiedValues") &&
                clipboardStack.getTagElement("CopiedValues").contains("MusicalFrequency")) { // if the clipboard has anything specifically about note links in its NBT

            CompoundTag clipboardTag = clipboardStack.getTagElement("CopiedValues").getCompound("MusicalFrequency");
            clipboardTag.putString("Pitch", pitch.getNormalizedName()); // put the new pitch in clipboard NBT
            clipboardTag.put("Key", key.serializeNBT()); // put the new key in clipboard NBT

        } else { // if the clipboard hasn't been used for note links previously
            ClipboardOverrides.switchTo(ClipboardOverrides.ClipboardType.WRITTEN, offhand); // make the clipboard visually look like it's been written in

            CompoundTag copiedTag = new CompoundTag(); // make a new tag to put in "CopiedValues"
            CompoundTag noteLinkTag = new CompoundTag(); // make a new tag to put in "MusicalFrequency"
            noteLinkTag.putString("Pitch", pitch.getNormalizedName()); // default to F#-1
            noteLinkTag.put("Key", key.serializeNBT()); // default to no key item

            copiedTag.put("MusicalFrequency", noteLinkTag);
            clipboardStack.getOrCreateTag().put("CopiedValues", copiedTag); // apply the tags to the clipboard
        }


    }

    // Block destroyed or picked up by a contraption. Usually detaches kinetics
    @Override
    public void remove() {
        super.remove();

        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.remove called at {}", worldPosition);

        updateSelfAndAttached(getBlockState());
    }

    // Block destroyed or replaced. Requires Block to call IBE::onRemove
    @Override
    public void destroy() {
        PipeOrgans.LOGGER.debug("NoteLinkBlockEntity.destroy called at {}", worldPosition);
        super.destroy();
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

    public void reset() {
        transmittedSignal = 0;
        receivedSignal = 0;
        receivedSignalChanged = false;
        link.changeKeyFrequency(ItemStack.EMPTY);
        link.changePitch(PipePitch.DEFAULT);
    }
}
