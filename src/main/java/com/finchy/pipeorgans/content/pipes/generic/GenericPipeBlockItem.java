package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GenericPipeBlockItem extends BlockItem {

    String stopSize;

    public GenericPipeBlockItem(Block pBlock, Properties pProperties, String stopSize) {
        super(pBlock, pProperties);
        this.stopSize = stopSize;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        pTooltip.add(Component.translatable("pipeorgans.stopsize."+this.stopSize));
    }
}
