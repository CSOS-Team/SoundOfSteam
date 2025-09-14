package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.MidiSourceBlockEntity;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("DataFlowIssue")
public class KeyboardRelayBlockEntity extends MidiSourceBlockEntity {

    private UUID user = null;
    private boolean deactivatedThisTick;

    public KeyboardRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    public void onBlockRemoved() {
        Entity playerEntity = ((ServerLevel)this.level).getEntity(this.user);
        if (playerEntity instanceof Player) {
            tryStopUsing((Player)playerEntity);
        }
        // removeFromAllStopMasters();
    }

    public void tryStartUsing(Player player) {
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
        user = player.getUUID();
        player.getPersistentData().putIntArray("UsingKBRelayPos", new int[]{worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()});

        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false), 3); //  turn power off
        notifyUpdate();
    }

    private void stopUsing(Player player) {
        user = null;
        if (player != null) {
            player.getPersistentData().remove("UsingKBRelayPos");
        }
        level.setBlock(worldPosition, getBlockState().setValue(BlockStateProperties.POWERED, false), 3); //  turn power off
        deactivatedThisTick = true;
        notifyUpdate();
    }

    public static boolean playerIsUsing(Player player) {
        return player.getPersistentData().contains("UsingKBRelayPos");
    }

    public static BlockPos playerUsingKBRPos(Player player) {
        if (player.getPersistentData().contains("UsingKBRelayPos")) {
            int[] pos = player.getPersistentData().getIntArray("UsingKBRelayPos");
            return new BlockPos(pos[0], pos[1], pos[2]);
        }
        return null;
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

    @Override
    public void handleMidiMessage(MidiMessage mm) {
        if (mm instanceof ShortMessage sm && (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm))) {
            handleNote(sm);
            PipeOrgans.LOGGER.info("HANDLED");
        }
    }

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        if (player.level() != world) {
            return false;
        }
        double reach = 0.4 * player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
    }
}
