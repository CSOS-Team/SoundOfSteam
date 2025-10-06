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

    public static final RegistryObject<SoundEvent> GEDECKT_SUPERHIGH = registerSoundEvent("gedeckt_superhigh");
    public static final RegistryObject<SoundEvent> GEDECKT_HIGH = registerSoundEvent("gedeckt_high");
    public static final RegistryObject<SoundEvent> GEDECKT_MEDIUM = registerSoundEvent("gedeckt_medium");
    public static final RegistryObject<SoundEvent> GEDECKT_LOW = registerSoundEvent("gedeckt_low");
    public static final RegistryObject<SoundEvent> GEDECKT_DEEP = registerSoundEvent("gedeckt_deep");

    public static final RegistryObject<SoundEvent> HOHLFLUTE_SUPERHIGH = registerSoundEvent("hohlflute_superhigh");
    public static final RegistryObject<SoundEvent> HOHLFLUTE_HIGH = registerSoundEvent("hohlflute_high");
    public static final RegistryObject<SoundEvent> HOHLFLUTE_MEDIUM = registerSoundEvent("hohlflute_medium");
    public static final RegistryObject<SoundEvent> HOHLFLUTE_LOW = registerSoundEvent("hohlflute_low");
    public static final RegistryObject<SoundEvent> HOHLFLUTE_DEEP = registerSoundEvent("hohlflute_deep");

    public static final RegistryObject<SoundEvent> ROHRFLOTE_SUPERHIGH = registerSoundEvent("rohrflote_superhigh");
    public static final RegistryObject<SoundEvent> ROHRFLOTE_HIGH = registerSoundEvent("rohrflote_high");
    public static final RegistryObject<SoundEvent> ROHRFLOTE_MEDIUM = registerSoundEvent("rohrflote_medium");
    public static final RegistryObject<SoundEvent> ROHRFLOTE_LOW = registerSoundEvent("rohrflote_low");
    public static final RegistryObject<SoundEvent> ROHRFLOTE_DEEP = registerSoundEvent("rohrflote_deep");

    public static final RegistryObject<SoundEvent> DIAPASON_SUPERHIGH = registerSoundEvent("diapason_superhigh");
    public static final RegistryObject<SoundEvent> DIAPASON_HIGH = registerSoundEvent("diapason_high");
    public static final RegistryObject<SoundEvent> DIAPASON_MEDIUM = registerSoundEvent("diapason_medium");
    public static final RegistryObject<SoundEvent> DIAPASON_LOW = registerSoundEvent("diapason_low");
    public static final RegistryObject<SoundEvent> DIAPASON_DEEP = registerSoundEvent("diapason_deep");

    public static final RegistryObject<SoundEvent> HAUNTED_WHISTLE_SUPERHIGH = registerSoundEvent("haunted_whistle_superhigh");
    public static final RegistryObject<SoundEvent> HAUNTED_WHISTLE_HIGH = registerSoundEvent("haunted_whistle_high");
    public static final RegistryObject<SoundEvent> HAUNTED_WHISTLE_MEDIUM = registerSoundEvent("haunted_whistle_medium");
    public static final RegistryObject<SoundEvent> HAUNTED_WHISTLE_LOW = registerSoundEvent("haunted_whistle_low");
    public static final RegistryObject<SoundEvent> HAUNTED_WHISTLE_DEEP = registerSoundEvent("haunted_whistle_deep");

    public static final RegistryObject<SoundEvent> PRESTANT_SUPERHIGH = registerSoundEvent("prestant_superhigh");
    public static final RegistryObject<SoundEvent> PRESTANT_HIGH = registerSoundEvent("prestant_high");
    public static final RegistryObject<SoundEvent> PRESTANT_MEDIUM = registerSoundEvent("prestant_medium");
    public static final RegistryObject<SoundEvent> PRESTANT_LOW = registerSoundEvent("prestant_low");
    public static final RegistryObject<SoundEvent> PRESTANT_DEEP = registerSoundEvent("prestant_deep");

    public static final RegistryObject<SoundEvent> GAMBA_SUPERHIGH = registerSoundEvent("gamba_superhigh");
    public static final RegistryObject<SoundEvent> GAMBA_HIGH = registerSoundEvent("gamba_high");
    public static final RegistryObject<SoundEvent> GAMBA_MEDIUM = registerSoundEvent("gamba_medium");
    public static final RegistryObject<SoundEvent> GAMBA_LOW = registerSoundEvent("gamba_low");
    public static final RegistryObject<SoundEvent> GAMBA_DEEP = registerSoundEvent("gamba_deep");

    public static final RegistryObject<SoundEvent> PICCOLO_SUPERHIGH = registerSoundEvent("piccolo_superhigh");
    public static final RegistryObject<SoundEvent> PICCOLO_HIGH = registerSoundEvent("piccolo_high");
    public static final RegistryObject<SoundEvent> PICCOLO_MEDIUM = registerSoundEvent("piccolo_medium");
    public static final RegistryObject<SoundEvent> PICCOLO_LOW = registerSoundEvent("piccolo_low");
    public static final RegistryObject<SoundEvent> PICCOLO_DEEP = registerSoundEvent("piccolo_deep");

    public static final RegistryObject<SoundEvent> SUBBASS_SUPERHIGH = registerSoundEvent("subbass_blah");
    public static final RegistryObject<SoundEvent> SUBBASS_HIGH = registerSoundEvent("subbass_high");
    public static final RegistryObject<SoundEvent> SUBBASS_MEDIUM = registerSoundEvent("subbass_medium");
    public static final RegistryObject<SoundEvent> SUBBASS_LOW = registerSoundEvent("subbass_low");
    public static final RegistryObject<SoundEvent> SUBBASS_DEEP = registerSoundEvent("subbass_deep");

    public static final RegistryObject<SoundEvent> TROMPETTE_SUPERHIGH = registerSoundEvent("trompette_superhigh");
    public static final RegistryObject<SoundEvent> TROMPETTE_HIGH = registerSoundEvent("trompette_high");
    public static final RegistryObject<SoundEvent> TROMPETTE_MEDIUM = registerSoundEvent("trompette_medium");
    public static final RegistryObject<SoundEvent> TROMPETTE_LOW = registerSoundEvent("trompette_low");
    public static final RegistryObject<SoundEvent> TROMPETTE_DEEP = registerSoundEvent("trompette_deep");

    public static final RegistryObject<SoundEvent> ENGLISH_HORN_SUPERHIGH = registerSoundEvent("english_horn_superhigh");
    public static final RegistryObject<SoundEvent> ENGLISH_HORN_HIGH = registerSoundEvent("english_horn_high");
    public static final RegistryObject<SoundEvent> ENGLISH_HORN_MEDIUM = registerSoundEvent("english_horn_medium");
    public static final RegistryObject<SoundEvent> ENGLISH_HORN_LOW = registerSoundEvent("english_horn_low");
    public static final RegistryObject<SoundEvent> ENGLISH_HORN_DEEP = registerSoundEvent("english_horn_deep");

    public static final RegistryObject<SoundEvent> NASARD_SUPERHIGH = registerSoundEvent("nasard_superhigh");
    public static final RegistryObject<SoundEvent> NASARD_HIGH = registerSoundEvent("nasard_high");
    public static final RegistryObject<SoundEvent> NASARD_MEDIUM = registerSoundEvent("nasard_medium");
    public static final RegistryObject<SoundEvent> NASARD_LOW = registerSoundEvent("nasard_low");
    public static final RegistryObject<SoundEvent> NASARD_DEEP = registerSoundEvent("nasard_deep");

    public static final RegistryObject<SoundEvent> POSAUNE_SUPERHIGH = registerSoundEvent("posaune_blah");
    public static final RegistryObject<SoundEvent> POSAUNE_HIGH = registerSoundEvent("posaune_high");
    public static final RegistryObject<SoundEvent> POSAUNE_MEDIUM = registerSoundEvent("posaune_medium");
    public static final RegistryObject<SoundEvent> POSAUNE_LOW = registerSoundEvent("posaune_low");
    public static final RegistryObject<SoundEvent> POSAUNE_DEEP = registerSoundEvent("posaune_deep");

    public static final RegistryObject<SoundEvent> VOX_HUMANA_SUPERHIGH = registerSoundEvent("vox_humana_superhigh");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_HIGH = registerSoundEvent("vox_humana_high");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_MEDIUM = registerSoundEvent("vox_humana_medium");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_LOW = registerSoundEvent("vox_humana_low");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_DEEP = registerSoundEvent("vox_humana_deep");

    public static final RegistryObject<SoundEvent> VIOLA_SUPERHIGH = registerSoundEvent("viola_superhigh");
    public static final RegistryObject<SoundEvent> VIOLA_HIGH = registerSoundEvent("viola_high");
    public static final RegistryObject<SoundEvent> VIOLA_MEDIUM = registerSoundEvent("viola_medium");
    public static final RegistryObject<SoundEvent> VIOLA_LOW = registerSoundEvent("viola_low");
    public static final RegistryObject<SoundEvent> VIOLA_DEEP = registerSoundEvent("viola_deep");

    public static final RegistryObject<SoundEvent> VOX_CELESTE_SUPERHIGH = registerSoundEvent("vox_celeste_superhigh");
    public static final RegistryObject<SoundEvent> VOX_CELESTE_HIGH = registerSoundEvent("vox_celeste_high");
    public static final RegistryObject<SoundEvent> VOX_CELESTE_MEDIUM = registerSoundEvent("vox_celeste_medium");
    public static final RegistryObject<SoundEvent> VOX_CELESTE_LOW = registerSoundEvent("vox_celeste_low");
    public static final RegistryObject<SoundEvent> VOX_CELESTE_DEEP = registerSoundEvent("vox_celeste_deep");


    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
