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

    public static final RegistryObject<SoundEvent> GEDECKT_SUPERHIGH = registerSoundEvents("gedeckt_superhigh");
    public static final RegistryObject<SoundEvent> GEDECKT_HIGH = registerSoundEvents("gedeckt_high");
    public static final RegistryObject<SoundEvent> GEDECKT_MEDIUM = registerSoundEvents("gedeckt_medium");
    public static final RegistryObject<SoundEvent> GEDECKT_LOW = registerSoundEvents("gedeckt_low");
    public static final RegistryObject<SoundEvent> GEDECKT_DEEP = registerSoundEvents("gedeckt_deep");

    public static final RegistryObject<SoundEvent> DIAPASON_SUPERHIGH = registerSoundEvents("diapason_superhigh");
    public static final RegistryObject<SoundEvent> DIAPASON_HIGH = registerSoundEvents("diapason_high");
    public static final RegistryObject<SoundEvent> DIAPASON_MEDIUM = registerSoundEvents("diapason_medium");
    public static final RegistryObject<SoundEvent> DIAPASON_LOW = registerSoundEvents("diapason_low");
    public static final RegistryObject<SoundEvent> DIAPASON_DEEP = registerSoundEvents("diapason_deep");

    public static final RegistryObject<SoundEvent> GAMBA_SUPERHIGH = registerSoundEvents("gamba_superhigh");
    public static final RegistryObject<SoundEvent> GAMBA_HIGH = registerSoundEvents("gamba_high");
    public static final RegistryObject<SoundEvent> GAMBA_MEDIUM = registerSoundEvents("gamba_medium");
    public static final RegistryObject<SoundEvent> GAMBA_LOW = registerSoundEvents("gamba_low");
    public static final RegistryObject<SoundEvent> GAMBA_DEEP = registerSoundEvents("gamba_deep");

    public static final RegistryObject<SoundEvent> PICCOLO_SUPERHIGH = registerSoundEvents("piccolo_superhigh");
    public static final RegistryObject<SoundEvent> PICCOLO_HIGH = registerSoundEvents("piccolo_high");
    public static final RegistryObject<SoundEvent> PICCOLO_MEDIUM = registerSoundEvents("piccolo_medium");
    public static final RegistryObject<SoundEvent> PICCOLO_LOW = registerSoundEvents("piccolo_low");
    public static final RegistryObject<SoundEvent> PICCOLO_DEEP = registerSoundEvents("piccolo_deep");

    public static final RegistryObject<SoundEvent> SUBBASS_HIGH = registerSoundEvents("subbass_high");
    public static final RegistryObject<SoundEvent> SUBBASS_MEDIUM = registerSoundEvents("subbass_medium");
    public static final RegistryObject<SoundEvent> SUBBASS_LOW = registerSoundEvents("subbass_low");
    public static final RegistryObject<SoundEvent> SUBBASS_DEEP = registerSoundEvents("subbass_deep");

    public static final RegistryObject<SoundEvent> TROMPETTE_SUPERHIGH = registerSoundEvents("trompette_superhigh");
    public static final RegistryObject<SoundEvent> TROMPETTE_HIGH = registerSoundEvents("trompette_high");
    public static final RegistryObject<SoundEvent> TROMPETTE_MEDIUM = registerSoundEvents("trompette_medium");
    public static final RegistryObject<SoundEvent> TROMPETTE_LOW = registerSoundEvents("trompette_low");
    public static final RegistryObject<SoundEvent> TROMPETTE_DEEP = registerSoundEvents("trompette_deep");

    public static final RegistryObject<SoundEvent> NASARD_SUPERHIGH = registerSoundEvents("nasard_superhigh");
    public static final RegistryObject<SoundEvent> NASARD_HIGH = registerSoundEvents("nasard_high");
    public static final RegistryObject<SoundEvent> NASARD_MEDIUM = registerSoundEvents("nasard_medium");
    public static final RegistryObject<SoundEvent> NASARD_LOW = registerSoundEvents("nasard_low");
    public static final RegistryObject<SoundEvent> NASARD_DEEP = registerSoundEvents("nasard_deep");

    public static final RegistryObject<SoundEvent> POSAUNE_HIGH = registerSoundEvents("posaune_high");
    public static final RegistryObject<SoundEvent> POSAUNE_MEDIUM = registerSoundEvents("posaune_medium");
    public static final RegistryObject<SoundEvent> POSAUNE_LOW = registerSoundEvents("posaune_low");
    public static final RegistryObject<SoundEvent> POSAUNE_DEEP = registerSoundEvents("posaune_deep");

    public static final RegistryObject<SoundEvent> VOX_HUMANA_SUPERHIGH = registerSoundEvents("vox_humana_superhigh");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_HIGH = registerSoundEvents("vox_humana_high");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_MEDIUM = registerSoundEvents("vox_humana_medium");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_LOW = registerSoundEvents("vox_humana_low");
    public static final RegistryObject<SoundEvent> VOX_HUMANA_DEEP = registerSoundEvents("vox_humana_deep");

    public static final RegistryObject<SoundEvent> HORN_GREAT_HARMONY = registerSoundEvents("horn_great_harmony");
    public static final RegistryObject<SoundEvent> HORN_GREAT_MELODY = registerSoundEvents("horn_great_melody");
    public static final RegistryObject<SoundEvent> HORN_GREAT_BASS = registerSoundEvents("horn_great_bass");

    public static final RegistryObject<SoundEvent> HORN_OLD_HARMONY = registerSoundEvents("horn_old_harmony");
    public static final RegistryObject<SoundEvent> HORN_OLD_MELODY = registerSoundEvents("horn_old_melody");
    public static final RegistryObject<SoundEvent> HORN_OLD_BASS = registerSoundEvents("horn_old_bass");

    public static final RegistryObject<SoundEvent> HORN_PURE_HARMONY = registerSoundEvents("horn_pure_harmony");
    public static final RegistryObject<SoundEvent> HORN_PURE_MELODY = registerSoundEvents("horn_pure_melody");
    public static final RegistryObject<SoundEvent> HORN_PURE_BASS = registerSoundEvents("horn_pure_bass");

    public static final RegistryObject<SoundEvent> HORN_HUMBLE_HARMONY = registerSoundEvents("horn_humble_harmony");
    public static final RegistryObject<SoundEvent> HORN_HUMBLE_MELODY = registerSoundEvents("horn_humble_melody");
    public static final RegistryObject<SoundEvent> HORN_HUMBLE_BASS = registerSoundEvents("horn_humble_bass");

    public static final RegistryObject<SoundEvent> HORN_DRY_HARMONY = registerSoundEvents("horn_dry_harmony");
    public static final RegistryObject<SoundEvent> HORN_DRY_MELODY = registerSoundEvents("horn_dry_melody");
    public static final RegistryObject<SoundEvent> HORN_DRY_BASS = registerSoundEvents("horn_dry_bass");

    public static final RegistryObject<SoundEvent> HORN_CLEAR_HARMONY = registerSoundEvents("horn_clear_harmony");
    public static final RegistryObject<SoundEvent> HORN_CLEAR_MELODY = registerSoundEvents("horn_clear_melody");
    public static final RegistryObject<SoundEvent> HORN_CLEAR_BASS = registerSoundEvents("horn_clear_bass");

    public static final RegistryObject<SoundEvent> HORN_FRESH_HARMONY = registerSoundEvents("horn_fresh_harmony");
    public static final RegistryObject<SoundEvent> HORN_FRESH_MELODY = registerSoundEvents("horn_fresh_melody");
    public static final RegistryObject<SoundEvent> HORN_FRESH_BASS = registerSoundEvents("horn_fresh_bass");

    public static final RegistryObject<SoundEvent> HORN_SECRET_HARMONY = registerSoundEvents("horn_secret_harmony");
    public static final RegistryObject<SoundEvent> HORN_SECRET_MELODY = registerSoundEvents("horn_secret_melody");
    public static final RegistryObject<SoundEvent> HORN_SECRET_BASS = registerSoundEvents("horn_secret_bass");

    public static final RegistryObject<SoundEvent> HORN_FEARLESS_HARMONY = registerSoundEvents("horn_fearless_harmony");
    public static final RegistryObject<SoundEvent> HORN_FEARLESS_MELODY = registerSoundEvents("horn_fearless_melody");
    public static final RegistryObject<SoundEvent> HORN_FEARLESS_BASS = registerSoundEvents("horn_fearless_bass");

    public static final RegistryObject<SoundEvent> HORN_SWEET_HARMONY = registerSoundEvents("horn_sweet_harmony");
    public static final RegistryObject<SoundEvent> HORN_SWEET_MELODY = registerSoundEvents("horn_sweet_melody");
    public static final RegistryObject<SoundEvent> HORN_SWEET_BASS = registerSoundEvents("horn_sweet_bass");

    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
