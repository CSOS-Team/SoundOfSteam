package com.finchy.pipeorgans.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.midiblocks.StopMasterBlockEntity;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StopMasterDevice implements MidiDevice {

    private boolean isOpen = false;
    private final StopMasterReceiver receiver = new StopMasterReceiver();
    private final List<Receiver> receivers;

    private List<Integer> channels;

    private final String uuid;
    private final StopMasterBlockEntity be;

    private final Info DEVICE_INFO;

    public StopMasterDevice(String uuid, StopMasterBlockEntity be) {
        this.uuid = uuid;
        DEVICE_INFO = new Info(
                "Organ Stop Master", "Finchy",
                uuid, "1.0"
        ) {};
        receivers = new ArrayList<>();
        receivers.add(receiver);
        channels = new ArrayList<>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15)); // default all channels on
        this.be = be;
    }

    public void connectToMidiSource(MidiSourceDevice source) {
        try {
            source.open();
            open();
            Transmitter transmitter = source.getTransmitter();
            transmitter.setReceiver(receiver);
            PipeOrgans.LOGGER.info("{} {} successfully connected to {} {}", getDeviceInfo().getName(), uuid, source.getDeviceInfo().getName(), source.getUuid());

        } catch (MidiUnavailableException e) {
            PipeOrgans.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public static int processMidiEvent(MidiEvent event) {
        MidiMessage message = event.getMessage();
        return processMidiPitch(message);
    }

    public static int processMidiPitch(MidiMessage message) {
        int pitch = -1;
        if (message instanceof ShortMessage sm) {
            int command = sm.getCommand();
            if (command == ShortMessage.NOTE_ON || command == ShortMessage.NOTE_OFF) {
                pitch = sm.getData1();
                
            }
        }
        return pitch;
    }

    public void toggleChannel(int channel) {
        if (channels.contains(channel)) {
            channels.remove(channel);
        } else {
            channels.add(channel);
        }
    }

    public void setChannels(List<Integer> newChannels) {
        channels = newChannels;
    }

    public String getUuid() { // probably completely unnecessary, used for testing
        return uuid;
    }

    @Override
    public Info getDeviceInfo() {
        return DEVICE_INFO;
    }

    @Override
    public void open() throws MidiUnavailableException {
        if (!isOpen) {
            isOpen = true;
            PipeOrgans.LOGGER.info("Stop master {} opened", uuid);
        } else { throw(new MidiUnavailableException()); }
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            PipeOrgans.LOGGER.info("Stop master {} closed", uuid);
        }
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public long getMicrosecondPosition() {
        return -1;
    }

    @Override
    public int getMaxReceivers() {
        return 1;
    }

    @Override
    public int getMaxTransmitters() {
        return 0;
    }

    @Override
    public Receiver getReceiver() throws MidiUnavailableException {
        return receiver;
    }

    @Override
    public List<Receiver> getReceivers() {
        return receivers;
    }

    @Override
    public Transmitter getTransmitter() throws MidiUnavailableException {
        return null;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return List.of();
    }

    private class StopMasterReceiver implements Receiver {
        Receiver sr;
        private StopMasterReceiver() {
            try {
                Synthesizer synth = MidiSystem.getSynthesizer();
                synth.open();
                sr = synth.getReceiver();
            } catch (MidiUnavailableException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public void send(MidiMessage message, long timeStamp) {
            if (message instanceof ShortMessage sm) {
                int pitch = processMidiPitch(sm);
                if (pitch != -1) {
                    boolean status = sm.getStatus() == ShortMessage.NOTE_ON;
                    if (channels.contains(sm.getChannel())) {
                        PipeOrgans.LOGGER.info("SMDevice: Received {} {}", pitch, status);
                        be.sendPitchState(pitch, status);
                    }
                }
            }
        }

        @Override
        public void close() { PipeOrgans.LOGGER.info("Receiver closed"); }
    }
}
