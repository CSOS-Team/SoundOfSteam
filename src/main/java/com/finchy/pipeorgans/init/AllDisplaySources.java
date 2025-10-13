package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarDisplaySources;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.util.entry.RegistryEntry;

import java.util.function.Supplier;

public class AllDisplaySources {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    public static final RegistryEntry<TrackerBarDisplaySources.FilenameSource> TRACKER_BAR_FILENAME = simple("tracker_bar_filename", TrackerBarDisplaySources.FilenameSource::new);
    public static final RegistryEntry<TrackerBarDisplaySources.BPMSource> TRACKER_BAR_BPM = simple("tracker_bar_bpm", TrackerBarDisplaySources.BPMSource::new);

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> supplier) {
        return REGISTRATE.displaySource(name, supplier).register();
    }

    public static void register() {
    }
}
