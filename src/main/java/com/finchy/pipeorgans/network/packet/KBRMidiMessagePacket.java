package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.network.AllPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import javax.sound.midi.*;

public record KBRMidiMessagePacket(int code, int length, byte[] data) implements ServerboundPacketPayload {

    public static final StreamCodec<ByteBuf, KBRMidiMessagePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, KBRMidiMessagePacket::code,
            ByteBufCodecs.VAR_INT, KBRMidiMessagePacket::length,
            CatnipStreamCodecBuilders.nullable(ByteBufCodecs.byteArray(Integer.MAX_VALUE)), KBRMidiMessagePacket::data,
            KBRMidiMessagePacket::new
    );

    // unfortunately, the constructor for ShortMessage that takes a byte[] is protected, so this exposes it
    public static class MidiShortMessage extends ShortMessage {
        public MidiShortMessage(byte[] data, int length) throws InvalidMidiDataException {
            super();
            super.setMessage(data, length);
        }
    }

    public final static int SHORT_CODE = 0;
    public final static int SYSEX_CODE = 1;
    public final static int META_CODE = 2;
    public final static int OTHER_CODE = -1;

    @Override
    public void handle(ServerPlayer player) {
        MidiMessage message;
        try {
            if (this.code == SHORT_CODE) { // if it's a ShortMessage
                message = new MidiShortMessage(data, length);
            } else if (code == SYSEX_CODE) { // if it's a SysexMessage
                message = new SysexMessage(data, length);

            } else { // something else; fallback to ShortMessage
                // any MetaMessages become system reset (0xFF) ShortMessages
                message = new MidiShortMessage(data, length);
            }
        } catch (InvalidMidiDataException e) {
            throw new RuntimeException(e);
        }
        
        ServerLevel level = (ServerLevel) player.level();

        BlockPos pos = KeyboardRelayBlockEntity.playerUsingKBRPos(player); // get pos of KBR being used
        if (pos != null) { // if player is actually using a KBR

            if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity kbr // if there is actually a KBR at that pos
                    && kbr.isUsedBy(player)) { // and that player is using THAT KBR
                kbr.handleMidiMessage(message); // send midi data to KBR
            }
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.MIDI_MESSAGE;
    }
}