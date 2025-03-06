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



    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
