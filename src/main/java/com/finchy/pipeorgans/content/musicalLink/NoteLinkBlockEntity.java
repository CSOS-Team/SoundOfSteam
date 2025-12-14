package com.finchy.pipeorgans.content.musicalLink;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class NoteLinkBlockEntity extends SmartBlockEntity implements MenuProvider {

    public NoteLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private boolean receivedSignalChanged;
    private int receivedSignal;
    private int transmittedSignal;
    private boolean transmitter;

    private ItemStack key = ItemStack.EMPTY;
    private PipePitch pitch = PipePitch.INVALID;

    private NoteLinkBehaviour link;

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
		if (level == null || level.isClientSide || link.hasNewPos())
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
        link.notifySignalChange();
    }

    @Override
    public void tick() {
        super.tick();

        if (level == null || level.isClientSide) return;

        BlockState bs = getBlockState();
        if (!AllBlocks.NOTE_LINK.has(bs)) return;

        if (isTransmitterBlock() != transmitter) {
            transmitter = isTransmitterBlock();
            link.changeMode(transmitter ? NoteLinkBehaviour.Mode.TRANSMIT : NoteLinkBehaviour.Mode.RECEIVE);
            updateSelfAndAttached(bs);
        }

        if (transmitter) return;
        if (level.isClientSide) return;

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
        behaviours.add(link = new NoteLinkBehaviour(this,
                        this::getTransmittedSignal,
                        this::setReceivedSignal));
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.note_link");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return NoteLinkMenu.create(pContainerId, pPlayerInventory, this);
    }

    public void reset() {
        transmittedSignal = 0;
        receivedSignal = 0;
        receivedSignalChanged = false;
        link.changeKeyFrequency(ItemStack.EMPTY);
        link.changePitch(PipePitch.INVALID);
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
}
