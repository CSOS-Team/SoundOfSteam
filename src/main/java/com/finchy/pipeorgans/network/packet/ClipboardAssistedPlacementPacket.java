package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.ClientConfig;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.CAPDirection;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.ClipboardAssistedPlacementHandler;
import com.finchy.pipeorgans.network.AllPackets;
import com.simibubi.create.AllDataComponents;
import com.simibubi.create.content.equipment.clipboard.ClipboardContent;
import com.simibubi.create.content.equipment.clipboard.ClipboardEditPacket;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;

public record ClipboardAssistedPlacementPacket(BlockPos pos, ItemStack clipboardItemStack, CAPDirection direction) implements ClientboundPacketPayload {
    
    public static final StreamCodec<RegistryFriendlyByteBuf, ClipboardAssistedPlacementPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClipboardAssistedPlacementPacket::pos,
            ItemStack.STREAM_CODEC, ClipboardAssistedPlacementPacket::clipboardItemStack,
            CatnipStreamCodecBuilders.ofEnum(CAPDirection.class), ClipboardAssistedPlacementPacket::direction,
            ClipboardAssistedPlacementPacket::new
    );

    @Override
    public void handle(LocalPlayer player) {
        // Handle client-side logic here if needed
        ClientConfig.syncFromFile();
        if (!ClientConfig.capEnabled) return; // if CAP is disabled, do nothing

        CAPDirection newDirection = (direction == CAPDirection.FORWARD) ? ClientConfig.capDefaultDirection : ClientConfig.capDefaultDirection.opposite();

        /*
        boolean changed = ClipboardAssistedPlacementHandler.handleClipboardAssistedPlacement(
                pos,
                clipboardItemStack,
                newDirection,
                ClientConfig.capCopyMode,
                player.level().registryAccess()
        );

        if (changed) {
            ClipboardContent clipboardContent = clipboardItemStack.getOrDefault(AllDataComponents.CLIPBOARD_CONTENT, ClipboardContent.EMPTY);
            clipboardContent = clipboardContent.setType(ClipboardOverrides.ClipboardType.WRITTEN);
            CatnipServices.NETWORK.sendToServer(new ClipboardEditPacket(40, clipboardContent, null));
        }
         */
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.CLIPBOARD_ASSISTED_PLACEMENT;
    }
}
