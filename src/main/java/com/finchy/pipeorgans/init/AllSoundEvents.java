package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PipeOrgans.MOD_ID);


    // declare sounds here

    public static final RegistryObject<SoundEvent> GEDECKT_HIGH = registerSoundEvents("gedeckt_high");
    public static final RegistryObject<SoundEvent> GEDECKT_MEDIUM = registerSoundEvents("gedeckt_medium");
    public static final RegistryObject<SoundEvent> GEDECKT_LOW = registerSoundEvents("gedeckt_low");
    public static final RegistryObject<SoundEvent> GEDECKT_DEEP = registerSoundEvents("gedeckt_deep");

    public static final RegistryObject<SoundEvent> DIAPASON_HIGH = registerSoundEvents("diapason_high");
    public static final RegistryObject<SoundEvent> DIAPASON_MEDIUM = registerSoundEvents("diapason_medium");
    public static final RegistryObject<SoundEvent> DIAPASON_LOW = registerSoundEvents("diapason_low");
    public static final RegistryObject<SoundEvent> DIAPASON_DEEP = registerSoundEvents("diapason_deep");



    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(PipeOrgans.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}
