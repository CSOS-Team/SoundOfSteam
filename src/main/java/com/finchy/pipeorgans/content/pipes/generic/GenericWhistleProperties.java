package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class GenericWhistleProperties {

    public enum QuadrupleExtensionShape implements StringRepresentable {
        SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUAD("quad"), QUAD_CONNECTED("quad_connected");

        private final String name;
        QuadrupleExtensionShape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public enum WhistleShape implements StringRepresentable {
        GENERIC("generic"), SLIM("slim");

        private final String name;
        WhistleShape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

    public enum WhistleSize implements StringRepresentable {
        TINY("tiny"), SMALL("small"), MEDIUM("medium"), LARGE("large"), HUGE("huge");

        private final String name;
        WhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }

}
