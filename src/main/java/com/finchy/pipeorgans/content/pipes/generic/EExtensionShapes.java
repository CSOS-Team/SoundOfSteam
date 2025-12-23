package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;

public class EExtensionShapes {

    public interface ExtensionShape {}

    public enum QuadrupleShape implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), TRIPLE("triple"), QUAD("quad"), QUAD_CONNECTED("quad_connected");

        private final String name;
        QuadrupleShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum DoubleShape implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;
        DoubleShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum SingleShape implements ExtensionShape, StringRepresentable {
        SINGLE("single"), SINGLE_CONNECTED("single_connected");

        private final String name;
        SingleShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum HorizontalShape implements ExtensionShape, StringRepresentable {
        SINGLE("single"), DOUBLE("double"), DOUBLE_CONNECTED("double_connected");

        private final String name;
        HorizontalShape(String name) {
            this.name = name;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

}
