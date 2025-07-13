package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class EExtensionShapes {

    public interface ExtensionShape {
        int getIndex();
    }

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

        @Override
        public int getIndex() {
            return ordinal();
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

        @Override
        public int getIndex() {
            return ordinal();
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

        @Override
        public int getIndex() {
            return ordinal();
        }
    }

}
