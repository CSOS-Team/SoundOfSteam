package com.finchy.pipeorgans.content.midi.stopMaster;

import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class StopMasterBlockItem extends BlockItem {

    public StopMasterBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = pContext.getItemInHand();

        if (level.getBlockEntity(pos) instanceof MidiSourceBlockEntity) {// if clicked on a midi source
            CompoundTag tag = stack.getOrCreateTag();
            tag.putIntArray("midi_source_pos",
                    new int[]{pos.getX(), pos.getY(), pos.getZ()} // store midi source coords
            );
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(pContext);
        }
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
        if (pStack.hasTag()) {
            int[] pos = pStack.getOrCreateTag().getIntArray("midi_source_pos");
            String posString = "(%d, %d, %d)".formatted(pos[0], pos[1], pos[2]);
            pTooltip.add(Component.translatable("item.stop_master.tooltip").append(Component.literal(posString)));
        }
    }
}
