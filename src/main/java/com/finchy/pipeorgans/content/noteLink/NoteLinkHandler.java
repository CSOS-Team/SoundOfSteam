package com.finchy.pipeorgans.content.noteLink;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.RaycastHelper;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.LogicalSide;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class NoteLinkHandler {
    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if (player.isShiftKeyDown() || player.isSpectator())
            return;
        
        NoteLinkBehaviour behaviour = BlockEntityBehaviour.get(level, pos, NoteLinkBehaviour.TYPE);
        if (behaviour == null)
            return;

        BlockHitResult ray = RaycastHelper.rayTraceRange(level, player, 10);
        if (ray == null)
            return;
        if (AllItems.WRENCH.isIn(held))
            return;

        if (behaviour.testHit(ray.getLocation())) {
            if (event.getSide() != LogicalSide.CLIENT)
                behaviour.rightClickKeyFrequency(player, held);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.SUCCESS);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .25f, .1f);
        }

    }
}
