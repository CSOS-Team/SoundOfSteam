package com.finchy.pipeorgans.content.midi.stopMaster;

import com.simibubi.create.Create;
import com.simibubi.create.content.redstone.link.IRedstoneLinkable;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler;
import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

public class StopMasterLinkBehaviour extends BlockEntityBehaviour implements IRedstoneLinkable {

	public static final BehaviourType<StopMasterLinkBehaviour> TYPE = new BehaviourType<>();

	Frequency frequencyFirst;
	Frequency frequencyLast;
	ValueBoxTransform firstSlot;
	ValueBoxTransform secondSlot;
	Vec3 textShift;

	public boolean newPosition;
	private IntSupplier transmission;
	private IntConsumer signalCallback;

	protected StopMasterLinkBehaviour(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots) {
		super(be);
		frequencyFirst = Frequency.EMPTY;
		frequencyLast = Frequency.EMPTY;
		firstSlot = slots.getLeft();
		secondSlot = slots.getRight();
		textShift = Vec3.ZERO;
		newPosition = true;
	}

	public static StopMasterLinkBehaviour transmitter(SmartBlockEntity be, Pair<ValueBoxTransform, ValueBoxTransform> slots,
                                                      IntSupplier transmission) {
		StopMasterLinkBehaviour behaviour = new StopMasterLinkBehaviour(be, slots);
		behaviour.transmission = transmission;
		return behaviour;
	}

	public StopMasterLinkBehaviour moveText(Vec3 shift) {
		textShift = shift;
		return this;
	}

	public void copyItemsFrom(StopMasterLinkBehaviour behaviour) {
		if (behaviour == null)
			return;
		frequencyFirst = behaviour.frequencyFirst;
		frequencyLast = behaviour.frequencyLast;
	}

	@Override
	public boolean isListening() {
		return false;
	}

	@Override
	public int getTransmittedStrength() {
		return transmission.getAsInt();
	}

	@Override
	public void setReceivedStrength(int networkPower) {
		if (!newPosition)
			return;
		signalCallback.accept(networkPower);
	}

	public void notifySignalChange() {
		Create.REDSTONE_LINK_NETWORK_HANDLER.updateNetworkOf(getWorld(), this);
	}

	@Override
	public void initialize() {
		super.initialize();
		if (getWorld().isClientSide)
			return;
		getHandler().addToNetwork(getWorld(), this);
		newPosition = true;
	}

	@Override
	public Couple<Frequency> getNetworkKey() {
		return Couple.create(frequencyFirst, frequencyLast);
	}

	@Override
	public void unload() {
		super.unload();
		if (getWorld().isClientSide)
			return;
		getHandler().removeFromNetwork(getWorld(), this);
	}

	@Override
	public boolean isSafeNBT() {
		return true;
	}

	@Override
	public void write(CompoundTag nbt, boolean clientPacket) {
		super.write(nbt, clientPacket);
		nbt.put("FrequencyFirst", frequencyFirst.getStack()
			.save(new CompoundTag()));
		nbt.put("FrequencyLast", frequencyLast.getStack()
			.save(new CompoundTag()));
		nbt.putLong("LastKnownPosition", blockEntity.getBlockPos()
			.asLong());
	}

	@Override
	public void read(CompoundTag nbt, boolean clientPacket) {
		long positionInTag = blockEntity.getBlockPos()
			.asLong();
		long positionKey = nbt.getLong("LastKnownPosition");
		newPosition = positionInTag != positionKey;

		super.read(nbt, clientPacket);
		frequencyFirst = Frequency.of(ItemStack.of(nbt.getCompound("FrequencyFirst")));
		frequencyLast = Frequency.of(ItemStack.of(nbt.getCompound("FrequencyLast")));
	}

	public void transmitOnFrequency(ItemStack a, ItemStack b, boolean off) {
		a = a.copy();
		b = b.copy();



	}

	public void addnet() {
		getHandler().addToNetwork(getWorld(), this);
	}

	public void removenet() {
		getHandler().removeFromNetwork(getWorld(), this);
	}

	public void setFrequencyWorse(ItemStack a, ItemStack b, boolean on) {
		a = a.copy();
		b = b.copy();

		frequencyFirst = Frequency.of(a);
		frequencyLast = Frequency.of(b);

		if (on) {
			getHandler().addToNetwork(getWorld(), this);
		} else {
			getHandler().removeFromNetwork(getWorld(), this);
		}
	}

	public void setFrequency(boolean first, ItemStack stack) {
		stack = stack.copy();
		stack.setCount(1);
		ItemStack toCompare = first ? frequencyFirst.getStack() : frequencyLast.getStack();
		boolean changed = !ItemStack.isSameItemSameTags(stack, toCompare);

		if (changed)
			getHandler().removeFromNetwork(getWorld(), this);

		if (first)
			frequencyFirst = Frequency.of(stack);
		else
			frequencyLast = Frequency.of(stack);

		if (!changed)
			return;

		blockEntity.sendData();
		getHandler().addToNetwork(getWorld(), this);
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

	private RedstoneLinkNetworkHandler getHandler() {
		return Create.REDSTONE_LINK_NETWORK_HANDLER;
	}

	public static class SlotPositioning {
		Function<BlockState, Pair<Vec3, Vec3>> offsets;
		Function<BlockState, Vec3> rotation;
		float scale;

		public SlotPositioning(Function<BlockState, Pair<Vec3, Vec3>> offsetsForState,
			Function<BlockState, Vec3> rotationForState) {
			offsets = offsetsForState;
			rotation = rotationForState;
			scale = 1;
		}

		public SlotPositioning scale(float scale) {
			this.scale = scale;
			return this;
		}

	}

	public boolean testHit(Boolean first, Vec3 hit) {
		BlockState state = blockEntity.getBlockState();
		Vec3 localHit = hit.subtract(Vec3.atLowerCornerOf(blockEntity.getBlockPos()));
		return (first ? firstSlot : secondSlot).testHit(getWorld(), getPos(), state, localHit);
	}

	@Override
	public boolean isAlive() {
		Level level = getWorld();
		BlockPos pos = getPos();
		if (blockEntity.isChunkUnloaded())
			return false;
		if (blockEntity.isRemoved())
			return false;
		if (!level.isLoaded(pos))
			return false;
		return level.getBlockEntity(pos) == blockEntity;
	}

	@Override
	public BlockPos getLocation() {
		return getPos();
	}

}
