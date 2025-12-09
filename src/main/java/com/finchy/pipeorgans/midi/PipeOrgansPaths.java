package com.finchy.pipeorgans.midi;

import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class PipeOrgansPaths {
    public static final Path GAME_DIR = FMLPaths.GAMEDIR.get();
    public static final Path MIDIS_DIR = GAME_DIR.resolve("midi_files");
    public static final Path UPLOADED_MIDIS_DIR = MIDIS_DIR.resolve("uploaded");
}
