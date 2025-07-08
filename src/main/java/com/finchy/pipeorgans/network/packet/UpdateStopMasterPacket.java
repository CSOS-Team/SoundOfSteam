package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class UpdateStopMasterPacket extends SimplePacketBase {

    public final int toggledChannel;
    public final BlockPos pos;

    public UpdateStopMasterPacket(int toggledChannel, BlockPos pos) {
        this.toggledChannel = toggledChannel;
        this.pos = pos;
    }

    public UpdateStopMasterPacket(FriendlyByteBuf buffer) {
        this.toggledChannel = buffer.readInt();
        this.pos = buffer.readBlockPos();
    }

    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(toggledChannel);
        buffer.writeBlockPos(pos);
    }

    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();

            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) {
                sm.toggleChannel(toggledChannel);
            }

        });
        return true;
    }
}