package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayClientHandler;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

public class KeyboardRelayActivePacket extends SimplePacketBase {

    private final BlockPos pos;
    private final boolean active;

    // Used when SENDING
    public KeyboardRelayActivePacket(BlockPos pos, boolean active) {
        this.pos = pos;
        this.active = active;
    }

    // Used when RECEIVING
    public KeyboardRelayActivePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.active = buf.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(active);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(this::handleClient);
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void handleClient() {
        if (active)
            KeyboardRelayClientHandler.activate(pos);
        else
            KeyboardRelayClientHandler.deactivate();
    }
}
