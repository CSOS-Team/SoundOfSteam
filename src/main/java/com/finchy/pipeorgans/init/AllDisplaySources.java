package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBPMDisplaySource;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

public class AllDisplaySources {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    public static final RegistryEntry<TrackerBarBPMDisplaySource> TRACKER_BAR_BPM = REGISTRATE.displaySource(
            "tracker_bar_bpm", TrackerBarBPMDisplaySource::new
    ).register();

    public static void register() {
    }
}
