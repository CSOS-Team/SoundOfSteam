package com.finchy.pipeorgans.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class Generic {

    public enum GenericExtensionShape implements StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;

        GenericExtensionShape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public enum WhistleSize implements StringRepresentable {
        SMALL("small"), MEDIUM("medium"), LARGE("large"), HUGE("huge");

        private final String name;

        WhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public enum SmallWhistleSize implements StringRepresentable {
        TINY("tiny"), SMALL("small"), MEDIUM("medium"), LARGE("large");

        private final String name;

        SmallWhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}
