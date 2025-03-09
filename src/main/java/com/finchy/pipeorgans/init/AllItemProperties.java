package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

public class AllItemProperties {

    public static void addItemProperties() {
        registerTootingProperty(AllItems.COPPER_HORN_CLEAR);
        registerTootingProperty(AllItems.COPPER_HORN_DRY);
        registerTootingProperty(AllItems.COPPER_HORN_FEARLESS);
        registerTootingProperty(AllItems.COPPER_HORN_FRESH);
        registerTootingProperty(AllItems.COPPER_HORN_GREAT);
        registerTootingProperty(AllItems.COPPER_HORN_HUMBLE);
        registerTootingProperty(AllItems.COPPER_HORN_OLD);
        registerTootingProperty(AllItems.COPPER_HORN_PURE);
        registerTootingProperty(AllItems.COPPER_HORN_SECRET);
        registerTootingProperty(AllItems.COPPER_HORN_SWEET);
    }


    private static <T extends Item> void registerTootingProperty(RegistryObject<T> item) {
        ItemProperties.register(item.get(), new ResourceLocation(PipeOrgans.MOD_ID, "tooting"), ((stack, level, entity, seed) -> {
            return entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F;
        }));
    }
}
