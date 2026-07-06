package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import javax.sound.midi.*;

public class KBRMidiMessagePacket extends KBRPacketBase {

    public final MidiMessage message;

    private final static int SHORT_CODE = 0;
    private final static int SYSEX_CODE = 1;
    private final static int META_CODE = 2;
    private final static int OTHER_CODE = -1;

    public KBRMidiMessagePacket(MidiMessage message, BlockPos KBRPos) {
        super(KBRPos);
        this.message = message;
    }

    // the constructor for ShortMessage that takes a byte[] is protected, so this exposes it
    public static class MidiShortMessage extends ShortMessage {
        public MidiShortMessage(byte[] data, int length) throws InvalidMidiDataException {
            super();
            super.setMessage(data, length);
        }
    }

    public KBRMidiMessagePacket(FriendlyByteBuf buffer) {
        super(buffer);
        int code = buffer.readVarInt();
        int length = buffer.readVarInt();
        byte[] data = buffer.readByteArray(length);
        try {
            if (code == SHORT_CODE) { // if it's a ShortMessage
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

    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        super.write(buffer);
        int code = OTHER_CODE;
        if (message instanceof ShortMessage) {
            code = SHORT_CODE;
        } else if (message instanceof SysexMessage) {
            code = SYSEX_CODE;
        } /* else if (message instanceof MetaMessage) {
            code = META_CODE;
         */
        byte[] data = message.getMessage();
        buffer.writeVarInt(code);
        buffer.writeVarInt(data.length);
        buffer.writeByteArray(data);
    }

    @Override
    protected void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr) {
        kbr.handleMidiMessage(message);
    }
}