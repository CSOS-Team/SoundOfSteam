package com.finchy.pipeorgans.content.pipes.generic;

public enum ExtensionMode {

    // this enum handles the logic for extension shape TYPES, while enums in ExtensionShapes are for handling blockstates

    SINGLE(1), DOUBLE(2), QUADRUPLE(4);

    private final int extensionsPerBlock;
    ExtensionMode(int extensionsPerBlock) {
        this.extensionsPerBlock = extensionsPerBlock;
    }

    public int getExtensionsPerBlock() {
        return extensionsPerBlock;
    }

}
