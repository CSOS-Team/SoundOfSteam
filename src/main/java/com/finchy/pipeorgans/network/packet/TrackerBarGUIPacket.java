package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarMenu;
import com.finchy.pipeorgans.network.AllPackets;
import io.netty.buffer.ByteBuf;
import net.createmod.catnip.codecs.stream.CatnipStreamCodecBuilders;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public record TrackerBarGUIPacket(String button, BlockPos pos) implements ServerboundPacketPayload {

    public static final StreamCodec<ByteBuf, TrackerBarGUIPacket> STREAM_CODEC = StreamCodec.composite(
            CatnipStreamCodecBuilders.nullable(ByteBufCodecs.stringUtf8(256)), TrackerBarGUIPacket::button,
            BlockPos.STREAM_CODEC, TrackerBarGUIPacket::pos,
            TrackerBarGUIPacket::new
    );

    @Override
    public void handle(ServerPlayer player) {
        ServerLevel level = player.serverLevel();

        if (!(player.containerMenu instanceof TrackerBarMenu))
            return;

        if (level.getBlockEntity(pos) instanceof TrackerBarBlockEntity be) {
            if (button.equals("play")) {
                be.pressTogglePlayButton();
            } else if (button.equals("stop")) {
                be.pressStopButton();
            }
        }
    }

    @Override
    public PacketTypeProvider getTypeProvider() {
        return AllPackets.TRACKER_BAR_GUI;
    }
}
