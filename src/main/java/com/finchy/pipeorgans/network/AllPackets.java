package com.finchy.pipeorgans.network;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.network.packet.KBRMidiMessagePacket;
import com.finchy.pipeorgans.network.packet.MidiUploadPacket;
import com.finchy.pipeorgans.network.packet.TrackerBarGUIPacket;
import net.createmod.catnip.net.base.BasePacketPayload;
import net.createmod.catnip.net.base.CatnipPacketRegistry;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import java.util.Locale;

public enum AllPackets implements BasePacketPayload.PacketTypeProvider {

    // client to server
    MIDI_MESSAGE(KBRMidiMessagePacket.class, KBRMidiMessagePacket.STREAM_CODEC),
    MIDI_UPLOAD(MidiUploadPacket.class, MidiUploadPacket.STREAM_CODEC),
    TRACKER_BAR_GUI(TrackerBarGUIPacket.class, TrackerBarGUIPacket.STREAM_CODEC);

    private final CatnipPacketRegistry.PacketType<?> type;

    <T extends BasePacketPayload> AllPackets(Class <T> type, StreamCodec<? super RegistryFriendlyByteBuf, T> codec) {
        String name = this.name().toLowerCase(Locale.ROOT);
        this.type = new CatnipPacketRegistry.PacketType<>(
                new CustomPacketPayload.Type<>(PipeOrgans.asResource(name)), type, codec
        );
    }

    @Override
    public <T extends CustomPacketPayload> CustomPacketPayload.Type<T> getType() {
        return (CustomPacketPayload.Type<T>) this.type.type();
    }

    public static void register() {
        CatnipPacketRegistry packetRegistry = new CatnipPacketRegistry(PipeOrgans.MOD_ID, 1);
        for (AllPackets packet : AllPackets.values()) {
            packetRegistry.registerPacket(packet.type);
        }
        packetRegistry.registerAllPackets();
    }

}
