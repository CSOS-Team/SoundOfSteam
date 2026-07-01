package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.coupler.Coupler;
import com.finchy.pipeorgans.content.coupler.CouplerBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

// Client -> server actions for the Coupler block GUI
public class CouplerActionPacket extends SimplePacketBase {

    public static final int TOGGLE = 1;
    public static final int OPEN_EDIT = 2;
    public static final int SAVE = 3;
    public static final int DELETE = 4; // clears the coupler's fields (makes it unused)
    public static final int OPEN_MAIN = 5;
    public static final int MOVE = 6;

    private final BlockPos pos;
    private final int action;
    private final int index;
    private final int toIndex;
    private final String name;
    private final ItemStack divisionA;
    private final ItemStack divisionB;

    public CouplerActionPacket(BlockPos pos, int action, int index, int toIndex, String name, ItemStack divisionA, ItemStack divisionB) {
        this.pos = pos;
        this.action = action;
        this.index = index;
        this.toIndex = toIndex;
        this.name = name == null ? "" : name;
        this.divisionA = divisionA == null ? ItemStack.EMPTY : divisionA;
        this.divisionB = divisionB == null ? ItemStack.EMPTY : divisionB;
    }

    public static CouplerActionPacket simple(BlockPos pos, int action, int index) {
        return new CouplerActionPacket(pos, action, index, -1, "", ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public static CouplerActionPacket move(BlockPos pos, int from, int to) {
        return new CouplerActionPacket(pos, MOVE, from, to, "", ItemStack.EMPTY, ItemStack.EMPTY);
    }

    public CouplerActionPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.action = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.toIndex = buffer.readVarInt();
        this.name = buffer.readUtf(Coupler.MAX_NAME_LENGTH * 4);
        this.divisionA = buffer.readItem();
        this.divisionB = buffer.readItem();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(action);
        buffer.writeVarInt(index);
        buffer.writeVarInt(toIndex);
        buffer.writeUtf(name, Coupler.MAX_NAME_LENGTH * 4);
        buffer.writeItem(divisionA);
        buffer.writeItem(divisionB);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;
            ServerLevel level = player.serverLevel();
            if (!level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof CouplerBlockEntity be))
                return;
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64)
                return;

            switch (action) {
                case TOGGLE -> be.togglePress(index);
                case OPEN_EDIT -> be.openEditMenu(player, index);
                case SAVE -> {
                    String trimmed = name.length() > Coupler.MAX_NAME_LENGTH ? name.substring(0, Coupler.MAX_NAME_LENGTH) : name;
                    be.editCoupler(index, trimmed, divisionA, divisionB);
                    be.openMainMenu(player);
                }
                case DELETE -> {
                    be.clearCoupler(index);
                    be.openMainMenu(player);
                }
                case OPEN_MAIN -> be.openMainMenu(player);
                case MOVE -> {
                    java.util.Collections.swap(be.getCouplers(), index, toIndex);
                    be.setChanged();
                }
            }
        });
        return true;
    }
}
