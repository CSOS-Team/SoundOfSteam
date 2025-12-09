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

    String stopSize;

    public enum StopSize implements StringRepresentable {
        TWO("2"), TWOANDTWOTHIRDS("223"), FOUR("4"), EIGHT("8"), SIXTEEN("16"), THIRTYTWO("32");

        public final String size;
        StopSize(String size) {
            this.size = size;
        }

        @Override
        public String getSerializedName() {
            return size;
        }
    }

    public GenericPipeBlockItem(Block pBlock, Properties pProperties, StopSize stopSize) {
        super(pBlock, pProperties);
        this.stopSize = stopSize.getSerializedName();
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, TooltipContext context, @NotNull List<Component> pTooltip, @NotNull TooltipFlag pFlag) {
        super.appendHoverText(pStack, context, pTooltip, pFlag);
        pTooltip.add(Component.translatable("pipeorgans.stopsize."+this.stopSize));
    }
}
