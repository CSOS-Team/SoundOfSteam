package com.finchy.pipeorgans.util.midi;

import java.util.HashMap;
import java.util.Map;

public abstract class AllPitchMappings {

    private static final Map<String, PitchMapping> mappings = new HashMap<>();

    public static void addMapping(String id, PitchMapping mapping) {
        mappings.put(id, mapping);
    }

    public static PitchMapping getMapping(String id) {
        return mappings.get(id);
    }

}
