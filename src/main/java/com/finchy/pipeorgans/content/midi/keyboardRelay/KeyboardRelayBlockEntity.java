package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.content.midi.MidiSourceBehaviour;
import com.finchy.pipeorgans.init.AllSoundEvents;
import com.finchy.pipeorgans.util.MidiUtils;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.Nullable;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import java.util.List;
import java.util.UUID;

@SuppressWarnings({"DataFlowIssue", "NullableProblems"})
public class KeyboardRelayBlockEntity extends SmartBlockEntity implements MenuProvider {

    private UUID user = null;
    private boolean deactivatedThisTick;

    MidiSourceBehaviour midiSourceBehaviour;

    public KeyboardRelayBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(midiSourceBehaviour = new MidiSourceBehaviour(this));
    }

    public void onBlockRemoved() {
        midiSourceBehaviour.link.stopAllNotes();
        if (user != null) {
            Entity playerEntity = ((ServerLevel) this.level).getEntity(this.user);
            if (playerEntity instanceof Player player)
                player.getPersistentData().remove("UsingKBRelayPos");
            user = null;
        }
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        midiSourceBehaviour.write(tag, clientPacket);
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        midiSourceBehaviour.read(tag, clientPacket);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("gui.pipeorgans.keyboard_relay");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return KeyboardRelayMenu.create(pContainerId, pPlayerInventory, this);
    }

    public void tryStartUsing(Player player) {
        // Clear stale persistent data if the stored position no longer points to a KBR that knows this player
        if (playerIsUsing(player)) {
            BlockPos storedPos = playerUsingKBRPos(player);
            boolean stale = storedPos == null;
            if (!stale) {
                if (!(level.getBlockEntity(storedPos) instanceof KeyboardRelayBlockEntity otherKbr)
                        || !otherKbr.isUsedBy(player))
                    stale = true;
            }
            if (stale)
                player.getPersistentData().remove("UsingKBRelayPos");
        }
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

        level.setBlock(worldPosition, getBlockState().setValue(KeyboardRelayBlock.ACTIVE, true), 3);
        playOpenSound(level, getBlockPos());
        notifyUpdate();
    }


    private void stopUsing(Player player) {
        user = null;

        if (player != null)
            player.getPersistentData().remove("UsingKBRelayPos");

        level.setBlock(worldPosition, getBlockState().setValue(KeyboardRelayBlock.ACTIVE, false), 3);
        level.setBlock(worldPosition, getBlockState().setValue(KeyboardRelayBlock.TRANSMITTING, false), 3);

        deactivatedThisTick = true;
        midiSourceBehaviour.link.stopAllNotes();
        playCloseSound(level, getBlockPos());
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

    public void handleMidiMessage(MidiMessage mm) {
        if (mm instanceof ShortMessage sm && (MidiUtils.isNoteOn(sm) || MidiUtils.isNoteOff(sm))) {
            midiSourceBehaviour.handleNote(sm);
        }
    }

    public static boolean playerInRange(Player player, Level world, BlockPos pos) {
        if (player.level() != world) {
            return false;
        }
        return player.distanceToSqr(Vec3.atCenterOf(pos)) < Math.pow(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()), 2);
    }

    private void playOpenSound (Level pLevel, BlockPos pPos){
        SoundEvent openSound;
        openSound = AllSoundEvents.KBR_OPEN.get();
        pLevel.playSound(null, pPos, openSound, SoundSource.BLOCKS, 0.5f, 1f);
    }

    private void playCloseSound (Level pLevel, BlockPos pPos){
        SoundEvent openSound;
        openSound = AllSoundEvents.KBR_CLOSE.get();
        pLevel.playSound(null, pPos, openSound, SoundSource.BLOCKS, 0.5f, 1f);
    }

}
