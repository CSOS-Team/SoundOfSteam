package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class KBRPacketBase implements ServerboundPacketPayload {

    protected final BlockPos KBRPos;

    public KBRPacketBase(BlockPos KBRPos) {
        this.KBRPos = KBRPos;
    }
    
    public BlockPos getKBRPos() {
        return KBRPos;
    }

    @Override
    public void handle(ServerPlayer player) {
        BlockEntity be = player.level().getBlockEntity(KBRPos);
        if (!(be instanceof KeyboardRelayBlockEntity kbr))
            return;

        handleKBR(player, kbr);
    }
    
    protected abstract void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr);
}