package com.finchy.pipeorgans.content.musicalLink;

import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class MusicalLinkBlockEntity extends SmartBlockEntity {

    public MusicalLinkBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private boolean receivedSignalChanged;
    private int receivedSignal;
    private int transmittedSignal;
    private boolean transmitter;

    private MusicalLinkBehaviour link;

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
        if (!AllBlocks.MUSICAL_LINK.has(bs)) return;

        if (isTransmitterBlock() != transmitter) {
            transmitter = isTransmitterBlock();
            link.changeMode(transmitter ? MusicalLinkBehaviour.Mode.TRANSMIT : MusicalLinkBehaviour.Mode.RECEIVE);
            updateSelfAndAttached(bs);
        }

        if (transmitter) return;
        if (level.isClientSide) return;

        if ((receivedSignal > 0) != bs.getValue(MusicalLinkBlock.POWERED)) {
            receivedSignalChanged = true;
            level.setBlockAndUpdate(worldPosition, bs.cycle(MusicalLinkBlock.POWERED));
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
        Direction attachedFace = blockState.getValue(MusicalLinkBlock.FACING)
                .getOpposite();
        BlockPos attachedPos = worldPosition.relative(attachedFace);
        level.blockUpdated(worldPosition, level.getBlockState(worldPosition)
                .getBlock());
        level.blockUpdated(attachedPos, level.getBlockState(attachedPos)
                .getBlock());
        receivedSignalChanged = false;
    }

    protected Boolean isTransmitterBlock() {
        return !getBlockState().getValue(MusicalLinkBlock.RECEIVER);
    }

    public int getReceivedSignal() {
        return receivedSignal;
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(link = new MusicalLinkBehaviour(this,
                        this::getTransmittedSignal,
                        this::setReceivedSignal));
    }
}
