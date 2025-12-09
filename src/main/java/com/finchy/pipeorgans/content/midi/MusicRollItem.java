package com.finchy.pipeorgans.content.midi;

import com.finchy.pipeorgans.init.AllDataComponents;
import com.finchy.pipeorgans.init.AllItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class MusicRollItem extends Item {

    public MusicRollItem(Properties pProperties) {
        super(pProperties);
    }

    public static ItemStack create(String midi, String owner) {
        ItemStack roll = AllItems.MUSIC_ROLL.asStack();

        roll.set(AllDataComponents.MIDI_FILE, midi);
        roll.set(AllDataComponents.MIDI_OWNER, owner);

        return roll;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext context, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (pStack.has(AllDataComponents.MIDI_FILE)) {
            pTooltipComponents.add(Component.literal(
                    ChatFormatting.GOLD + pStack.get(AllDataComponents.MIDI_FILE) // add the filename in gold
            ));
        } else {
            pTooltipComponents.add(Component.translatable("item.pipeorgans.music_roll.invalid").withStyle(ChatFormatting.RED)); // oops, no file
        }
        super.appendHoverText(pStack, context, pTooltipComponents, pIsAdvanced);
    }
}
