package com.finchy.pipeorgans.ponder.util.smartText;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.util.timing.TimingMap;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;

import java.util.function.Function;

public class SmartTextDisplay {

    protected CreateSceneBuilder scene;
    protected SceneBuildingUtil util;
    protected Function<String, Integer> durationProvider;
    protected Function<String, Integer> bufferProvider;

    public static class TimingMapHelp {
        public static final int CHANNEL_CALCULATED = 0;
        public static final int CHANNEL_MINIMUMS = 1;
        public static final int CHANNEL_FIXED = 2;
        public static final int CHANNEL_COMBINED_HIGHEST = 3;
        public static final int CHANNEL_ADDED = 4;
        public static final int CHANNEL_TOTAL = 5;

        public static final int DURATION_SLOT = 0;
        public static final int BUFFER_SLOT = 1;

        protected static TimingMap.Calculator getMinimumsCalculator(SmartText text) {
            return (slot, getter) -> switch (slot) {
                case 0 -> text.durationOverride().getMinimum(getter.apply(CHANNEL_CALCULATED));
                case 1 -> text.bufferOverride().getMinimum(getter.apply(CHANNEL_CALCULATED));
                default -> 0;
            };
        }

        protected static TimingMap.Calculator getFixedCalculator(SmartText text) {
            return (slot, getter) -> switch (slot) {
                case 0 -> text.durationOverride().getFixed(getter.apply(CHANNEL_CALCULATED));
                case 1 -> text.bufferOverride().getFixed(getter.apply(CHANNEL_CALCULATED));
                default -> 0;
            };
        }

        protected static TimingMap.Calculator getAddedCalculator(SmartText text) {
            return (slot, getter) -> switch (slot) {
                case 0 -> text.durationOverride().getAdded(getter.apply(CHANNEL_COMBINED_HIGHEST));
                case 1 -> text.bufferOverride().getAdded(getter.apply(CHANNEL_COMBINED_HIGHEST));
                default -> 0;
            };
        }

        protected static final TimingMap.Calculator COMBINED_HIGHEST_CALCULATOR = (slot, getter) -> Math.max(getter.apply(CHANNEL_MINIMUMS), getter.apply(CHANNEL_FIXED));
        protected static final TimingMap.Calculator TOTAL_CALCULATOR = (slot, getter) -> getter.apply(CHANNEL_COMBINED_HIGHEST) + getter.apply(CHANNEL_ADDED);
    }

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

    public TimingMap show(SmartText text) {
        TimingMap map = new TimingMap(2);
        map.set(TimingMapHelp.CHANNEL_CALCULATED, TimingMapHelp.DURATION_SLOT, durationProvider.apply(text.text()));
        map.set(TimingMapHelp.CHANNEL_CALCULATED, TimingMapHelp.BUFFER_SLOT, bufferProvider.apply(text.text()));

        map.calculateChannel(TimingMapHelp.CHANNEL_MINIMUMS, TimingMapHelp.getMinimumsCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_FIXED, TimingMapHelp.getFixedCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_COMBINED_HIGHEST, TimingMapHelp.COMBINED_HIGHEST_CALCULATOR);
        map.calculateChannel(TimingMapHelp.CHANNEL_ADDED, TimingMapHelp.getAddedCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_TOTAL, TimingMapHelp.TOTAL_CALCULATOR);

        text.building().accept(scene.overlay().showText(map.get(TimingMapHelp.CHANNEL_TOTAL, TimingMapHelp.DURATION_SLOT)).text(text.text()));
        return map;
    }

    public void showAndIdle(SmartText text) {
        TimingMap map = show(text);
        scene.idle(map.getChannelTotal(TimingMapHelp.CHANNEL_TOTAL));
    }
}
