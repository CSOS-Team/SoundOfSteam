package com.finchy.pipeorgans.midi;

import com.finchy.pipeorgans.PipeOrgans;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;

public class MidiSourceDevice implements MidiDevice {

    private boolean isOpen = false;
    final Receiver receiver = new MidiSourceReceiver();
    protected final List<Receiver> receivers;
    protected final List<Transmitter> transmitters;

    private final String uuid;

    private final Info DEVICE_INFO;

    public MidiSourceDevice(String uuid, Info DEVICE_INFO) {
        this.uuid = uuid;
        this.DEVICE_INFO = DEVICE_INFO;
        transmitters = new ArrayList<>();
        receivers = new ArrayList<>();
        receivers.add(receiver);

    }

    public String getUuid() {
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
            PipeOrgans.LOGGER.info(DEVICE_INFO.getName()+" "+uuid+" opened");
        } else { throw(new MidiUnavailableException()); }
    }

    @Override
    public void close() {
        if (isOpen) {
            isOpen = false;
            PipeOrgans.LOGGER.info(DEVICE_INFO.getName()+" "+uuid+" closed");
            for (Transmitter transmitter : transmitters) {
                transmitter.close();
            }
            transmitters.clear();
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
        return 0; // 'tis but a facade
    }

    @Override
    public int getMaxTransmitters() {
        return -1;
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
        Transmitter transmitter = new MidiSourceTransmitter();
        transmitters.add(transmitter);
        return transmitter;
    }

    @Override
    public List<Transmitter> getTransmitters() {
        return transmitters;
    }

    protected class MidiSourceReceiver implements Receiver {

        @Override
        public void send(MidiMessage message, long timeStamp) {
            for (Transmitter transmitter : transmitters) {
                Receiver downstreamReceiver = transmitter.getReceiver();
                if (downstreamReceiver != null) {
                    downstreamReceiver.send(message, timeStamp);
                }
            }
        }

        @Override
        public void close() {
            PipeOrgans.LOGGER.info("Receiver closed");
        }
    }

    protected class MidiSourceTransmitter implements Transmitter {
        private Receiver receiver;

        @Override
        public void setReceiver(Receiver receiver) {
            this.receiver = receiver;
        }

        @Override
        public Receiver getReceiver() {
            return receiver;
        }

        @Override
        public void close() {
            PipeOrgans.LOGGER.info("Transmitter closed");
            setReceiver(null);
        }
    }
}
