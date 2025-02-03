package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.jozufozu.flywheel.core.PartialModel;

public class AllPartialModels {

    public static final PartialModel

    GEDECKT_MOUTH_TINY = block("gedeckt/g_tiny_mouth"),
    GEDECKT_MOUTH_SMALL = block("gedeckt/g_small_mouth"),
    GEDECKT_MOUTH_MEDIUM = block("gedeckt/g_medium_mouth"),
    GEDECKT_MOUTH_LARGE = block("gedeckt/g_large_mouth"),
    GEDECKT_MOUTH_HUGE = block("gedeckt/g_huge_mouth"),

    DIAPASON_MOUTH_TINY = block("diapason/d_tiny_mouth"),
    DIAPASON_MOUTH_SMALL = block("diapason/d_small_mouth"),
    DIAPASON_MOUTH_MEDIUM = block("diapason/d_medium_mouth"),
    DIAPASON_MOUTH_LARGE = block("diapason/d_large_mouth"),
    DIAPASON_MOUTH_HUGE = block("diapason/d_huge_mouth"),

    GAMBA_MOUTH_TINY = block("gamba/gm_tiny_mouth"),
    GAMBA_MOUTH_SMALL = block("gamba/gm_small_mouth"),
    GAMBA_MOUTH_MEDIUM = block("gamba/gm_medium_mouth"),
    GAMBA_MOUTH_LARGE = block("gamba/gm_large_mouth"),
    GAMBA_MOUTH_HUGE = block("gamba/gm_huge_mouth"),

    PICCOLO_MOUTH_TINY = block("piccolo/p_tiny_mouth"),
    PICCOLO_MOUTH_SMALL = block("piccolo/p_small_mouth"),
    PICCOLO_MOUTH_MEDIUM = block("piccolo/p_medium_mouth"),
    PICCOLO_MOUTH_LARGE = block("piccolo/p_medium_large"),
    PICCOLO_MOUTH_HUGE = block("piccolo/p_medium_huge"),

    SUBBASS_MOUTH_SMALL = block("subbass/s_small_mouth"),
    SUBBASS_MOUTH_MEDIUM = block("subbass/s_medium_mouth"),
    SUBBASS_MOUTH_LARGE = block("subbass/s_large_mouth"),
    SUBBASS_MOUTH_HUGE = block("subbass/s_huge_mouth"),

    TROMPETTE_MOUTH_TINY = block("trompette/t_tiny_mouth"),
    TROMPETTE_MOUTH_SMALL = block("trompette/t_small_mouth"),
    TROMPETTE_MOUTH_MEDIUM = block("trompette/t_medium_mouth"),
    TROMPETTE_MOUTH_LARGE = block("trompette/t_large_mouth"),
    TROMPETTE_MOUTH_HUGE = block("trompette/t_huge_mouth"),

    NASARD_MOUTH_TINY = block("nasard/n_tiny_mouth"),
    NASARD_MOUTH_SMALL = block("nasard/n_small_mouth"),
    NASARD_MOUTH_MEDIUM = block("nasard/n_medium_mouth"),
    NASARD_MOUTH_LARGE = block("nasard/n_large_mouth"),
    NASARD_MOUTH_HUGE = block("nasard/n_huge_mouth")

    ;


    private static PartialModel block(String path) {
        return new PartialModel(PipeOrgans.asResource("block/" + path));
    }

    public static void init() {}
}
