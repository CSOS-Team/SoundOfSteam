package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.content.midi.stopMaster.StopMasterBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class KeyboardRelayBlockEntity extends SmartBlockEntity {

    private UUID user = null;
    private boolean deactivatedThisTick;

    private final List<BlockPos> linkedCoords = new ArrayList<>();

    public KeyboardRelayBlockEntity(BlockPos pos, BlockState state) {
        super(AllBlockEntities.KEYBOARD_RELAY_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {}

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        ListTag coordsList = new ListTag();
        for (BlockPos pos : linkedCoords) { // for every linked position
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX()); // put x/y/z coords in tag
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            coordsList.add(posTag); // add coords to list
        }
        tag.put("linked_coords", coordsList); // add list to NBT

        super.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        ListTag coordsList = tag.getList("linked_coords", Tag.TAG_COMPOUND); // get coords from NBT
        linkedCoords.clear(); // clear this blockentity's current list

        for (int i=0; i<coordsList.size(); i++) { // for every coord in list
            CompoundTag posTag = coordsList.getCompound(i);
            int x = posTag.getInt("x"); // get x/y/z
            int y = posTag.getInt("y");
            int z = posTag.getInt("z");
            linkedCoords.add(new BlockPos(x, y, z)); // add pos to this blockentity's list
        }
        super.read(tag, clientPacket);
    }

    public void handleMidiObject(MidiMessageServerObject mm) {
        // todo: check level and pos when sending
        for (BlockPos pos : linkedCoords) { // for every linked position
            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) { // if stopmaster is at that location
                sm.receiveMidiSignal(mm); // send midi to stopmaster
            }
        }
    }

    public void linkStopMaster(StopMasterBlockEntity be) {
        BlockPos pos = be.getBlockPos(); // get pos of stopmaster
        if (!linkedCoords.contains(pos)) { // if stopmaster has not already been linked
            linkedCoords.add(pos); // add pos to list
        }
        notifyUpdate();
    }

    public void removeStopMaster(StopMasterBlockEntity be) {
        linkedCoords.remove(be.getBlockPos()); // remove pos from list
        notifyUpdate();
    }

    public void removeFromAllStopMasters() {
        for (BlockPos pos : linkedCoords) { // for every linked position
            if (level.getBlockEntity(pos) instanceof StopMasterBlockEntity sm) { // if stopmaster is at that location
                sm.removeSource(); // remove source from stopmaster
            }
        }
    }

    public void onBlockRemoved() {
        Entity playerEntity = ((ServerLevel)this.level).getEntity(this.user);
        if (playerEntity instanceof Player) {
            tryStopUsing((Player)playerEntity);
        }
        removeFromAllStopMasters();
    }

    // todo: if player logs out while using a KBR, set user = null and remove relevant tags from user

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
        player.sendSystemMessage(Component.literal("START"));
        player.getPersistentData().putIntArray("UsingKBRelayPos", new int[]{worldPosition.getX(), worldPosition.getY(), worldPosition.getZ()});
        notifyUpdate();
    }

    private void stopUsing(Player player) {
        user = null;
        if (player != null) {
            player.getPersistentData().remove("UsingKBRelayPos");
            player.sendSystemMessage(Component.literal("STOP"));
        }
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

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        if (player.level() != world) {
            return false;
        }
        double reach = 0.4 * player.getAttributeValue(ForgeMod.BLOCK_REACH.get());
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < reach * reach;
    }

    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();
        BlockPos blockPos = getBlockPos();
        int[] writePos = new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ()};
        tag.putIntArray("pos", writePos);
        return tag;
    }
}
