package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.rollPuncher.RollPuncherMenu;
import com.finchy.pipeorgans.network.AllPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public record MidiUploadPacket(int code, long size, String midi, byte[] data) implements ServerboundPacketPayload {

    public static final int BEGIN = 0;
    public static final int WRITE = 1;
    public static final int FINISH = 2;

    public static final StreamCodec<ByteBuf, MidiUploadPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, MidiUploadPacket::code,
            ByteBufCodecs.VAR_LONG, MidiUploadPacket::size,
            CatnipStreamCodecBuilders.nullable(ByteBufCodecs.stringUtf8(256)), MidiUploadPacket::midi,
            CatnipStreamCodecBuilders.nullable(ByteBufCodecs.byteArray(Integer.MAX_VALUE)), MidiUploadPacket::data,
            MidiUploadPacket::new
    );

    public static MidiUploadPacket begin(String midi, long size) {
        return new MidiUploadPacket(BEGIN, size, midi, null);
    }

    public static MidiUploadPacket write(String midi, byte[] data) {
        return new MidiUploadPacket(WRITE, 0, midi, data);
    }

    public static MidiUploadPacket finish(String midi) {
        return new MidiUploadPacket(FINISH, 0, midi, null);
    }

    @Override
    public void handle(ServerPlayer player) {
            if (code == BEGIN) {
                BlockPos pos = ((RollPuncherMenu) player.containerMenu).contentHolder
                        .getBlockPos();
                PipeOrgans.MIDI_RECEIVER.handleNewUpload(player, midi, size, pos);
            }
            if (code == WRITE)
                PipeOrgans.MIDI_RECEIVER.handleWriteRequest(player, midi, data);
            if (code == FINISH)
                PipeOrgans.MIDI_RECEIVER.handleFinishedUpload(player, midi);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.MIDI_UPLOAD;
    }
}
