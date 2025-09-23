package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.model.Models;

public class TrackerBarVisual extends SingleAxisRotatingVisual<TrackerBarBlockEntity> {
    public TrackerBarVisual(VisualizationContext context, TrackerBarBlockEntity blockEntity, float partialTick) {
        super(context, blockEntity, partialTick, Models.partial(AllPartialModels.TRACKER_BAR_SHAFT));
    }
}
