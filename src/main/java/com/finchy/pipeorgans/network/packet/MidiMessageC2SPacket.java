package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.midi.server.MidiMessageServerObject;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MidiMessageC2SPacket {
    
    public final Byte channel;
    public final Byte note;
    public final Byte velocity;
    public final UUID player;
    public final BlockPos pos;

    public static MidiMessageC2SPacket createNotePacket(Byte channel, Byte note, Byte velocity, UUID player, BlockPos pos) {
        return new MidiMessageC2SPacket(channel, note, velocity, player, pos);
    }

    protected MidiMessageC2SPacket(Byte channel, Byte note, Byte velocity, UUID player, BlockPos pos) {
        this.channel = channel;
        this.note = note;
        this.velocity = velocity;
        this.player = player;
        this.pos = pos;
    }

    public static MidiMessageC2SPacket decodePacket(FriendlyByteBuf buf) {
        try {
            byte channel = buf.readByte();
            byte note = buf.readByte();
            byte velocity = buf.readByte();
            UUID player = buf.readUUID();
            BlockPos pos = buf.readBlockPos();

            return new MidiMessageC2SPacket(channel, note, velocity, player, pos);
        } catch (IndexOutOfBoundsException e) {
            PipeOrgans.LOGGER.error("MidiMessagePacket did not contain enough bytes: {}", String.valueOf(e));
            return null;
        }
    }

    public static void encodePacket(MidiMessageC2SPacket packet, FriendlyByteBuf buf) {
        buf.writeByte(packet.channel);
        buf.writeByte(packet.note);
        buf.writeByte(packet.velocity);
        buf.writeUUID(packet.player);
        buf.writeBlockPos(packet.pos);
    }

    public void handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
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
    }
}