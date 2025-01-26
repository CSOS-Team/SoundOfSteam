package com.finchy.pipeorgans.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class Generic {

    public enum ExtensionShape implements StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;

        ExtensionShape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public enum QuadrupleExtensionShape implements StringRepresentable {
        SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUADRUPLE("quadruple"), QUADRUPLE_CONNECTED("quadruple_connected");

        private final String name;

        QuadrupleExtensionShape(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
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
            return this.name;
        }
    }

    public enum GambaWhistleSize implements StringRepresentable {
        TINY("tiny"), SMALL("small"), MEDIUM("medium"), LARGE("large");

        private final String name;

        GambaWhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

    public enum PiccoloWhistleSize implements StringRepresentable {
        TINY("tiny"), SMALL("small"), MEDIUM("medium");

        private final String name;

        PiccoloWhistleSize(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }

}
