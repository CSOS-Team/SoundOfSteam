package com.finchy.pipeorgans.midi.client;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.trackerBar.MidiFileParser;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.MidiUploadPacket;
import com.simibubi.create.foundation.utility.FilesHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.*;

@OnlyIn(Dist.CLIENT)
public class ClientMidiLoader {

    private List<Component> availableMidis;
    private Map<String, InputStream> activeUploads;
    private int packetCycle;

    private static final int PACKET_DELAY = 10;
    private static final String DIRECTORY = "midi_files";
    private static final String EXTENSION = ".mid";

    public ClientMidiLoader() {
        availableMidis = new ArrayList<>();
        activeUploads = new HashMap<>();
        refresh();
    }

    public void tick() {
        if (activeUploads.isEmpty())
            return;
        if (packetCycle-- > 0)
            return;
        packetCycle = PACKET_DELAY;

        for (String midi : new HashSet<>(activeUploads.keySet()))
            continueUpload(midi);
    }

    public void startNewUpload(String midi) {
        Path path = Paths.get(DIRECTORY, midi);
        if (!Files.exists(path)) {
            PipeOrgans.LOGGER.error("Missing .mid file: {}", path);
            return;
        }

        InputStream in;
        try {
            long size = Files.size(path);

            if(!MidiFileParser.validateSizeLimitation(size))
                return;

            if (!MidiFileParser.isValidMidi(path.toFile())) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null)
                    player.displayClientMessage(Component.literal(".mid file is in the wrong format"), false); // make translatable later
                return;
            }

            in = Files.newInputStream(path, StandardOpenOption.READ);
            activeUploads.put(midi, in);
            AllPackets.getChannel().sendToServer(MidiUploadPacket.begin(midi, size));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void continueUpload(String midi) {
        if (activeUploads.containsKey(midi)) {
            int maxPacketSize = 1024; // max midi packet size; add to config later
            byte[] data = new byte[maxPacketSize];
            try {
                int status = activeUploads.get(midi).read(data);
                if (status != -1) {
                    if (status < maxPacketSize)
                        data = Arrays.copyOf(data, status);
                    if (Minecraft.getInstance().level != null)
                        AllPackets.getChannel().sendToServer(MidiUploadPacket.write(midi, data));
                    else {
                        activeUploads.remove(midi);
                        return;
                    }
                }
                if (status < maxPacketSize)
                    finishUpload(midi);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void finishUpload(String midi) {
        if (activeUploads.containsKey(midi)) {
            AllPackets.getChannel().sendToServer(MidiUploadPacket.finish(midi));
            activeUploads.remove(midi);
        }
    }

    public void refresh() {
        FilesHelper.createFolderIfMissing(DIRECTORY);
        availableMidis.clear();

        try {
            Files.list(Paths.get(DIRECTORY +"/"))
                    .filter(f -> !Files.isDirectory(f) && f.getFileName().toString().endsWith(EXTENSION)).forEach(path -> { // get all files in midi_files/, then filter based on whether they're a folder and end with .mid
                        if (Files.isDirectory(path)) // if path is a folder ending with .mid, not a .mid file
                            return; // this seems redundant, but create does it, so...

                        availableMidis.add(Component.literal(path.getFileName().toString())); // add to list of available midis
                    });
        } catch (NoSuchFileException e) {
            // no midis in the folder
        } catch (IOException e) {
            e.printStackTrace();
        }

        availableMidis.sort((aT, bT) -> {
            String a = aT.getString();
            String b = bT.getString();
            if (a.endsWith(EXTENSION))
                a = a.substring(0, a.length()-4); // remove the .mid from the end
            if (b.endsWith(EXTENSION))
                b = b.substring(0, b.length()-4); // as above

            int aLen = a.length();
            int bLen = b.length();
            int minSize = Math.min(aLen, bLen); // length of smallest filename
            char aChar, bChar;
            boolean aNumber, bNumber;
            boolean asNumeric = false; // whether comparing as numbers

            int lastNumericCompare = 0;
            for (int i=0; i<minSize; i++) {
                aChar = a.charAt(i);
                bChar = b.charAt(i);
                aNumber = aChar >= '0' && aChar <= '9'; // whether aChar is a number
                bNumber = bChar >= '0' && bChar <= '9'; // whether bChar is a number

                if (asNumeric) { // if comparing as numbers
                    if (aNumber && bNumber) { // if A and B are both numbers
                        if (lastNumericCompare == 0)  // if aChar and bChar were the same digit (or there are no digits)
                            lastNumericCompare = aChar - bChar;
                    } else if (aNumber) // if just A is a number
                        return 1; // A comes first
                    else if (bNumber) // if just B is a number
                        return -1; // B comes first
                    else if (lastNumericCompare == 0) {
                        if (aChar != bChar)
                            return aChar - bChar;
                        asNumeric = false;
                    } else
                        return lastNumericCompare;

                } else if (aNumber && bNumber) { // if not comparing as numbers yet, but should be
                    asNumeric = true; // compare next chars as numbers
                    if (lastNumericCompare == 0)
                        lastNumericCompare = aChar - bChar; // +ve if aChar bigger, -ve if bChar bigger
                } else if (aChar != bChar) // if not comparing as numbers
                    return aChar - bChar;
            }
            if (asNumeric) { // if final char was a number
                if (aLen > bLen && a.charAt(bLen) >= '0' && a.charAt(bLen) <= '9') // if A, and aChar at B final position is a number
                    return  1; // A is bigger, thus B is smaller
                else if (bLen > aLen && b.charAt(aLen) >= '0' && b.charAt(aLen) <= '9') // if B, and bChar at A final position is a number
                    return  -1; // B is bigger, thus A is smaller
                else if (lastNumericCompare == 0)
                    return aLen - bLen;
                else
                    return lastNumericCompare;
            } else
                return aLen - bLen;
        });
    }

    public List<Component> getAvailableMidis() {
        return availableMidis;
    }

    public Path getPath(String name) {
        return Paths.get(DIRECTORY, name + EXTENSION);
    }

}
