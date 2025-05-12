package com.finchy.pipeorgans.midi.pitchMappings;

import java.util.HashMap;
import java.util.Map;

public abstract class AllPitchMappings {

    private static final Map<String, PitchMapping> allPitchMappings = new HashMap<>();

    public static PitchMapping PIPE_CENTRIC = add(new PipeCentricPitchMapping());
    public static PitchMapping C_CENTRIC = add(new CPitchMapping());

    public static PitchMapping getMapping(String id) {
        return allPitchMappings.getOrDefault(id, C_CENTRIC);
    }

    public static PitchMapping add(PitchMapping mapping) {
        allPitchMappings.put(mapping.name(), mapping);
        return mapping;
    }

    public static void register() {}



}
