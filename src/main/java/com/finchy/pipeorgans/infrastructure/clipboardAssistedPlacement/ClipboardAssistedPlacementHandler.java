package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.finchy.pipeorgans.ClientConfig;
import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.ServerConfig;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBehaviour;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlock;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllItems;
import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.ClipboardAssistedPlacementPacket;
import com.finchy.pipeorgans.network.packet.NoteLinkUpdateFromClipboardPacket;
import com.finchy.pipeorgans.util.PipePitch;
import com.mojang.datafixers.TypeRewriteRule;
import com.simibubi.create.content.equipment.clipboard.ClipboardEditPacket;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkDirection;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

@Mod.EventBusSubscriber
public class ClipboardAssistedPlacementHandler {
    protected static final Logger logger = PipeOrgans.LOGGER;

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getLevel() instanceof ServerLevel level)) return; // ensures server-side and actual Level (i.e. not in world-gen). Ideally it would be client-side, but it seems that EntityPlaceEvent is only ever fired server-side.
        if (!ServerConfig.clipboardAssistedPlacementEnabled) return; // ensure clipboard assisted placement is disabled on server

        BlockPos pos = event.getPos();
        Entity entity = event.getEntity();

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof NoteLinkBlockEntity noteLinkBE)) return; // ensure a note link was just placed

        if (!(entity instanceof ServerPlayer player)) return; // return if a zombie somehow placed a note link
        if (!player.getMainHandItem().is(AllBlocks.NOTE_LINK.asItem())) return; // return if the player isn't holding a note link in main hand (for some reason) (apparently wrenching a note link can also trigger this event)

        boolean playerIsShifting = player.isShiftKeyDown();

        ItemStack offhand = player.getOffhandItem(); // get item in offhand
        if (!offhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem())) return; // return if player isn't holding a clipboard in offhand

        AllPackets.getChannel().sendTo(
                new ClipboardAssistedPlacementPacket(
                        pos,
                        offhand,
                        playerIsShifting ? CAPDirection.BACKWARD : CAPDirection.FORWARD
                ),
                player.connection.connection,
                NetworkDirection.PLAY_TO_CLIENT
        );

        /*
        ClipboardAssistedPlacementData data = ClipboardAssistedPlacementData.fromClipboardItem(offhand, level);
        BlockPos srcPos = data.getPosition(be);

        if (pos.equals(srcPos)) {
            data.removePosition(be);
            data.writeToClipboardItem(offhand, level);
            return;
        }

        ClipboardAssistedPlacement.MutationResult result = ClipboardAssistedPlacement.MutationResult.FAILURE_REPLACE;

        if (srcPos != null && !player.isShiftKeyDown()) {
            logger.debug("Source position found in clipboard data: {}", srcPos);
            ClipboardAssistedPlacementBehaviour source = BlockEntityBehaviour.get(level, srcPos, ClipboardAssistedPlacementBehaviour.TYPE);
            if (source != null) {
                logger.debug("Source block entity found at source position for block {}", source.blockEntity.getBlockState().getBlock().getName().getString());
                result = target.applyPlacementMutation(source.blockEntity);
                logger.debug("Applied placement mutation from source to target");
            }
        }

        if (result.srcAction() == ClipboardAssistedPlacement.SourceAction.REPLACE) {
            data.setPosition(be, pos);
            data.writeToClipboardItem(offhand, level);
            logger.debug("Updated clipboard data with new position {}", pos);
        } else if (result.srcAction() == ClipboardAssistedPlacement.SourceAction.REMOVE) {
            data.removePosition(be);
            data.writeToClipboardItem(offhand, level);
            logger.debug("Removed position from clipboard data");
        }

         */
    }

    // Checked handling of clipboard assisted placement for note links. Only called when both the server and client allow it
    // Return true to indicate that the clipboard should be updated (packet sent to server) (this also applies the visual override), false to leave it unchanged.
    public static boolean handleClipboardAssistedPlacement(BlockPos pos, ItemStack clipboardItemStack, CAPDirection direction, boolean copyMode) {

        if (clipboardItemStack.hasTag() &&
                clipboardItemStack.getTag().contains("CopiedValues") &&
                clipboardItemStack.getTagElement("CopiedValues").contains("MusicalFrequency")) { // if the clipboard has anything specifically about note links in its NBT

            CompoundTag musicalFreqTag = clipboardItemStack.getTagElement("CopiedValues").getCompound("MusicalFrequency"); // get the NBT data relating to note links
            PipePitch pitch = PipePitch.fromNormalizedName(musicalFreqTag.getString("Pitch"));
            PipePitch next = direction.map(pitch.next(), pitch.prev()); // get the pitch above what's written on the clipboard
            if (next == null) next = PipePitch.HIGHEST; // if it's the maximum pitch, just stay at the maximum
            musicalFreqTag.putString("Pitch", next.getNormalizedName()); // put the new pitch onto the clipboard

            AllPackets.getChannel().sendToServer(new NoteLinkUpdateFromClipboardPacket(pos, musicalFreqTag, copyMode)); // send a packet to the server to update the note link's settings


            return true;
        } else { // if the clipboard hasn't been used for note links previously

            CompoundTag copiedTag = new CompoundTag(); // make a new tag to put in "CopiedValues"
            CompoundTag noteLinkTag = new CompoundTag(); // make a new tag to put in "MusicalFrequency"
            noteLinkTag.putString("Pitch", PipePitch.LOWEST.getNormalizedName()); // default to F#-1
            noteLinkTag.put("Key", ItemStack.EMPTY.serializeNBT()); // default to no key item
            if (copyMode) noteLinkTag.putBoolean("Receiver", false);

            copiedTag.put("MusicalFrequency", noteLinkTag);
            clipboardItemStack.getOrCreateTag().put("CopiedValues", copiedTag); // apply the tags to the clipboard
            return true;
        }
    }
}
