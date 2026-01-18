package com.finchy.pipeorgans.ponder;

public final class PonderTimings {
    private PonderTimings() {}

    private static final int TICKS_PER_SECOND = 20;
    private static final int HALF_SECOND = TICKS_PER_SECOND / 2;

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

    /**
     * Returns the number of ticks to last N seconds
    */
    public static int seconds(int seconds) {
        return TICKS_PER_SECOND * seconds;
    }

    public static final int INTERACTION_DISPLAY_TIME = TICKS_PER_SECOND;

    public static final int CONTEXT_INFO_BUFFER = TICKS_PER_SECOND / 2;

    public static int getCalculatedReadingTime(String text) {
        int length = text.length();
        // min of Base time (1s) + time per character and minimum of 4 seconds. This is halved because Comfy Reading slows reading speed by half, so in Comfy Reading it fits for slower readers, and with it disabled it's less annoying for fast readers.
        // a bit of research suggested an average reading speed of ~16 characters per second for a text of reasonable complexity (both in topic and language).
        // The one-second base and four-second minimum are to account for very short texts, and were picked pretty arbitrarily.
        return Math.max(TICKS_PER_SECOND + (int) Math.ceil(length / 16f * HALF_SECOND), TICKS_PER_SECOND * 4);
    }

    public static int getCalculatedBufferTime(String text) {
        // min of 10% of reading time or one second as buffer
        // this is also pretty arbitrary, and will require some testing to see if it feels right.
        return Math.max((int) Math.ceil(getCalculatedReadingTime(text) * 0.1f), TICKS_PER_SECOND);
    }
}
