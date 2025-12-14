package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.util.PipePitch;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

public class MusicalLinkUpdatePacket extends SimplePacketBase {

    protected ItemStack key;
    protected PipePitch pitch;

    public MusicalLinkUpdatePacket(ItemStack key, PipePitch pitch) {
        this.key = key;
        this.pitch = pitch;
    }

    public MusicalLinkUpdatePacket(FriendlyByteBuf buffer) {
        this.key = buffer.readItem();
        this.pitch = PipePitch.fromNormalizedName(buffer.readUtf(8));
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeItem(key);
        buffer.writeUtf(pitch.getNormalizedName(), 8);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();
        });
        return true;
    }
}
