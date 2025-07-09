package com.finchy.pipeorgans.util;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

import javax.sound.midi.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class MidiUtils {

    public static boolean isNoteOn(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0; // if message is note on AND velocity > 0
    }

    public static boolean isNoteOff(ShortMessage sm) {
        return sm.getCommand() == ShortMessage.NOTE_OFF || sm.getData2() == 0; // if message is note off OR velocity == 0
    }

    public abstract class MidiParser {

        public static List<Queue<MidiEvent>> parseMidiFile(String midi, String owner) {
            midi = "test.mid";
            owner = "Dev";

            Path path = Paths.get("midi_files/uploaded",owner,midi);
            if (!Files.exists(path)) {
                PipeOrgans.LOGGER.error("Missing .mid file: {}", path);
                return null;
            }
            InputStream in;
            try {
                long size = Files.size(path);

                if (!validateSizeLimitation(size)) {
                    PipeOrgans.LOGGER.error(".mid file is too large!: {}", path);
                    return null;
                }

                if (!isValidMidi(path.toFile())) {
                    LocalPlayer player = Minecraft.getInstance().player;
                    if (player != null)
                        player.displayClientMessage(Component.literal(".mid file is in the wrong format"), false); // make translatable later
                    return null;
                }

                in = Files.newInputStream(path, StandardOpenOption.READ);
                Sequence sequence = MidiSystem.getSequence(in);

                List<Queue<MidiEvent>> trackList = new ArrayList<>();
                for (Track track : sequence.getTracks()) {
                    Queue<MidiEvent> eventQueue = new LinkedList<>();

                    for (int i = 0; i < track.size(); i++) {
                        MidiEvent event = track.get(i);
                        eventQueue.add(event);
                    }
                    trackList.add(eventQueue);
                    PipeOrgans.LOGGER.info("ADDED TRACK!");
                }
                PipeOrgans.LOGGER.info("FINISHED PARSING MIDI!");
                return trackList;

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InvalidMidiDataException e) {
                e.printStackTrace();
            }

            return null;
        }

        // check the first 4 bytes of the given file to see if they match the header 4D 54 68 64
        public static boolean isValidMidi(File file) {
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] bytes = new byte[4];
                if (fis.read(bytes) != 4)
                    return false;

                int byte1 = bytes[0] & 0xFF;
                int byte2 = bytes[1] & 0xFF;
                int byte3 = bytes[2] & 0xFF;
                int byte4 = bytes[3] & 0xFF;

                return byte1 == 0x4D && byte2 == 0x54 && byte3 == 0x68 && byte4 == 0x64;
            } catch (IOException exception) {
                return false;
            }
        }

        public static boolean validateSizeLimitation(long size) {
            if (Minecraft.getInstance().hasSingleplayerServer())
                return true;
            int maxSize = 256; // max midi file size; add to config later
            if (size > maxSize * 1000) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    player.displayClientMessage(Component.literal("Midi upload too large") // make translatable later
                            .append(Component.literal(" (" + size/1000 + " KB).")), false);
                    player.displayClientMessage(Component.literal("Maximum size is")
                            .append(Component.literal(" " + maxSize + " KB")), false);
                }
                return false;
            }
            return true;
        }
    }

}
