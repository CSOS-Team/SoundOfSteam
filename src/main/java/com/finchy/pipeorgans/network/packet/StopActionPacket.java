package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.stop.Stop;
import com.finchy.pipeorgans.content.stop.StopBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

// Client -> server actions for the Stop block GUI
public class StopActionPacket extends SimplePacketBase {

    public static final int ADD = 0;
    public static final int TOGGLE = 1;
    public static final int OPEN_EDIT = 2;
    public static final int SAVE = 3;
    public static final int DELETE = 4;
    public static final int OPEN_MAIN = 5;

    private final BlockPos pos;
    private final int action;
    private final int index;
    private final String name;
    private final String descriptor;
    private final ItemStack filter;

    public StopActionPacket(BlockPos pos, int action, int index, String name, String descriptor, ItemStack filter) {
        this.pos = pos;
        this.action = action;
        this.index = index;
        this.name = name == null ? "" : name;
        this.descriptor = descriptor == null ? "" : descriptor;
        this.filter = filter == null ? ItemStack.EMPTY : filter;
    }

    public static StopActionPacket simple(BlockPos pos, int action, int index) {
        return new StopActionPacket(pos, action, index, "", "", ItemStack.EMPTY);
    }

    public StopActionPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.action = buffer.readVarInt();
        this.index = buffer.readVarInt();
        this.name = buffer.readUtf(Stop.MAX_NAME_LENGTH * 4);
        this.descriptor = buffer.readUtf(Stop.MAX_DESCRIPTOR_LENGTH * 4);
        this.filter = buffer.readItem();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(action);
        buffer.writeVarInt(index);
        buffer.writeUtf(name, Stop.MAX_NAME_LENGTH * 4);
        buffer.writeUtf(descriptor, Stop.MAX_DESCRIPTOR_LENGTH * 4);
        buffer.writeItem(filter);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;
            ServerLevel level = player.serverLevel();
            if (!level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof StopBlockEntity be))
                return;
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64)
                return;

            switch (action) {
                case ADD -> be.addStop();
                case TOGGLE -> be.togglePress(index);
                case OPEN_EDIT -> be.openEditMenu(player, index);
                case SAVE -> {
                    String trimmedName = name.length() > Stop.MAX_NAME_LENGTH ? name.substring(0, Stop.MAX_NAME_LENGTH) : name;
                    String trimmedDesc = descriptor.length() > Stop.MAX_DESCRIPTOR_LENGTH
                            ? descriptor.substring(0, Stop.MAX_DESCRIPTOR_LENGTH) : descriptor;
                    be.editStop(index, trimmedName, trimmedDesc, filter);
                    be.openMainMenu(player);
                }
                case DELETE -> {
                    be.deleteStop(index);
                    be.openMainMenu(player);
                }
                case OPEN_MAIN -> be.openMainMenu(player);
            }
        });
        return true;
    }
}
