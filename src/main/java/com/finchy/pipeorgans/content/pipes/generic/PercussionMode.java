package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public enum PercussionMode implements StringRepresentable {
    TAP("tap"), ROLL("roll");

    private final String name;
    PercussionMode(String name) {
        this.name = name;
    }

    @Override
    public @NotNull String getSerializedName() {
        return name;
    }
}
