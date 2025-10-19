package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.simibubi.create.Create;
import net.createmod.catnip.render.SpriteShiftEntry;
import net.createmod.catnip.render.SpriteShifter;

public class AllSpriteShifts {

    public static final SpriteShiftEntry SCROLLING_MUSIC =
            get("block/tracker_bar/music", "block/tracker_bar/music_scroll");

    private static SpriteShiftEntry get(String originalLocation, String targetLocation) {
        return SpriteShifter.get(PipeOrgans.asResource(originalLocation), Create.asResource(targetLocation));
    }
}
