package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.util.MidiLoadException;
import com.finchy.pipeorgans.util.MidiUtils;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class MidiFileParser {

    public static Sequence getSequenceFromFile(String midi, String owner) throws MidiLoadException {

        Path path = Paths.get("midi_files/uploaded", owner, midi);
        if (!Files.exists(path)) {
            throw new MidiLoadException("MIDI file does not exist: " + path);
        }

        try {
            long size = Files.size(path);

            if (!validateSizeLimitation(size)) {
                throw new MidiLoadException("MIDI file is too large: " + size + " bytes");
            }
        } catch (IOException e) {
            throw new MidiLoadException("Failed to check file size for: " + path, e);
        }

        if (!isValidMidi(path.toFile())) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null)
                player.displayClientMessage(Component.literal(".mid file is in the wrong format"), false); // make translatable later
            throw new MidiLoadException("MIDI file is in the wrong format: " + path);
        }

        try {
            InputStream in = Files.newInputStream(path, StandardOpenOption.READ);
            return MidiSystem.getSequence(in);
        } catch (InvalidMidiDataException e) {
            throw new MidiLoadException("MIDI System was unable to get sequence from file: " + path, e);
        } catch (IOException e) {
            throw new MidiLoadException("IO error while reading MIDI file: " + path, e);
        }
    }

    public static List<Queue<MidiEvent>> parseMidiEvents(Sequence sequence) {

        List<Queue<MidiEvent>> trackList = new ArrayList<>();
        for (Track track : sequence.getTracks()) {
            Queue<MidiEvent> eventQueue = new LinkedList<>();

            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                eventQueue.add(event);
            }
            trackList.add(eventQueue);
        }
        return trackList;

    }

    public static void initialParse(Sequence sequence, BiConsumer<Integer, Integer> channelInstrumentConsumer, Consumer<byte[]> tempoConsumer) {
        Track[] tracks = sequence.getTracks();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);

                if (event.getTick() > 0) break; // we don't care about events after the track starts playing; we'll handle those after playing starts
                MidiMessage msg = event.getMessage();

                if (msg instanceof ShortMessage sm && MidiUtils.isProgramChange(sm)) {
                    channelInstrumentConsumer.accept(sm.getChannel(), sm.getData1()); // set instruments

                } else if (msg instanceof MetaMessage mm && MidiUtils.isTempoChange(mm)) {
                    tempoConsumer.accept(mm.getData()); // set tempo
                }
            }
        }
    }

    public static int endTick(Sequence sequence) {
        Track[] tracks = sequence.getTracks();
        long songEndTick = 1;
        for (Track track : tracks) {
            long trackEndTick = 1;
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                trackEndTick = Math.max(trackEndTick, event.getTick());
            }
            songEndTick = Math.max(songEndTick, trackEndTick);
        }
        return (int) songEndTick;
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
        return true;
    }
}
