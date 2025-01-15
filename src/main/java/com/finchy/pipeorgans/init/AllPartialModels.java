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
    DIAPASON_MOUTH_HUGE = block("diapason/d_huge_mouth")

    ;


    private static PartialModel block(String path) {
        return new PartialModel(PipeOrgans.asResource("block/" + path));
    }

    public static void init() {
        // init static fields
    }
}
