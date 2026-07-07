package com.finchy.pipeorgans.network.packet.kbr;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.SysexMessage;

public abstract class KBRPacketBase extends SimplePacketBase {

    private BlockPos KBRPos;

    public KBRPacketBase(BlockPos KBRPos) {
        this.KBRPos = KBRPos;
    }

    public KBRPacketBase(FriendlyByteBuf buffer) {
        KBRPos = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeInt(KBRPos.getX());
        buffer.writeInt(KBRPos.getY());
        buffer.writeInt(KBRPos.getZ());
    }

    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null)
                return;

            BlockEntity be = player.level().getBlockEntity(KBRPos);
            if (!(be instanceof KeyboardRelayBlockEntity kbr))
                return;

            handleKBR(player, kbr);
        });
        return true;
    }
    
    protected abstract void handleKBR(ServerPlayer player, KeyboardRelayBlockEntity kbr);
}