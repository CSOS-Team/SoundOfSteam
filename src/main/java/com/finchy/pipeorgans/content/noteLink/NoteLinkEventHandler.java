package com.finchy.pipeorgans.content.noteLink;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class NoteLinkEventHandler {
    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if (player.isSpectator()) return;

        BlockState bs = level.getBlockState(pos);
        if (!(bs.getBlock() instanceof NoteLinkBlock noteLinkBlock)) return;

        if (player.isShiftKeyDown()) {
            if (held.isEmpty()) {
                if (noteLinkBlock.onEmptyHandShiftUse(bs, level, pos, player) == InteractionResult.SUCCESS) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }

    }
}
