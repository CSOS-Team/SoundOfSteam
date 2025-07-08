package com.finchy.pipeorgans.midi.client;

import com.simibubi.create.foundation.utility.FilesHelper;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ClientMidiLoader {

    private List<Component> availableMidis;

    private static final String directory = "midi_files";
    private static final String extension = ".mid";

    public ClientMidiLoader() {
        availableMidis = new ArrayList<>();
        refresh();
    }

    public void refresh() {
        FilesHelper.createFolderIfMissing(directory);
        availableMidis.clear();

        try {
            Files.list(Paths.get(directory+"/"))
                    .filter(f -> !Files.isDirectory(f) && f.getFileName().toString().endsWith(extension)).forEach(path -> { // get all files in midi_files/, then filter based on whether they're a folder and end with .mid
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
            if (a.endsWith(extension))
                a = a.substring(0, a.length()-4); // remove the .mid from the end
            if (b.endsWith(extension))
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
        return Paths.get(directory, name + extension);
    }

}
