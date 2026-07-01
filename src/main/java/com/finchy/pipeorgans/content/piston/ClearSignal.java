package com.finchy.pipeorgans.content.piston;

import com.simibubi.create.content.redstone.link.RedstoneLinkNetworkHandler.Frequency;
import net.createmod.catnip.data.Couple;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

// Broadcast double redstone signal on clear
public final class ClearSignal {

    private ClearSignal() {
    }

    public static Couple<Frequency> networkKey() {
        Frequency redstone = Frequency.of(new ItemStack(Items.REDSTONE));
        return Couple.create(redstone, redstone);
    }
}
