package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.piston.PistonBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

// Client -> server actions for the Piston block GUI
public class PistonActionPacket extends SimplePacketBase {

    public static final int RECALL = 0;
    public static final int SET = 1;
    public static final int CLEAR_PISTON = 2;
    public static final int TUTTI = 3;
    public static final int CLEAR = 4;

    private final BlockPos pos;
    private final int action;
    private final int index;

    public PistonActionPacket(BlockPos pos, int action, int index) {
        this.pos = pos;
        this.action = action;
        this.index = index;
    }

    public PistonActionPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.action = buffer.readVarInt();
        this.index = buffer.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(action);
        buffer.writeVarInt(index);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;
            ServerLevel level = player.serverLevel();
            if (!level.isLoaded(pos) || !(level.getBlockEntity(pos) instanceof PistonBlockEntity be))
                return;
            if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) > 64)
                return;

            switch (action) {
                case RECALL -> be.recallPiston(index);
                case SET -> be.setPiston(index);
                case CLEAR_PISTON -> be.clearPiston(index);
                case TUTTI -> be.tutti();
                case CLEAR -> be.clear();
            }
        });
        return true;
    }
}
