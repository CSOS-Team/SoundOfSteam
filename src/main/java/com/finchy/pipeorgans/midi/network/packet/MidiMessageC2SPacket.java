package com.finchy.pipeorgans.midi.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.midi.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.midi.network.CustomPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class MidiMessageC2SPacket implements CustomPacketPayload {
    public static final ResourceLocation ID = PipeOrgans.asResource(MidiMessageC2SPacket.class.getSimpleName().toLowerCase());
    
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

    @Override
    public ResourceLocation id() {
        return ID;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        encodePacket(this, buf);
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
            assert player != null;
            ServerLevel level = (ServerLevel) player.level();
            if (KeyboardRelayBlockEntity.playerIsUsing(player)) {
                BlockPos pos = KeyboardRelayBlockEntity.playerUsingKBRPos(player);
                if (level.getBlockEntity(pos) instanceof KeyboardRelayBlockEntity kbr
                        && kbr.isUsedBy(player)) {
                    player.sendSystemMessage(Component.literal(""+pos));
                }
            }

        });
    }
}
