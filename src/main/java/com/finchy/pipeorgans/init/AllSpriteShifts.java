package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class AllSpriteShifts {

    public static final SpriteShiftEntry SCROLLING_MUSIC =
            SpriteShifter.get(PipeOrgans.asResource("block/tracker_bar/paper_texture"), PipeOrgans.asResource("block/tracker_bar/paper_scroll_texture"));

    public static void register() {
    }
}
