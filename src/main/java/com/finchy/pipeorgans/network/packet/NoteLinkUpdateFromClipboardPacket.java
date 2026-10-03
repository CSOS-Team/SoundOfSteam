package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;

public class NoteLinkUpdateFromClipboardPacket extends SimplePacketBase {

    protected final BlockPos pos;
    protected CompoundTag tag;
    protected boolean copyMode;

    public NoteLinkUpdateFromClipboardPacket(BlockPos pos, CompoundTag tag, boolean copyMode) {
        this.pos = pos;
        this.tag = tag;
        this.copyMode = copyMode;
    }

    public NoteLinkUpdateFromClipboardPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.tag = buffer.readAnySizeNbt();
        this.copyMode = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeNbt(tag);
        buffer.writeBoolean(copyMode);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            Optional<NoteLinkBlockEntity> obe = level.getBlockEntity(pos, AllBlockEntities.NOTE_LINK_BLOCK_ENTITY.get());
            obe.ifPresent(be -> be.applyClipboardSettings(tag, copyMode));
        });
        return true;
    }
}
