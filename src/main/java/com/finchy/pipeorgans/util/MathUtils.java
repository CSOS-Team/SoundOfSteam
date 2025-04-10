package com.finchy.pipeorgans.util;

public abstract class MathUtils {

    public static float map(float value, float start1, float stop1, float start2, float stop2) {
        return start2 + (stop2 - start2) * ((value - start1) / (stop1 - start1));
        // thank you, processing!
    }

}
