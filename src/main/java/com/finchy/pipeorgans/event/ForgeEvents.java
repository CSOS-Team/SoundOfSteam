package com.finchy.pipeorgans.event;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PipeOrgans.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEvents {

    @SubscribeEvent
    public static void onLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        Player player = event.getEntity();
        Level level = player.level();

        PipeOrgans.LOGGER.info("PLAYER LOGGED OUT: "+player.getName().getString());

        BlockPos pos = KeyboardRelayBlockEntity.playerUsingKBRPos(player); // get pos of KBR being used
        if (pos != null) { // if player is actually using a KBR

            if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity kbr // if there is actually a KBR at that pos
                    && kbr.isUsedBy(player)) { // and that player is using that KBR
                kbr.tryStopUsing(player);
            }
        }

    }
}
