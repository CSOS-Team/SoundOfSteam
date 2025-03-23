package com.finchy.pipeorgans.midi.network;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.midi.network.packet.MidiMessageC2SPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static SimpleChannel INSTANCE;

    private static int packetID = 0;
    private static int id() {
        return packetID++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(PipeOrgans.asResource("messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(MidiMessageC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(MidiMessageC2SPacket::decodePacket)
                .encoder(MidiMessageC2SPacket::encodePacket)
                .consumerMainThread(MidiMessageC2SPacket::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer (MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
