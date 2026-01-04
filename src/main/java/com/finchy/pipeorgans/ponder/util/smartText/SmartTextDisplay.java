package com.finchy.pipeorgans.ponder.util.smartText;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.data.Couple;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class SmartTextDisplay {

    protected CreateSceneBuilder scene;
    protected SceneBuildingUtil util;
    protected Function<String, Integer> durationProvider;
    protected Function<String, Integer> bufferProvider;

    public SmartTextDisplay(CreateSceneBuilder scene, SceneBuildingUtil util) {
        this(scene, util, PonderTimings::getCalculatedReadingTime, PonderTimings::getCalculatedBufferTime);
    }

    public SmartTextDisplay(CreateSceneBuilder scene, SceneBuildingUtil util, Function<String, Integer> durationProvider, Function<String, Integer> bufferProvider) {
        this.bufferProvider = bufferProvider;
        this.durationProvider = durationProvider;
        this.scene = scene;
        this.util = util;
    }

    public int getMinDuration(SmartText text) {
        return text.durationOverride().getMinimum(durationProvider.apply(text.text()));
    }

    public int getMinBuffer(SmartText text) {
        return text.bufferOverride().getMinimum(bufferProvider.apply(text.text()));
    }

    public int getFixedDuration(SmartText text) {
        return text.durationOverride().getFixed(durationProvider.apply(text.text()));
    }

    public int getFixedBuffer(SmartText text) {
        return text.bufferOverride().getFixed(bufferProvider.apply(text.text()));
    }

    public Couple<Integer> show(SmartText text) {
        int duration = Math.max(
                getMinDuration(text),
                getFixedDuration(text)
        );
        text.building().accept(scene.overlay().showText(duration).text(text.text()));
        int buffer = Math.max(
                getMinBuffer(text),
                getFixedBuffer(text)
        );
        return Couple.create(duration, buffer);
    }

    public void showAndIdle(SmartText text) {
        Couple<Integer> times = show(text);
        scene.idle(times.getFirst() + times.getSecond());
    }
}
