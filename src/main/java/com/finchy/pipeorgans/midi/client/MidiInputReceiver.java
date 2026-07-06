package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.kbr.KBRMidiMessagePacket;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiInputReceiver implements Receiver {

    private volatile boolean open = true;

    @Override
    public void send(MidiMessage message, long timeStamp) {
        if (open && message instanceof ShortMessage sm) {
            handleMessage(sm);
        }
    }

    @Override
    public void close() {
        open = false;
    }

    protected void handleMessage(ShortMessage sm) {
        ClientMidiHandler.handleMessage(sm);
    }

}
