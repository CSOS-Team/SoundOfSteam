package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.network.PacketHandler;
import com.finchy.pipeorgans.midi.network.packet.MidiMessageC2SPacket;
import com.finchy.pipeorgans.util.midi.MidiUtils;

import net.minecraft.client.Minecraft;
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

        if (player != null && PipeOrgans.getProxy().isClient()) { // only run on client
            if (MidiUtils.isNoteOn(sm)) { // if message is note on
                transmitNotePacket( // send packet with channel, note, velocity, player
                        Integer.valueOf(sm.getChannel()).byteValue(),
                        sm.getMessage()[1],
                        sm.getMessage()[2],
                        player
                );
            } else if (MidiUtils.isNoteOff(sm)) { // if message is note off
                transmitNotePacket( // send packet with channel, note, 0 velocity, player
                        Integer.valueOf(sm.getChannel()).byteValue(),
                        sm.getMessage()[1],
                        Integer.valueOf(0).byteValue(),
                        player
                );
            }
        }
    }

    public void transmitNotePacket(Byte channel, Byte note, Byte velocity, Player player) {
        MidiMessageC2SPacket packet = MidiMessageC2SPacket.createNotePacket(channel, note, velocity, player.getUUID(), player.getOnPos());
        PacketHandler.sendToServer(packet);
    }

}
