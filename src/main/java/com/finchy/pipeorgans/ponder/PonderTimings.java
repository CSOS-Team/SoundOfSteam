package com.finchy.pipeorgans.ponder;

public final class PonderTimings {
    private PonderTimings() {}

    private static final int TICKS_PER_SECOND = 20;

    public static final int READING_TIME = TICKS_PER_SECOND * 5;
    public static final int READING_BUFFER = TICKS_PER_SECOND;
    public static final int READING_WINDOW = READING_TIME + READING_BUFFER;

    public static final int BUILD_STEP = TICKS_PER_SECOND / 4;
    public static final int BUILD_FINISH = TICKS_PER_SECOND / 2;

    public static int afterBuffer() {
        return afterBuffer(1);
    }

    public static int afterBuffer(int times) {
        return READING_WINDOW - READING_BUFFER * times;
    }

    public static final int INTERACTION_DISPLAY_TIME = TICKS_PER_SECOND * 1;

    public static final int CONTEXT_INFO_BUFFER = TICKS_PER_SECOND / 2;
}
