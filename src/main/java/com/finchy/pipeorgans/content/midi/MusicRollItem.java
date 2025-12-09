package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllItems;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MusicRollItem extends Item {

    public MusicRollItem(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack create(String midi, String owner) {
        ItemStack roll = AllItems.MUSIC_ROLL.asStack();

        CompoundTag tag = new CompoundTag();
        tag.putString("Owner", owner);
        tag.putString("File", midi);

        roll.setTag(tag);
        return roll;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.hasTag()) {
            if (pStack.getTag().contains("File")) { // if it contains a file
                pTooltipComponents.add(Component.literal(
                        ChatFormatting.GOLD + pStack.getTag().getString("File") // add the filename in gold
                ));
            }
        } else {
            pTooltipComponents.add(Component.translatable("item.pipeorgans.music_roll.invalid").withStyle(ChatFormatting.RED)); // oops, no file
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
