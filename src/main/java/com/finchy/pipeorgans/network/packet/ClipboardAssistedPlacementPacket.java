package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.ClientConfig;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.CAPDirection;
import com.finchy.pipeorgans.infrastructure.clipboardAssistedPlacement.ClipboardAssistedPlacementHandler;
import com.simibubi.create.AllPackets;
import com.simibubi.create.content.equipment.clipboard.ClipboardEditPacket;
import com.simibubi.create.content.equipment.clipboard.ClipboardOverrides;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ClipboardAssistedPlacementPacket extends SimplePacketBase {
    protected BlockPos pos;
    protected ItemStack clipboardItemStack; // the clipboard item stack
    protected CAPDirection direction;

    // When sending to the client, FORWARD is assumed to be the default direction. The packet will adjust based on client config when received.
    public ClipboardAssistedPlacementPacket(BlockPos pos, ItemStack clipboardItemStack, CAPDirection direction) {
        this.pos = pos;
        this.clipboardItemStack = clipboardItemStack;
        this.direction = direction;
    }

    public ClipboardAssistedPlacementPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.clipboardItemStack = buffer.readItem();
        this.direction = buffer.readBoolean() ? CAPDirection.FORWARD : CAPDirection.BACKWARD;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeItem(clipboardItemStack);
        buffer.writeBoolean(direction == CAPDirection.FORWARD);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            switch (context.getDirection().getReceptionSide()) {
                case CLIENT -> {
                    // Handle client-side logic here if needed
                    ClientConfig.syncFromFile();
                    if (!ClientConfig.capEnabled) return; // if CAP is disabled, do nothing

                    this.direction = (this.direction == CAPDirection.FORWARD) ? ClientConfig.capDefaultDirection : ClientConfig.capDefaultDirection.opposite();

                    boolean changed = ClipboardAssistedPlacementHandler.handleClipboardAssistedPlacement(
                            pos,
                            clipboardItemStack,
                            direction,
                            ClientConfig.capCopyMode
                    );

                    if (changed) {
                        ClipboardOverrides.switchTo(ClipboardOverrides.ClipboardType.WRITTEN, clipboardItemStack);
                        AllPackets.getChannel().sendToServer(new ClipboardEditPacket(40, clipboardItemStack.getTag(), null));
                    }
                }
            }
        });
        return true;
    }
}
