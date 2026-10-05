package com.finchy.pipeorgans.content.pipes.generic;

import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GenericPipeBlockItem extends BlockItem {

    StopSize stopSize;

    public GenericPipeBlockItem(Block pBlock, Properties pProperties, StopSize stopSize) {
        super(pBlock, pProperties);
        this.stopSize = stopSize;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, TooltipContext context, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, context, pTooltip, pFlag);
        pTooltip.add(Component.translatable("pipeorgans.stopsize."+this.stopSize.getSerializedName()));
    }
}
