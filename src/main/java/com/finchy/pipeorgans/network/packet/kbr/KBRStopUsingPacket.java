package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

public class KBRStopUsingPacket extends KBRPacketBase {
    
    public KBRStopUsingPacket(BlockPos KBRPos) {
        super(KBRPos);
    }
    
    public KBRStopUsingPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr) {
        kbr.tryStopUsing(player);
    }
}
