package com.finchy.pipeorgans.ponder.util.smartText;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.util.timing.TimingMap;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.data.Pair;
import net.createmod.ponder.api.scene.SceneBuilder;
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
    public record ShowAction(TimingMap timings, Runnable showRunnable) {
        public void run(SceneBuilder scene) {
            showRunnable.run();
        }

        public void runAndIdle(SceneBuilder scene) {
            run(scene);
            scene.idle(timings.getChannelTotal(TimingMapHelp.CHANNEL_TOTAL));
        }
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

    public TimingMap show(SmartText text) {
        ShowAction action = getShowAction(text);
        action.run(scene);
        return action.timings();
    }

    public ShowAction getShowAction(SmartText text) {
        TimingMap map = new TimingMap(2);
        map.set(TimingMapHelp.CHANNEL_CALCULATED, TimingMapHelp.DURATION_SLOT, durationProvider.apply(text.text()));
        map.set(TimingMapHelp.CHANNEL_CALCULATED, TimingMapHelp.BUFFER_SLOT, bufferProvider.apply(text.text()));

        map.calculateChannel(TimingMapHelp.CHANNEL_MINIMUMS, TimingMapHelp.getMinimumsCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_FIXED, TimingMapHelp.getFixedCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_COMBINED_HIGHEST, TimingMapHelp.COMBINED_HIGHEST_CALCULATOR);
        map.calculateChannel(TimingMapHelp.CHANNEL_ADDED, TimingMapHelp.getAddedCalculator(text));
        map.calculateChannel(TimingMapHelp.CHANNEL_TOTAL, TimingMapHelp.TOTAL_CALCULATOR);

        Runnable action = () -> text.building().accept(scene.overlay().showText(map.get(TimingMapHelp.CHANNEL_TOTAL, TimingMapHelp.DURATION_SLOT)).text(text.text()));

        return new ShowAction(map, action);
    }

    public void showAndIdle(SmartText text) {
        ShowAction action = getShowAction(text);
        action.runAndIdle(scene);
    }
}
