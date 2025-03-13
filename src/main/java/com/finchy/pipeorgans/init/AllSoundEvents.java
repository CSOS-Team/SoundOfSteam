package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class AllSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PipeOrgans.MOD_ID);


    // declare sounds here

    public static final DeferredHolder<SoundEvent, SoundEvent> GEDECKT_SUPERHIGH = registerSoundEvents("gedeckt_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEDECKT_HIGH = registerSoundEvents("gedeckt_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEDECKT_MEDIUM = registerSoundEvents("gedeckt_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEDECKT_LOW = registerSoundEvents("gedeckt_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> GEDECKT_DEEP = registerSoundEvents("gedeckt_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> DIAPASON_SUPERHIGH = registerSoundEvents("diapason_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIAPASON_HIGH = registerSoundEvents("diapason_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIAPASON_MEDIUM = registerSoundEvents("diapason_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIAPASON_LOW = registerSoundEvents("diapason_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> DIAPASON_DEEP = registerSoundEvents("diapason_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> GAMBA_SUPERHIGH = registerSoundEvents("gamba_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> GAMBA_HIGH = registerSoundEvents("gamba_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> GAMBA_MEDIUM = registerSoundEvents("gamba_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> GAMBA_LOW = registerSoundEvents("gamba_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> GAMBA_DEEP = registerSoundEvents("gamba_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> PICCOLO_SUPERHIGH = registerSoundEvents("piccolo_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> PICCOLO_HIGH = registerSoundEvents("piccolo_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> PICCOLO_MEDIUM = registerSoundEvents("piccolo_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> PICCOLO_LOW = registerSoundEvents("piccolo_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> PICCOLO_DEEP = registerSoundEvents("piccolo_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> SUBBASS_HIGH = registerSoundEvents("subbass_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUBBASS_MEDIUM = registerSoundEvents("subbass_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUBBASS_LOW = registerSoundEvents("subbass_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> SUBBASS_DEEP = registerSoundEvents("subbass_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> TROMPETTE_SUPERHIGH = registerSoundEvents("trompette_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> TROMPETTE_HIGH = registerSoundEvents("trompette_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> TROMPETTE_MEDIUM = registerSoundEvents("trompette_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> TROMPETTE_LOW = registerSoundEvents("trompette_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> TROMPETTE_DEEP = registerSoundEvents("trompette_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> NASARD_SUPERHIGH = registerSoundEvents("nasard_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> NASARD_HIGH = registerSoundEvents("nasard_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> NASARD_MEDIUM = registerSoundEvents("nasard_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> NASARD_LOW = registerSoundEvents("nasard_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> NASARD_DEEP = registerSoundEvents("nasard_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> POSAUNE_HIGH = registerSoundEvents("posaune_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> POSAUNE_MEDIUM = registerSoundEvents("posaune_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> POSAUNE_LOW = registerSoundEvents("posaune_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> POSAUNE_DEEP = registerSoundEvents("posaune_deep");

    public static final DeferredHolder<SoundEvent, SoundEvent> VOX_HUMANA_SUPERHIGH = registerSoundEvents("vox_humana_superhigh");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOX_HUMANA_HIGH = registerSoundEvents("vox_humana_high");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOX_HUMANA_MEDIUM = registerSoundEvents("vox_humana_medium");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOX_HUMANA_LOW = registerSoundEvents("vox_humana_low");
    public static final DeferredHolder<SoundEvent, SoundEvent> VOX_HUMANA_DEEP = registerSoundEvents("vox_humana_deep");



    private static DeferredHolder<SoundEvent, SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
