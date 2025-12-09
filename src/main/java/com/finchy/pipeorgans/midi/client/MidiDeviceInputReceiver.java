package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.network.packet.KBRMidiMessagePacket;

import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

import static com.finchy.pipeorgans.network.packet.KBRMidiMessagePacket.*;

public class MidiDeviceInputReceiver implements Receiver {

    private volatile boolean open = true;

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (open) handleMessage(message);
    }

    @Override
    public void close() {
        open = false;
    }

    protected void handleMessage(MidiMessage message) {
        Player player = Minecraft.getInstance().player;

        if (player != null && PipeOrgans.getProxy().isClient()) { // only run on client
            sendNotePacket(message);
        }
    }

    public void sendNotePacket(MidiMessage message) {
        int code = OTHER_CODE;
        if (message instanceof ShortMessage) {
            code = SHORT_CODE;
        } else if (message instanceof SysexMessage) {
            code = SYSEX_CODE;
        } /* else if (message instanceof MetaMessage) {
            code = META_CODE;
         */
        byte[] data = message.getMessage();
        CatnipServices.NETWORK.sendToServer(new KBRMidiMessagePacket(code, data.length, data));
    }

}
