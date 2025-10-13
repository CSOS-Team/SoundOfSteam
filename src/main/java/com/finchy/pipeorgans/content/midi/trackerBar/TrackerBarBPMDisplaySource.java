package com.finchy.pipeorgans.content.midi.trackerBar;

import com.finchy.pipeorgans.content.midi.MidiSequencerBehaviour;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.SingleLineDisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TrackerBarBPMDisplaySource extends SingleLineDisplaySource {
    
    @Override
    protected MutableComponent provideLine(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity sourceBE = context.getSourceBlockEntity();
        if (!(sourceBE instanceof TrackerBarBlockEntity trackerBar))
            return EMPTY_LINE;

        MidiSequencerBehaviour sequencerBehaviour = trackerBar.getBehaviour(MidiSequencerBehaviour.TYPE);
        if (sequencerBehaviour == null)
            return EMPTY_LINE;
        if (!sequencerBehaviour.isSequenceLoaded())
            return EMPTY_LINE;
        
        return Component.literal(sequencerBehaviour.get10xBPM() / 10 + " BPM");
    }

    @Override
    protected String getTranslationKey() {
        return "tracker_bar_bpm";
    }

    @Override
    protected boolean allowsLabeling(DisplayLinkContext context) {
        return true;
    }
}
