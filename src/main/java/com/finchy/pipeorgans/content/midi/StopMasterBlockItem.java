package com.finchy.pipeorgans.content.midi;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

public class StopMasterBlockItem extends BlockItem {

    public StopMasterBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if (pContext.getLevel().isClientSide) {
            return InteractionResult.PASS;
        }
        pContext.getPlayer().sendSystemMessage(Component.literal("LINKED"));
        Level level = pContext.getLevel();
        BlockPos pos = pContext.getClickedPos();
        ItemStack stack = pContext.getItemInHand();

        if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity) { // todo: substitute KBR usages for generic midi source
            CompoundTag tag = stack.getOrCreateTag();
            tag.putIntArray("midi_source_pos",
                    new int[]{pos.getX(), pos.getY(), pos.getZ()} // store midi source coords
            );
            return InteractionResult.SUCCESS;
        } else {
            return super.useOn(pContext);
        }
    }

}
