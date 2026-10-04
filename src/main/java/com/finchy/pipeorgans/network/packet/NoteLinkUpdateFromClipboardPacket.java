package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.network.AllPackets;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public record NoteLinkUpdateFromClipboardPacket(BlockPos pos, CompoundTag tag, boolean copyMode) implements ServerboundPacketPayload {

    public static final StreamCodec<FriendlyByteBuf, NoteLinkUpdateFromClipboardPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, NoteLinkUpdateFromClipboardPacket::pos,
            ByteBufCodecs.COMPOUND_TAG, NoteLinkUpdateFromClipboardPacket::tag,
            ByteBufCodecs.BOOL, NoteLinkUpdateFromClipboardPacket::copyMode,
            NoteLinkUpdateFromClipboardPacket::new
    );

    @Override
    public void handle(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        Optional<NoteLinkBlockEntity> obe = level.getBlockEntity(pos, AllBlockEntities.NOTE_LINK_BLOCK_ENTITY.get());
        obe.ifPresent(be -> {});
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.NOTE_LINK_UPDATE_FROM_CLIPBOARD;
    }
}
