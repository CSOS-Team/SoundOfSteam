package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

public class KBRStopUsingPacket extends KBRPacketBase {

    public static final StreamCodec<ByteBuf, KBRStopUsingPacket> STREAM_CODEC = BlockPos.STREAM_CODEC.map(
            KBRStopUsingPacket::new, KBRPacketBase::getKBRPos
    );
    
    public KBRStopUsingPacket(BlockPos KBRPos) {
        super(KBRPos);
    }

    @Override
    protected void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr) {
        kbr.tryStopUsing(player);
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return null;
    }
}
