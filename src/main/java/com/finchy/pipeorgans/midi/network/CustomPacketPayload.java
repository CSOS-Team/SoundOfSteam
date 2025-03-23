package com.finchy.pipeorgans.midi.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface CustomPacketPayload {
    public ResourceLocation id();
    public void write(FriendlyByteBuf buf);
}
