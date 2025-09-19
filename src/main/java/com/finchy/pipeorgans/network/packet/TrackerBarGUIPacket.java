package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarMenu;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class TrackerBarGUIPacket extends SimplePacketBase {

    private String button;
    private BlockPos pos;

    public TrackerBarGUIPacket(String button, BlockPos pos) {
        this.button = button;
        this.pos = pos;
    }

    public TrackerBarGUIPacket(FriendlyByteBuf buffer) {
        this(buffer.readUtf(), buffer.readBlockPos());
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUtf(button);
        buffer.writeBlockPos(pos);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.serverLevel();

            if (player == null || !(player.containerMenu instanceof TrackerBarMenu))
                return;

            if (level.getBlockEntity(pos) instanceof TrackerBarBlockEntity be) {
                if (button.equals("play")) {
                    be.pressTogglePlayButton();
                } else if (button.equals("stop")) {
                    be.pressStopButton();
                }
            }
        });
        return true;
    }
}
