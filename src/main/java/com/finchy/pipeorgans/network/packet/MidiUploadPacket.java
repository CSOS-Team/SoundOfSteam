package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class MidiUploadPacket extends SimplePacketBase {

    public static final int BEGIN = 0;
    public static final int WRITE = 1;
    public static final int FINISH = 2;

    private int code;
    private long size;
    private String midi;
    private byte[] data;

    public MidiUploadPacket(int code, String midi) {
        this.code = code;
        this.midi = midi;
    }

    public static MidiUploadPacket begin(String midi, long size) {
        MidiUploadPacket pkt = new MidiUploadPacket(BEGIN, midi);
        pkt.size = size;
        return pkt;
    }

    public static MidiUploadPacket write(String midi, byte[] data) {
        MidiUploadPacket pkt = new MidiUploadPacket(WRITE, midi);
        pkt.data = data;
        return pkt;
    }

    public static MidiUploadPacket finish(String midi) {
        return new MidiUploadPacket(FINISH, midi);
    }

    public MidiUploadPacket(FriendlyByteBuf buffer) {
        code = buffer.readInt();
        midi = buffer.readUtf(256);

        if (code == BEGIN)
            size = buffer.readLong();
        if (code == WRITE)
            data = buffer.readByteArray();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(code);
        buffer.writeUtf(midi);

        if (code == BEGIN)
            buffer.writeLong(size);
        if (code == WRITE)
            buffer.writeByteArray(data);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;
            if (code == BEGIN) {
                //BlockPos pos = ((SchematicTableMenu) player.containerMenu).contentHolder
                //        .getBlockPos();
                PipeOrgans.MIDI_RECEIVER.handleNewUpload(player, midi, size, player.blockPosition());
                // ^ adapt this to the midi table, whenever it's added
            }
            if (code == WRITE)
                PipeOrgans.MIDI_RECEIVER.handleWriteRequest(player, midi, data);
            if (code == FINISH)
                PipeOrgans.MIDI_RECEIVER.handleFinishedUpload(player, midi);
        });
        return true;
    }
}
