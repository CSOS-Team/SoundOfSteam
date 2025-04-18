package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

public class UpdateStopMasterC2SPacket {

    public final int toggledChannel;
    public final String newMapping;
    public final BlockPos pos;

    public static UpdateStopMasterC2SPacket createPacket(int toggledChannel, String newMapping, BlockPos pos) {
        return new UpdateStopMasterC2SPacket(toggledChannel, newMapping, pos);
    }

    protected UpdateStopMasterC2SPacket(int toggledChannel, String newMapping, BlockPos pos) {
        this.toggledChannel = toggledChannel;
        this.newMapping = newMapping;
        this.pos = pos;
    }

    public static UpdateStopMasterC2SPacket decodePacket(FriendlyByteBuf buf) {
        try {
            int channels = buf.readInt();

            int length = buf.readInt(); // get length of bytes
            byte[] receivedBytes = new byte[length]; // make a new byte
            buf.readBytes(receivedBytes); // put data into byte array
            String mapping = new String(receivedBytes, StandardCharsets.UTF_8);
            PipeOrgans.LOGGER.info(mapping);

            BlockPos pos = buf.readBlockPos();

            return new UpdateStopMasterC2SPacket(channels, mapping, pos);
        } catch (IndexOutOfBoundsException e) {
            PipeOrgans.LOGGER.error("MidiMessagePacket did not contain enough bytes: {}", String.valueOf(e));
            return null;
        }
    }

    public static void encodePacket(UpdateStopMasterC2SPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.toggledChannel);

        byte[] mappingArray = packet.newMapping.getBytes(StandardCharsets.UTF_8); // get bytes of string
        buf.writeInt(mappingArray.length); // write length of bytes
        buf.writeBytes(mappingArray); // write bytes

        buf.writeBlockPos(packet.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();

            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) {
                sm.toggleChannel(toggledChannel);
                sm.setMapping(newMapping);
            }

        });
    }
}