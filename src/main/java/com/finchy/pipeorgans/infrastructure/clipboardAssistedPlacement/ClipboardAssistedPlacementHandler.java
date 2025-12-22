package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod.EventBusSubscriber
public class ClipboardAssistedPlacementHandler {
    protected static final Logger logger = PipeOrgans.LOGGER;

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return; // ensures server-side and actual Level (i.e. not in world-gen)
        BlockPos pos = event.getPos();
        Entity entity = event.getEntity();

        if (!(entity instanceof Player player)) return;

        logger.debug("Block placed at {} by {}", pos, player.getName().getString());

        ItemStack offhand = player.getOffhandItem();
        if (!offhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem())) return;

        logger.debug("Player is holding a clipboard in offhand");

        BlockEntity be = level.getBlockEntity(pos);
        if (be == null) return;

        logger.debug("Block entity found at placed position for block {}", be.getBlockState().getBlock().getName().getString());

        ClipboardAssistedPlacementBehaviour target = BlockEntityBehaviour.get(be, ClipboardAssistedPlacementBehaviour.TYPE);
        if (target == null) return;

        logger.debug("Target block entity has ClipboardAssistedPlacementBehaviour");

        ClipboardAssistedPlacementData data = ClipboardAssistedPlacementData.fromClipboardItem(offhand, level);
        BlockPos srcPos = data.getPosition(be);

        if (srcPos != null) {
            logger.debug("Source position found in clipboard data: {}", srcPos);
            ClipboardAssistedPlacementBehaviour source = BlockEntityBehaviour.get(level, srcPos, ClipboardAssistedPlacementBehaviour.TYPE);
            if (source != null) {
                logger.debug("Source block entity found at source position for block {}", source.blockEntity.getBlockState().getBlock().getName().getString());
                target.applyPlacementMutation(source);
                logger.debug("Applied placement mutation from source to target");
            }
        }

        data.setPosition(be, pos);
        data.writeToClipboardItem(offhand, level);
        logger.debug("Updated clipboard data with new position {}", pos);
    }
}
