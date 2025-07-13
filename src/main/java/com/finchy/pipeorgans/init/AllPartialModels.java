package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

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
    PICCOLO_MOUTH_LARGE = block("piccolo/p_large_mouth"),
    PICCOLO_MOUTH_HUGE = block("piccolo/p_huge_mouth"),

    SUBBASS_MOUTH_TINY = block("subbass/s_small_mouth"),
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
    NASARD_MOUTH_HUGE = block("nasard/n_huge_mouth"),

    POSAUNE_MOUTH_TINY = block("posaune/ps_small_mouth"),
    POSAUNE_MOUTH_SMALL = block("posaune/ps_small_mouth"),
    POSAUNE_MOUTH_MEDIUM = block("posaune/ps_medium_mouth"),
    POSAUNE_MOUTH_LARGE = block("posaune/ps_large_mouth"),
    POSAUNE_MOUTH_HUGE = block("posaune/ps_huge_mouth"),

    VOX_HUMANA_MOUTH_TINY = block("vox_humana/vh_tiny_mouth"),
    VOX_HUMANA_MOUTH_SMALL = block("vox_humana/vh_small_mouth"),
    VOX_HUMANA_MOUTH_MEDIUM = block("vox_humana/vh_medium_mouth"),
    VOX_HUMANA_MOUTH_LARGE = block("vox_humana/vh_large_mouth"),
    VOX_HUMANA_MOUTH_HUGE = block("vox_humana/vh_huge_mouth"),

    VIOLA_MOUTH_TINY = block("viola/vi_tiny_mouth"),
    VIOLA_MOUTH_SMALL = block("viola/vi_small_mouth"),
    VIOLA_MOUTH_MEDIUM = block("viola/vi_medium_mouth"),
    VIOLA_MOUTH_LARGE = block("viola/vi_large_mouth"),
    VIOLA_MOUTH_HUGE = block("viola/vi_huge_mouth")
    ;


    private static PartialModel block(String path) {
        return PartialModel.of(PipeOrgans.asResource("block/" + path));
    }

    public static void init() {}
}
