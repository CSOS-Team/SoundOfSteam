package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;

public class ExtensionShapes {

    public interface IExtensionShape<T extends Enum<T> & IExtensionShape<T>> {
        // having the interface and enum as T lets the methods return the enum from which they are being called
        // e.g., returning a specific extension value (see getConnected() and getFullBlock())

        // java pros if you read this, please don't execute me for stating simple stuff, i just have no idea what i'm doing :(((

        boolean isConnected(); // whether the blockstate indicates that there are more extensions after this one

        boolean isFullBlockLong(); // true if no more extensions can be fit into the block

        int extensionNumber(); // the number of extensions represented by this shape

        T getConnected(); // return the "connected" shape

        T longestNonConnected(); // return the second-last shape (before "connected")

    }

    public enum Single implements IExtensionShape<Single>, StringRepresentable {
        SINGLE("single", 1), SINGLE_CONNECTED("single_connected", 1);

        private final String name;
        private final int extensionNumber;
        Single(String name, int extensionNumber) {
            this.name = name;
            this.extensionNumber = extensionNumber;
        }

        @Override
        public boolean isConnected() {
            return this.equals(Single.SINGLE_CONNECTED); // define the connected blockstate
        }

        @Override
        public boolean isFullBlockLong() {
            return true;
        }

        @Override
        public int extensionNumber() {
            return extensionNumber;
        }

        @Override
        public Single getConnected() {
            return SINGLE_CONNECTED;
        }

        @Override
        public Single longestNonConnected() {
            return SINGLE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Double implements IExtensionShape<Double>, StringRepresentable {
        SINGLE("single", 1), DOUBLE("double", 2), DOUBLE_CONNECTED("double_connected", 2);

        private final String name;
        private final int extensionNumber;
        Double(String name, int extensionNumber) {
            this.name = name;
            this.extensionNumber = extensionNumber;
        }

        @Override
        public boolean isConnected() {
            return this.equals(Double.DOUBLE_CONNECTED);
        }

        @Override
        public boolean isFullBlockLong() {
            return this.equals(DOUBLE) || this.equals(DOUBLE_CONNECTED); // DOUBLE and DOUBLE_CONNECTED are both 1 full block tall
        }

        @Override
        public int extensionNumber() {
            return extensionNumber;
        }

        @Override
        public Double getConnected() {
            return DOUBLE_CONNECTED;
        }

        @Override
        public Double longestNonConnected() {
            return DOUBLE;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

    public enum Quadruple implements IExtensionShape<Quadruple>, StringRepresentable {
        SINGLE("single", 1), DOUBLE("double", 2), TRIPLE("triple", 3), QUAD("quad", 4), QUAD_CONNECTED("quad_connected", 4);

        private final String name;
        private final int extensionNumber;
        Quadruple(String name, int extensionNumber) {
            this.name = name;
            this.extensionNumber = extensionNumber;
        }

        @Override
        public boolean isConnected() {
            return this.equals(QUAD_CONNECTED);
        }

        @Override
        public boolean isFullBlockLong() {
            return this.equals(Quadruple.QUAD) || this.equals(QUAD_CONNECTED); // QUAD and QUAD_CONNECTED are both 1 full block tall
        }

        @Override
        public int extensionNumber() {
            return extensionNumber;
        }

        @Override
        public Quadruple getConnected() {
            return QUAD_CONNECTED;
        }

        @Override
        public Quadruple longestNonConnected() {
            return QUAD;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

}
