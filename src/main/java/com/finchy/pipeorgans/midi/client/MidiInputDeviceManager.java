package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class MidiInputDeviceManager {

    protected Receiver activeReceiver = null;
    protected Transmitter activeTransmitter = null;
    protected MidiDevice activeDevice = null;
    private String selectedDeviceName = "";
    private String midiDeviceError = null;

    public MidiInputDeviceManager() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                PipeOrgans.LOGGER.info("Closing selected MIDI device...");
                ExecutorService ex = Executors.newSingleThreadExecutor();
                Future<?> future = ex.submit(() -> close());
                try {
                    future.get(10000, TimeUnit.MILLISECONDS);
                    ex.shutdownNow();
                    ex = null;
                    interrupt();
                } catch (Exception e) {
                    PipeOrgans.LOGGER.error("Java ran into an error closing the selected MIDI device: {}", e.getMessage());
                    if (!future.isDone()) future.cancel(true);
                    try {
                        ex.shutdownNow();
                        interrupt();
                    } catch (Exception e1) {
                        PipeOrgans.LOGGER.error("Failed to stop executor: {}", e1.getMessage());
                        interrupt();
                    }
                }
            }
        });
    }

    public String getActiveDeviceName() {
        if (activeDevice != null) {
            return activeDevice.getDeviceInfo().getName();
        }
        return "None";
    }

    public boolean isDeviceSelected() {
        return (selectedDeviceName != null & !this.selectedDeviceName.trim().isEmpty());
    }

    public String getSelectedDeviceName() {
        return selectedDeviceName;
    }

    public String getSelectedDeviceError() {
        return midiDeviceError;
    }

    public void saveDeviceSelection(MidiDevice device) {
        selectedDeviceName = device.getDeviceInfo().getName();
        open();
    }

    public List<MidiDevice> getAvailableDevices() {
        List<MidiDevice> devices = new ArrayList<>();

        for (MidiDevice.Info info : MidiSystem.getMidiDeviceInfo()) {
            try {
                devices.add(MidiSystem.getMidiDevice(info));
            } catch (MidiUnavailableException e) {
                PipeOrgans.LOGGER.warn("Midi Device error. Skipping. Error: {}", e.getMessage());
            }
        }

        if (!devices.isEmpty()) {
            devices = devices.stream().filter(d -> (
                    d.getMaxTransmitters()!=0 && (d.getMaxTransmitters()==-1 || d.getTransmitters().size()<d.getMaxTransmitters()) // only if device supports transmitters and if it has free transmitter(s)
                )).toList();
        }

        return devices;
    }

    protected void openTransmitter() {
        PipeOrgans.LOGGER.info("Opening MIDI input device: {}", selectedDeviceName);

        for (MidiDevice device : getAvailableDevices()) {
            if (device.getDeviceInfo().getName().equals(selectedDeviceName)) {
                PipeOrgans.LOGGER.info("Found matching MIDI input device.");
                try {
                    if (activeDevice != null) {
                        activeDevice.close();
                        activeReceiver.close();
                        activeReceiver = null;
                    }
                    activeDevice = device;
                    activeDevice.open();
                    activeTransmitter = device.getTransmitter();
                    activeReceiver = new MidiDeviceInputReceiver();
                    activeTransmitter.setReceiver(activeReceiver);
                    PipeOrgans.LOGGER.info("Successfully opened MIDI input device: {}", selectedDeviceName);
                } catch (MidiUnavailableException e) {
                    PipeOrgans.LOGGER.error("Failed to open device {}: {}", selectedDeviceName, e.getMessage());
                    midiDeviceError = e.getMessage();
                    close();
                }
            }
        }
    }

    public void open() {
        if (isDeviceSelected()) {
            openTransmitter();
            return;
        }
        PipeOrgans.LOGGER.error("Error opening MidiInputDeviceManager: no input device selected!");
    }

    public void close() {
        if (activeReceiver != null) {
            activeReceiver.close();
            activeReceiver = null;
        }

        if (activeTransmitter != null) {
            activeTransmitter.close();
            activeTransmitter = null;
        }

        if (activeDevice != null) {
            activeDevice.close();
            activeDevice = null;
        }
    }

}
