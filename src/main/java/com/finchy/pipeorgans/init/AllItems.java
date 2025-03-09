package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.item.GenericCopperHornItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, PipeOrgans.MOD_ID);

    public static final RegistryObject<Item> BRASS_BOOT = ITEMS.register("brass_boot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> DARK_OAK_BOOT = ITEMS.register("dark_oak_boot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_BOOT = ITEMS.register("copper_boot",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> COPPER_HORN_GREAT = ITEMS.register("copper_horn_great",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_GREAT_BASS, AllSoundEvents.HORN_GREAT_MELODY, AllSoundEvents.HORN_GREAT_HARMONY,
                    "copper_horn_great.hover"));

    public static final RegistryObject<Item> COPPER_HORN_OLD = ITEMS.register("copper_horn_old",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_OLD_BASS, AllSoundEvents.HORN_OLD_MELODY, AllSoundEvents.HORN_OLD_HARMONY,
                    "copper_horn_old.hover"));

    public static final RegistryObject<Item> COPPER_HORN_PURE = ITEMS.register("copper_horn_pure",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_PURE_BASS, AllSoundEvents.HORN_PURE_MELODY, AllSoundEvents.HORN_PURE_HARMONY,
                    "copper_horn_pure.hover"));

    public static final RegistryObject<Item> COPPER_HORN_HUMBLE = ITEMS.register("copper_horn_humble",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_HUMBLE_BASS, AllSoundEvents.HORN_HUMBLE_MELODY, AllSoundEvents.HORN_HUMBLE_HARMONY,
                    "copper_horn_humble.hover"));

    public static final RegistryObject<Item> COPPER_HORN_DRY = ITEMS.register("copper_horn_dry",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_DRY_BASS, AllSoundEvents.HORN_DRY_MELODY, AllSoundEvents.HORN_DRY_HARMONY,
                    "copper_horn_dry.hover"));

    public static final RegistryObject<Item> COPPER_HORN_CLEAR = ITEMS.register("copper_horn_clear",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_CLEAR_BASS, AllSoundEvents.HORN_CLEAR_MELODY, AllSoundEvents.HORN_CLEAR_HARMONY,
                    "copper_horn_clear.hover"));

    public static final RegistryObject<Item> COPPER_HORN_FRESH = ITEMS.register("copper_horn_fresh",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_FRESH_BASS, AllSoundEvents.HORN_FRESH_MELODY, AllSoundEvents.HORN_FRESH_HARMONY,
                    "copper_horn_fresh.hover"));

    public static final RegistryObject<Item> COPPER_HORN_SECRET = ITEMS.register("copper_horn_secret",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_SECRET_BASS, AllSoundEvents.HORN_SECRET_MELODY, AllSoundEvents.HORN_SECRET_HARMONY,
                    "copper_horn_secret.hover"));

    public static final RegistryObject<Item> COPPER_HORN_FEARLESS = ITEMS.register("copper_horn_fearless",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_FEARLESS_BASS, AllSoundEvents.HORN_FEARLESS_MELODY, AllSoundEvents.HORN_FEARLESS_HARMONY,
                    "copper_horn_fearless.hover"));

    public static final RegistryObject<Item> COPPER_HORN_SWEET = ITEMS.register("copper_horn_sweet",
            () -> new GenericCopperHornItem(new Item.Properties().stacksTo(1),
                    AllSoundEvents.HORN_SWEET_BASS, AllSoundEvents.HORN_SWEET_MELODY, AllSoundEvents.HORN_SWEET_HARMONY,
                    "copper_horn_sweet.hover"));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
