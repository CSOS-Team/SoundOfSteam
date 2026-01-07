package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;

public class ExtensionShapes {

    public interface ExtensionShape {}

    public enum Quadruple implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUAD("quad"), QUAD_CONNECTED("quad_connected");

        private final String name;
        Quadruple(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Double implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;
        Double(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Single implements ExtensionShape, StringRepresentable {
        SINGLE("single"), SINGLE_CONNECTED("single_connected");

        private final String name;
        Single(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Horizontal implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;
        Horizontal(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

}
