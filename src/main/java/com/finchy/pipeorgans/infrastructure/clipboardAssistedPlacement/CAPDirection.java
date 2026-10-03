package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

public enum CAPDirection {
    FORWARD,
    BACKWARD;

    public CAPDirection opposite() {
        return this == FORWARD ? BACKWARD : FORWARD;
    }

    public int toSignedInt() {
        return this == FORWARD ? 1 : -1;
    }

    public <T> T map(T forwardValue, T backwardValue) {
        return this == FORWARD ? forwardValue : backwardValue;
    }
}
