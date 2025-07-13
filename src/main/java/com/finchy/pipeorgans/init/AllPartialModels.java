package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class AllPartialModels {

    public static final PartialModel

    GEDECKT_MOUTH_TINY = block("gedeckt/gedeckt_tiny_mouth"),
    GEDECKT_MOUTH_SMALL = block("gedeckt/gedeckt_small_mouth"),
    GEDECKT_MOUTH_MEDIUM = block("gedeckt/gedeckt_medium_mouth"),
    GEDECKT_MOUTH_LARGE = block("gedeckt/gedeckt_large_mouth"),
    GEDECKT_MOUTH_HUGE = block("gedeckt/gedeckt_huge_mouth"),

    DIAPASON_MOUTH_TINY = block("diapason/diapason_tiny_mouth"),
    DIAPASON_MOUTH_SMALL = block("diapason/diapason_small_mouth"),
    DIAPASON_MOUTH_MEDIUM = block("diapason/diapason_medium_mouth"),
    DIAPASON_MOUTH_LARGE = block("diapason/diapason_large_mouth"),
    DIAPASON_MOUTH_HUGE = block("diapason/diapason_huge_mouth"),

    GAMBA_MOUTH_TINY = block("gamba/gamba_tiny_mouth"),
    GAMBA_MOUTH_SMALL = block("gamba/gamba_small_mouth"),
    GAMBA_MOUTH_MEDIUM = block("gamba/gamba_medium_mouth"),
    GAMBA_MOUTH_LARGE = block("gamba/gamba_large_mouth"),
    GAMBA_MOUTH_HUGE = block("gamba/gamba_huge_mouth"),

    PICCOLO_MOUTH_TINY = block("piccolo/piccolo_tiny_mouth"),
    PICCOLO_MOUTH_SMALL = block("piccolo/piccolo_small_mouth"),
    PICCOLO_MOUTH_MEDIUM = block("piccolo/piccolo_medium_mouth"),
    PICCOLO_MOUTH_LARGE = block("piccolo/piccolo_large_mouth"),
    PICCOLO_MOUTH_HUGE = block("piccolo/piccolo_huge_mouth"),

    SUBBASS_MOUTH_TINY = block("subbass/subbass_small_mouth"),
    SUBBASS_MOUTH_SMALL = block("subbass/subbass_small_mouth"),
    SUBBASS_MOUTH_MEDIUM = block("subbass/subbass_medium_mouth"),
    SUBBASS_MOUTH_LARGE = block("subbass/subbass_large_mouth"),
    SUBBASS_MOUTH_HUGE = block("subbass/subbass_huge_mouth"),

    TROMPETTE_MOUTH_TINY = block("trompette/trompette_tiny_mouth"),
    TROMPETTE_MOUTH_SMALL = block("trompette/trompette_small_mouth"),
    TROMPETTE_MOUTH_MEDIUM = block("trompette/trompette_medium_mouth"),
    TROMPETTE_MOUTH_LARGE = block("trompette/trompette_large_mouth"),
    TROMPETTE_MOUTH_HUGE = block("trompette/trompette_huge_mouth"),

    NASARD_MOUTH_TINY = block("nasard/nasard_tiny_mouth"),
    NASARD_MOUTH_SMALL = block("nasard/nasard_small_mouth"),
    NASARD_MOUTH_MEDIUM = block("nasard/nasard_medium_mouth"),
    NASARD_MOUTH_LARGE = block("nasard/nasard_large_mouth"),
    NASARD_MOUTH_HUGE = block("nasard/nasard_huge_mouth"),

    POSAUNE_MOUTH_TINY = block("posaune/posaune_small_mouth"),
    POSAUNE_MOUTH_SMALL = block("posaune/posaune_small_mouth"),
    POSAUNE_MOUTH_MEDIUM = block("posaune/posaune_medium_mouth"),
    POSAUNE_MOUTH_LARGE = block("posaune/posaune_large_mouth"),
    POSAUNE_MOUTH_HUGE = block("posaune/posaune_huge_mouth"),

    VOX_HUMANA_MOUTH_TINY = block("vox_humana/vox_humana_tiny_mouth"),
    VOX_HUMANA_MOUTH_SMALL = block("vox_humana/vox_humana_small_mouth"),
    VOX_HUMANA_MOUTH_MEDIUM = block("vox_humana/vox_humana_medium_mouth"),
    VOX_HUMANA_MOUTH_LARGE = block("vox_humana/vox_humana_large_mouth"),
    VOX_HUMANA_MOUTH_HUGE = block("vox_humana/vox_humana_huge_mouth"),

    VIOLA_MOUTH_TINY = block("viola/viola_tiny_mouth"),
    VIOLA_MOUTH_SMALL = block("viola/viola_small_mouth"),
    VIOLA_MOUTH_MEDIUM = block("viola/viola_medium_mouth"),
    VIOLA_MOUTH_LARGE = block("viola/viola_large_mouth"),
    VIOLA_MOUTH_HUGE = block("viola/viola_huge_mouth")
    ;


    private static PartialModel block(String path) {
        return PartialModel.of(PipeOrgans.asResource("block/" + path));
    }

    public static void init() {}
}
