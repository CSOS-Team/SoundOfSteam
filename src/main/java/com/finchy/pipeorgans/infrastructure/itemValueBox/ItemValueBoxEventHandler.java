package com.finchy.pipeorgans.infrastructure.itemValueBox;

import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.RaycastHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class ItemValueBoxEventHandler {
    @SubscribeEvent
    public static void onBlockActivated(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        Player player = event.getEntity();
        ItemStack held = event.getItemStack();

        if (player.isSpectator() || player.isShiftKeyDown()) return;
        if (AllItems.WRENCH.isIn(held)) return;

        ItemValueBoxBehaviour behaviour = BlockEntityBehaviour.get(level, pos, ItemValueBoxBehaviour.TYPE);
        if (behaviour == null) return;

        BlockHitResult ray = RaycastHelper.rayTraceRange(level, player, 10);
        if (ray == null) return;

        int boxGroup = behaviour.testHit(ray.getLocation());
        if (boxGroup == -1) return;

        var result = behaviour.onInteract(boxGroup, held, player);
        if (result == InteractionResult.SUCCESS) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, .25f, .1f);
        }
    }
}
