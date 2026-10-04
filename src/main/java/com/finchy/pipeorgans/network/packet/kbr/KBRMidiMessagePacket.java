package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.network.AllPackets;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import javax.sound.midi.*;

public class KBRMidiMessagePacket extends KBRPacketBase {

    public static final StreamCodec<ByteBuf, MidiMessage> MIDI_MESSAGE_STREAM_CODEC = new StreamCodec<>() {

        private final static int SHORT_CODE = 0;
        private final static int SYSEX_CODE = 1;
        private final static int META_CODE = 2;
        private final static int OTHER_CODE = -1;

        @Override
        public MidiMessage decode(ByteBuf buffer) {
            int code = buffer.readInt();
            int length = buffer.readInt();
            byte[] data = buffer.readBytes(length).array();
            try {
                if (code == SHORT_CODE) { // if it's a MidiShortMessage
                    return new MidiShortMessage(data, length);
                } else if (code == SYSEX_CODE) { // if it's a SysexMessage
                    return new SysexMessage(data, length);

                } else { // something else; fallback to ShortMessage
                    // any MetaMessages become system reset (0xFF) MidiShortMessages
                    return new MidiShortMessage(data, length);
                }

            } catch (InvalidMidiDataException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void encode(ByteBuf buffer, MidiMessage message) {
            int code = OTHER_CODE;
            if (message instanceof ShortMessage) {
                code = SHORT_CODE;
            } else if (message instanceof SysexMessage) {
                code = SYSEX_CODE;
            } /* else if (message instanceof MetaMessage) {
            code = META_CODE;
         */
            byte[] data = message.getMessage();
            buffer.writeInt(code);
            buffer.writeInt(data.length);
            buffer.writeBytes(data);
        }
    };

    public static final StreamCodec<ByteBuf, KBRMidiMessagePacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, KBRMidiMessagePacket::getKBRPos,
            MIDI_MESSAGE_STREAM_CODEC, KBRMidiMessagePacket::getMessage,
            KBRMidiMessagePacket::new
    );
    
    private final MidiMessage message;

    public KBRMidiMessagePacket(BlockPos KBRPos, MidiMessage message) {
        super(KBRPos);
        this.message = message;
    }
    
    public MidiMessage getMessage() {
        return message;
    }

    // unfortunately, the constructor for ShortMessage that takes a byte[] is protected, so this exposes it
    public static class MidiShortMessage extends ShortMessage {
        public MidiShortMessage(byte[] data, int length) throws InvalidMidiDataException {
            super();
            super.setMessage(data, length);
        }
    }

    @Override
    protected void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr) {
        kbr.handleMidiMessage(message); // send midi data to KBR
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.MIDI_MESSAGE;
    }
}