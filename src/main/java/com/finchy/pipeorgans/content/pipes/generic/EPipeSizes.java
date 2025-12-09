package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class EPipeSizes {

    public enum PipeSize implements StringRepresentable {
        TINY("tiny"), SMALL("small"), MEDIUM("medium"), LARGE("large"), HUGE("huge");

        private final String name;
        PipeSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
