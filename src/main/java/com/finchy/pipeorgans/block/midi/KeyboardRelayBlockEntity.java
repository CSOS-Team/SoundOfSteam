package com.finchy.pipeorgans.block.midi;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.List;
import java.util.UUID;

public class KeyboardRelayBlockEntity extends SmartBlockEntity {

    private UUID user;
    private boolean deactivatedThisTick;

    public KeyboardRelayBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.KEYBOARD_RELAY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void tryStartUsing(Player player) {
        PipeOrgans.LOGGER.error("TRYSTART");
        if (!deactivatedThisTick && !hasUser() && !playerIsUsing(player) && playerInRange(player, level, worldPosition)) {
            startUsing(player);
        }
    }

    public void tryStopUsing(Player player) {
        if (isUsedBy(player)) {
            stopUsing(player);
        }
    }

    private void startUsing(Player player) {
        PipeOrgans.LOGGER.error("START");
        user = player.getUUID();
        player.getPersistentData().putBoolean("IsUsingKeyboardRelay", true);
        player.getPersistentData().putIntArray("UsingKBRelayPos", new int[]{worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()});
        sendData();
        player.sendSystemMessage(Component.literal("STARTED USING"));
    }

    private void stopUsing(Player player) {
        user = null;
        if (player != null) {
            player.getPersistentData().remove("IsUsingKeyboardRelay");
            player.getPersistentData().remove("UsingKBRelayPos");
        }
        deactivatedThisTick = true;
        sendData();
        player.sendSystemMessage(Component.literal("STOPPED USING"));
    }

    public static boolean playerIsUsing(Player player) {
        return player.getPersistentData().contains("IsUsingKeyboardRelay");
    }

    public static BlockPos playerUsingKBRPos(Player player) {
        int[] pos = player.getPersistentData().getIntArray("UsingKBRelayPos");
        return new BlockPos(pos[0], pos[1], pos[2]);
    }

    public boolean isUsedBy(Player player) {
        return hasUser() && user.equals(player.getUUID());
    }

    public boolean hasUser() {
        return user != null;
    }

    @Override
    public void tick() {
        if (!level.isClientSide) { // serverside only
            deactivatedThisTick = false;
            if (!(level instanceof ServerLevel) || user==null) { // only executing on server level, and if no valid user
                return;
            }

            Entity entity = ((ServerLevel) level).getEntity(user);
            if (!(entity instanceof Player player)) { // if user is somehow not a player
                stopUsing(null);
                return;
            }

            if (!playerInRange(player, level, worldPosition) || !playerIsUsing(player)) { // if user no longer in range... or if they stopped using it without stopping using it?
                stopUsing(player);
            }
        }
    }

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        double reach = 0.4 * player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
    }
}
