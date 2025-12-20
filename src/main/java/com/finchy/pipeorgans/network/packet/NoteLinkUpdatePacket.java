package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.musicalLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.init.AllBlockEntities;
import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.Optional;

public class NoteLinkUpdatePacket extends SimplePacketBase {

    protected BlockPos pos;
    protected ItemStack key;
    protected PipePitch pitch;

    public NoteLinkUpdatePacket(BlockPos pos, ItemStack key, PipePitch pitch) {
        this.pos = pos;
        this.key = key;
        this.pitch = pitch;
    }

    public NoteLinkUpdatePacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.key = buffer.readItem();
        this.pitch = PipePitch.fromNormalizedName(buffer.readUtf(8));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeItem(key);
        buffer.writeUtf(pitch.getNormalizedName(), 8);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
            Optional<NoteLinkBlockEntity> obe = level.getBlockEntity(pos, AllBlockEntities.NOTE_LINK_BLOCK_ENTITY.get());
            if (obe.isPresent()) {
                NoteLinkBlockEntity be = obe.get();
                be.setKey(key);
                be.setPitch(pitch);
            }
        });
        return true;
    }
}
