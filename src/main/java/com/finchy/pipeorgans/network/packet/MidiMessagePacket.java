package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class MidiMessagePacket extends SimplePacketBase {
    
    public final Byte channel;
    public final Byte note;
    public final Byte velocity;
    public final UUID player;
    public final BlockPos pos;

    public MidiMessagePacket(Byte channel, Byte note, Byte velocity, UUID player, BlockPos pos) {
        this.channel = channel;
        this.note = note;
        this.velocity = velocity;
        this.player = player;
        this.pos = pos;
    }

    public MidiMessagePacket(FriendlyByteBuf buffer) {
        channel = buffer.readByte();
        note = buffer.readByte();
        velocity = buffer.readByte();
        player = buffer.readUUID();
        pos = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeByte(channel);
        buffer.writeByte(note);
        buffer.writeByte(velocity);
        buffer.writeUUID(player);
        buffer.writeBlockPos(pos);
    }

    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {

            ServerPlayer player = context.getSender();
            ServerLevel level = (ServerLevel) player.level();

            BlockPos pos = KeyboardRelayBlockEntity.playerUsingKBRPos(player); // get pos of KBR being used
            if (pos != null) { // if player is actually using a KBR

                if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity kbr // if there is actually a KBR at that pos
                        && kbr.isUsedBy(player)) { // and that player is using that KBR
                    kbr.handleMidiObject(new MidiMessageServerObject(channel, note, velocity)); // send midi data to KBR
                }
            }

        });
        return true;
    }
}