package com.finchy.pipeorgans.content.musicalLink;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class MusicalLinkEventHandler {
    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if (player.isSpectator()) return;

        BlockState bs = level.getBlockState(pos);
        if (!(bs.getBlock() instanceof MusicalLinkBlock musicalLinkBlock)) return;

        if (player.isShiftKeyDown()) {
            if (held.isEmpty()) {
                if (musicalLinkBlock.onEmptyHandShiftUse(bs, level, pos) == InteractionResult.SUCCESS) {
                    event.setCancellationResult(InteractionResult.SUCCESS);
                    event.setCanceled(true);
                }
            }
        }

    }
}
