package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PipeOrgans.MOD_ID);


    // declare sounds here

    public static final RegistryObject<SoundEvent>

    TRACKER_BAR_CHANGE_ROLL = registerSoundEvent("tracker_bar_change_roll"),
    
     GEDECKT_SUPERHIGH = registerSoundEvent("gedeckt_superhigh"),
     GEDECKT_HIGH = registerSoundEvent("gedeckt_high"),
     GEDECKT_MEDIUM = registerSoundEvent("gedeckt_medium"),
     GEDECKT_LOW = registerSoundEvent("gedeckt_low"),
     GEDECKT_DEEP = registerSoundEvent("gedeckt_deep"),

     HOHLFLUTE_SUPERHIGH = registerSoundEvent("hohlflute_superhigh"),
     HOHLFLUTE_HIGH = registerSoundEvent("hohlflute_high"),
     HOHLFLUTE_MEDIUM = registerSoundEvent("hohlflute_medium"),
     HOHLFLUTE_LOW = registerSoundEvent("hohlflute_low"),
     HOHLFLUTE_DEEP = registerSoundEvent("hohlflute_deep"),

     ROHRFLOTE_SUPERHIGH = registerSoundEvent("rohrflote_superhigh"),
     ROHRFLOTE_HIGH = registerSoundEvent("rohrflote_high"),
     ROHRFLOTE_MEDIUM = registerSoundEvent("rohrflote_medium"),
     ROHRFLOTE_LOW = registerSoundEvent("rohrflote_low"),
     ROHRFLOTE_DEEP = registerSoundEvent("rohrflote_deep"),

     DIAPASON_SUPERHIGH = registerSoundEvent("diapason_superhigh"),
     DIAPASON_HIGH = registerSoundEvent("diapason_high"),
     DIAPASON_MEDIUM = registerSoundEvent("diapason_medium"),
     DIAPASON_LOW = registerSoundEvent("diapason_low"),
     DIAPASON_DEEP = registerSoundEvent("diapason_deep"),

     HAUNTED_WHISTLE_SUPERHIGH = registerSoundEvent("haunted_whistle_superhigh"),
     HAUNTED_WHISTLE_HIGH = registerSoundEvent("haunted_whistle_high"),
     HAUNTED_WHISTLE_MEDIUM = registerSoundEvent("haunted_whistle_medium"),
     HAUNTED_WHISTLE_LOW = registerSoundEvent("haunted_whistle_low"),
     HAUNTED_WHISTLE_DEEP = registerSoundEvent("haunted_whistle_deep"),

     PRESTANT_SUPERHIGH = registerSoundEvent("prestant_superhigh"),
     PRESTANT_HIGH = registerSoundEvent("prestant_high"),
     PRESTANT_MEDIUM = registerSoundEvent("prestant_medium"),
     PRESTANT_LOW = registerSoundEvent("prestant_low"),
     PRESTANT_DEEP = registerSoundEvent("prestant_deep"),

     GAMBA_SUPERHIGH = registerSoundEvent("gamba_superhigh"),
     GAMBA_HIGH = registerSoundEvent("gamba_high"),
     GAMBA_MEDIUM = registerSoundEvent("gamba_medium"),
     GAMBA_LOW = registerSoundEvent("gamba_low"),
     GAMBA_DEEP = registerSoundEvent("gamba_deep"),

     PICCOLO_SUPERHIGH = registerSoundEvent("piccolo_superhigh"),
     PICCOLO_HIGH = registerSoundEvent("piccolo_high"),
     PICCOLO_MEDIUM = registerSoundEvent("piccolo_medium"),
     PICCOLO_LOW = registerSoundEvent("piccolo_low"),
     PICCOLO_DEEP = registerSoundEvent("piccolo_deep"),

     SUBBASS_SUPERHIGH = registerSoundEvent("subbass_superhigh"),
     SUBBASS_HIGH = registerSoundEvent("subbass_high"),
     SUBBASS_MEDIUM = registerSoundEvent("subbass_medium"),
     SUBBASS_LOW = registerSoundEvent("subbass_low"),
     SUBBASS_DEEP = registerSoundEvent("subbass_deep"),

    OPEN_WOOD_SUPERHIGH = registerSoundEvent("open_wood_superhigh"),
    OPEN_WOOD_HIGH = registerSoundEvent("open_wood_high"),
    OPEN_WOOD_MEDIUM = registerSoundEvent("open_wood_medium"),
    OPEN_WOOD_LOW = registerSoundEvent("open_wood_low"),
    OPEN_WOOD_DEEP = registerSoundEvent("open_wood_deep"),

     TROMPETTE_SUPERHIGH = registerSoundEvent("trompette_superhigh"),
     TROMPETTE_HIGH = registerSoundEvent("trompette_high"),
     TROMPETTE_MEDIUM = registerSoundEvent("trompette_medium"),
     TROMPETTE_LOW = registerSoundEvent("trompette_low"),
     TROMPETTE_DEEP = registerSoundEvent("trompette_deep"),

    CHAMADE_SUPERHIGH = registerSoundEvent("chamade_superhigh"),
    CHAMADE_HIGH = registerSoundEvent("chamade_high"),
    CHAMADE_MEDIUM = registerSoundEvent("chamade_medium"),
    CHAMADE_LOW = registerSoundEvent("chamade_low"),
    CHAMADE_DEEP = registerSoundEvent("chamade_deep"),

     ENGLISH_HORN_SUPERHIGH = registerSoundEvent("english_horn_superhigh"),
     ENGLISH_HORN_HIGH = registerSoundEvent("english_horn_high"),
     ENGLISH_HORN_MEDIUM = registerSoundEvent("english_horn_medium"),
     ENGLISH_HORN_LOW = registerSoundEvent("english_horn_low"),
     ENGLISH_HORN_DEEP = registerSoundEvent("english_horn_deep"),

     NASARD_SUPERHIGH = registerSoundEvent("nasard_superhigh"),
     NASARD_HIGH = registerSoundEvent("nasard_high"),
     NASARD_MEDIUM = registerSoundEvent("nasard_medium"),
     NASARD_LOW = registerSoundEvent("nasard_low"),
     NASARD_DEEP = registerSoundEvent("nasard_deep"),

    TIERCE_SUPERHIGH = registerSoundEvent("tierce_superhigh"),
    TIERCE_HIGH = registerSoundEvent("tierce_high"),
    TIERCE_MEDIUM = registerSoundEvent("tierce_medium"),
    TIERCE_LOW = registerSoundEvent("tierce_low"),
    TIERCE_DEEP = registerSoundEvent("tierce_deep"),

     POSAUNE_SUPERHIGH = registerSoundEvent("posaune_superhigh"),
     POSAUNE_HIGH = registerSoundEvent("posaune_high"),
     POSAUNE_MEDIUM = registerSoundEvent("posaune_medium"),
     POSAUNE_LOW = registerSoundEvent("posaune_low"),
     POSAUNE_DEEP = registerSoundEvent("posaune_deep"),

    BASSOON_SUPERHIGH = registerSoundEvent("bassoon_superhigh"),
    BASSOON_HIGH = registerSoundEvent("bassoon_high"),
    BASSOON_MEDIUM = registerSoundEvent("bassoon_medium"),
    BASSOON_LOW = registerSoundEvent("bassoon_low"),
    BASSOON_DEEP = registerSoundEvent("bassoon_deep"),

     VOX_HUMANA_SUPERHIGH = registerSoundEvent("vox_humana_superhigh"),
     VOX_HUMANA_HIGH = registerSoundEvent("vox_humana_high"),
     VOX_HUMANA_MEDIUM = registerSoundEvent("vox_humana_medium"),
     VOX_HUMANA_LOW = registerSoundEvent("vox_humana_low"),
     VOX_HUMANA_DEEP = registerSoundEvent("vox_humana_deep"),

    VOX_HUMANA_SUPERHIGH_TREM = registerSoundEvent("vox_humana_superhigh_trem"),
    VOX_HUMANA_HIGH_TREM = registerSoundEvent("vox_humana_high_trem"),
    VOX_HUMANA_MEDIUM_TREM = registerSoundEvent("vox_humana_medium_trem"),
    VOX_HUMANA_LOW_TREM = registerSoundEvent("vox_humana_low_trem"),
    VOX_HUMANA_DEEP_TREM = registerSoundEvent("vox_humana_deep_trem"),

     VIOLA_SUPERHIGH = registerSoundEvent("viola_superhigh"),
     VIOLA_HIGH = registerSoundEvent("viola_high"),
     VIOLA_MEDIUM = registerSoundEvent("viola_medium"),
     VIOLA_LOW = registerSoundEvent("viola_low"),
     VIOLA_DEEP = registerSoundEvent("viola_deep"),

     VOX_CELESTE_SUPERHIGH = registerSoundEvent("vox_celeste_superhigh"),
     VOX_CELESTE_HIGH = registerSoundEvent("vox_celeste_high"),
     VOX_CELESTE_MEDIUM = registerSoundEvent("vox_celeste_medium"),
     VOX_CELESTE_LOW = registerSoundEvent("vox_celeste_low"),
     VOX_CELESTE_DEEP = registerSoundEvent("vox_celeste_deep"),

     HAUNTED_CHIFF = registerSoundEvent("haunted_chiff");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
