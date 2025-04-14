package com.finchy.pipeorgans.midi.pitchMappings;

import java.util.HashMap;
import java.util.Map;

public abstract class AllPitchMappings {

    private static final Map<String, PitchMapping> allPitchMappings = new HashMap<>();

    public static void addMapping(PitchMapping mapping) {
        allPitchMappings.put(mapping.id(), mapping);
    }

    public static PitchMapping getMapping(String id) {
        return allPitchMappings.get(id);
    }

    public static void register() {
        addMapping(new PipeCentricPitchMapping());
        addMapping(new CPitchMapping());
    }

}
