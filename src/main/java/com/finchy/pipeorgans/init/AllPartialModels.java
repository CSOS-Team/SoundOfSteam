package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.jozufozu.flywheel.core.PartialModel;

public class AllPartialModels {

    public static final PartialModel

    GEDECKT_MOUTH_SMALL = block("gedeckt/g_small_mouth"),
    GEDECKT_MOUTH_MEDIUM = block("gedeckt/g_medium_mouth"),
    GEDECKT_MOUTH_LARGE = block("gedeckt/g_large_mouth"),
    GEDECKT_MOUTH_HUGE = block("gedeckt/g_huge_mouth"),

    DIAPASON_MOUTH_SMALL = block("diapason/d_small_mouth"),
    DIAPASON_MOUTH_MEDIUM = block("diapason/d_medium_mouth"),
    DIAPASON_MOUTH_LARGE = block("diapason/d_large_mouth"),
    DIAPASON_MOUTH_HUGE = block("diapason/d_huge_mouth"),

    GAMBA_MOUTH_TINY = block("gamba/gm_tiny_mouth"),
    GAMBA_MOUTH_SMALL = block("gamba/gm_small_mouth"),
    GAMBA_MOUTH_MEDIUM = block("gamba/gm_medium_mouth"),
    GAMBA_MOUTH_LARGE = block("gamba/gm_large_mouth"),

    PICCOLO_MOUTH_TINY = block("piccolo/p_tiny_mouth"),
    PICCOLO_MOUTH_SMALL = block("piccolo/p_small_mouth"),
    PICCOLO_MOUTH_MEDIUM = block("piccolo/p_medium_mouth")

    ;


    private static PartialModel block(String path) {
        return new PartialModel(PipeOrgans.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }
}
