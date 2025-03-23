package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.network.TestPacketHandler;
import com.finchy.pipeorgans.midi.network.packet.MidiMessageC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

public class MidiDeviceInputReceiver implements Receiver {

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
        Player player = Minecraft.getInstance().player;

        if (player != null && PipeOrgans.getProxy().isClient()) { //only run on client
            player.sendSystemMessage(Component.literal("RECEIVED"));
        }
    }

    public void handleMidiNoteOn(Byte channel, Byte note, Byte velocity, Player player) {
        MidiMessageC2SPacket packet = MidiMessageC2SPacket.createNotePacket(channel, note, velocity, player.getUUID(), player.getOnPos());
        TestPacketHandler.sendToServer(packet);
    }

}
