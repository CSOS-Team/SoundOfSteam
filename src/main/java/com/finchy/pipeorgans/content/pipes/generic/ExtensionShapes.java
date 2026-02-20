package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.util.StringRepresentable;

public class ExtensionShapes {

    public interface IExtensionShape<T extends Enum<T> & IExtensionShape<T>> {
        /*
        Having the interface and enum as T lets the methods return the enum from which they are being called

        e.g., returning a specific extension value (see getConnected() and getFullBlock())

        Java pros if you read this, please don't execute me for stating simple stuff, I just have no idea what I'm doing :(((
        */
        boolean isConnected(); // whether the blockstate indicates that there are more extensions after this one

        boolean isFullBlockLong(); // true if no more extensions can be fit into the block

        boolean isSingle(); // true if removing any extensions would destroy the block

        int extensionNumber(); // the number of extensions represented by this shape

        T getConnected(); // return the "connected" shape

        T getLongestNonConnected(); // return the second-last shape (before "connected")

        /*
        used in sneak wrenching
        returns the extension shape to set based on how far "up" the pipe the player clicked
        higher clickPosition indicates further out from the pipe, between 0 and 1
        e.g. for Double, if they clicked in the upper half, return the extension shape for the lower half (Double.SINGLE)
        if they clicked such that the entire thing should be removed, return null
         */
        T getExtensionShapeForClickPosition(double clickPosition);
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
        public boolean isSingle() {
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
        public Single getLongestNonConnected() {
            return SINGLE;
        }

        // never gonna get called anyway, as the function returns before this point if isSingle() returns true (which it does)
        @Override
        public Single getExtensionShapeForClickPosition(double clickPosition) {
            return null;
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
        public boolean isSingle() {
            return this.equals(SINGLE);
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
        public Double getLongestNonConnected() {
            return DOUBLE;
        }

        @Override
        public Double getExtensionShapeForClickPosition(double clickPosition) {
            if (clickPosition > 0.5) // clicked in the upper half
                return Double.SINGLE;
            return null;
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
        public boolean isSingle() {
            return this.equals(SINGLE);
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
        public Quadruple getLongestNonConnected() {
            return QUAD;
        }

        @Override
        public Quadruple getExtensionShapeForClickPosition(double clickPosition) {
            // 1st quarter is closest to pipe, 4th quarter is furthest from pipe
            if (clickPosition > 0.75) // clicked in the 4th quarter
                return Quadruple.TRIPLE;
            else if (clickPosition > 0.5) // clicked in the 3rd or 4th quarters (but not 4th quarter at this point)
                return Quadruple.DOUBLE;
            else if (clickPosition > 0.25) // clicked in 2nd, 3rd, or 4th quarters (but not 3rd or 4th at this point)
                return Quadruple.SINGLE;
            else // clicked in the 1st quarter
                return null;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }

}
