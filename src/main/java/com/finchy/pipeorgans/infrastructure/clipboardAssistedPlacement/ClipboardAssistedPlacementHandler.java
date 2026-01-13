package com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
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

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof NoteLinkBlockEntity noteLinkBE)) return; // ensure a note link was just placed

        if (!(entity instanceof Player player)) return; // return if a zombie somehow placed a note link
        boolean playerIsShifting = player.isShiftKeyDown();

        ItemStack offhand = player.getOffhandItem(); // get item in offhand
        if (!offhand.is(com.simibubi.create.AllBlocks.CLIPBOARD.asItem())) return; // return if player isn't holding a clipboard in offhand

        if (offhand.hasTag() &&
                offhand.getTag().contains("CopiedValues") &&
                offhand.getTagElement("CopiedValues").contains("MusicalFrequency")) { // if the clipboard has anything specifically about note links in its NBT

            CompoundTag clipboardTag = offhand.getTagElement("CopiedValues").getCompound("MusicalFrequency"); // get the NBT data relating to note links

            noteLinkBE.applyClipboardSettings(clipboardTag); // change the new note link's settings depending on what's written on the clipboard

            PipePitch next;
            if (playerIsShifting) { // if the player is sneaking, use the pitch below
                next = PipePitch.fromNormalizedName(clipboardTag.getString("Pitch")).prev(); // get the pitch below what's written on the clipboard

            } else { // if the player isn't sneaking, use the pitch above
                next = PipePitch.fromNormalizedName(clipboardTag.getString("Pitch")).next(); // get the pitch above what's written on the clipboard
            }

            clipboardTag.putString("Pitch", next.getNormalizedName()); // put the new pitch onto the clipboard

        } else { // if the clipboard hasn't been used for note links previously
            ClipboardOverrides.switchTo(ClipboardOverrides.ClipboardType.WRITTEN, offhand); // make the clipboard visually look like it's been written in

            CompoundTag copiedTag = new CompoundTag(); // make a new tag to put in "CopiedValues"
            CompoundTag noteLinkTag = new CompoundTag(); // make a new tag to put in "MusicalFrequency"
            noteLinkTag.putString("Pitch", PipePitch.LOWEST.getNormalizedName()); // default to F#-1
            noteLinkTag.put("Key", ItemStack.EMPTY.serializeNBT()); // default to no key item

            copiedTag.put("MusicalFrequency", noteLinkTag);
            offhand.getOrCreateTag().put("CopiedValues", copiedTag); // apply the tags to the clipboard
        }

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
}
