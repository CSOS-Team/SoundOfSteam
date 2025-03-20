package com.finchy.pipeorgans.midi;

import com.finchy.pipeorgans.PipeOrgans;

import javax.sound.midi.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class KeyboardRelayDevice extends MidiSourceDevice {

    private MidiDevice keyboard = null;

    public KeyboardRelayDevice(String uuid) {
        super(uuid,
            new Info(
                    "Keyboard Relay", "Finchy",
                    uuid, "1.0"
            ) {}
        );
    }

    public void selectKeyboard() {
        Info[] infos = MidiSystem.getMidiDeviceInfo();
        ArrayList<Info> validInfos = new ArrayList<>();

        for (Info info : infos) {
            try {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxTransmitters() != 0) {
                    validInfos.add(info);
                }
            } catch (MidiUnavailableException e) {
                PipeOrgans.LOGGER.error(Arrays.toString(e.getStackTrace()));
            }
        }

        for (Info info : validInfos) {
            String indent = "    > ";
            PipeOrgans.LOGGER.info(info.getName());
            PipeOrgans.LOGGER.info(indent+info.getDescription());
            PipeOrgans.LOGGER.info(indent+info.getVendor());
            PipeOrgans.LOGGER.info(indent+info.getVersion()+"\n");
        }
        PipeOrgans.LOGGER.info("Please select a device to attach:");

        Scanner scanner = new Scanner(System.in);
        String selectedName = scanner.nextLine();
        MidiDevice selectedDevice = null;
        for (Info info : validInfos) {
            if (info.getName().equals(selectedName)) {
                try {
                    selectedDevice = MidiSystem.getMidiDevice(info);
                } catch (MidiUnavailableException e) {
                    PipeOrgans.LOGGER.error(Arrays.toString(e.getStackTrace()));
                }
                break;
            }
        }
        if (selectedDevice == null) {
            PipeOrgans.LOGGER.info("The device could not be found. Returning...");
            return;
        }
        System.out.printf("Attaching device %s...", selectedName);
        attachKeyboard(selectedDevice);
    }

    public void attachKeyboard(MidiDevice device) {
        keyboard = device;
        try {
            keyboard.open();
            Transmitter transmitter = keyboard.getTransmitter();
            Receiver receiver = getReceiver();
            transmitter.setReceiver(receiver);
            PipeOrgans.LOGGER.info(" attached.");
        } catch (MidiUnavailableException e) {
            PipeOrgans.LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }
}