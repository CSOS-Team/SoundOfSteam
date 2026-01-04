package com.finchy.pipeorgans.ponder.util.smartText;

import com.finchy.pipeorgans.ponder.util.actionBuilder.timing.override.TimingOverride;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Consumer;

public record SmartText(String text, Consumer<TextElementBuilder> building, TimingOverride durationOverride, TimingOverride bufferOverride) {
    public SmartText(String text, Consumer<TextElementBuilder> building) {
        this(text, building, TimingOverride.NONE, TimingOverride.NONE);
    }

    public SmartText withTimings(@Nullable TimingOverride durationOverride, @Nullable TimingOverride bufferOverride) {
        return new SmartText(this.text, this.building,
                Optional.ofNullable(durationOverride).orElse(TimingOverride.NONE),
                Optional.ofNullable(bufferOverride).orElse(TimingOverride.NONE)
        );
    }

    public static SmartText plainFloating(String text, boolean keyed) {
        return new SmartText(text, builder -> {if (keyed) builder.attachKeyFrame();});
    }

    public static SmartText plainPointing(String text, Vec3 anchor, boolean keyed, boolean nearAnchor) {
        return new SmartText(text, builder -> {
            builder.pointAt(anchor);
            if (nearAnchor)
                builder.placeNearTarget();
            if (keyed)
                builder.attachKeyFrame();
        });
    }

    public static SmartText plainPointing(String text, Vec3 anchor) {
        return plainPointing(text, anchor, false, false);
    }

    public static SmartText coloredFloating(String text, PonderPalette color, boolean keyed) {
        return new SmartText(text, builder -> {
            builder.colored(color);
            if (keyed) builder.attachKeyFrame();
        });
    }

    public static SmartText coloredPointing(String text, Vec3 anchor, PonderPalette color, boolean keyed, boolean nearAnchor) {
        return new SmartText(text, builder -> {
            builder.pointAt(anchor);
            builder.colored(color);
            if (keyed) builder.attachKeyFrame();
            if (nearAnchor) builder.placeNearTarget();
        });
    }
}