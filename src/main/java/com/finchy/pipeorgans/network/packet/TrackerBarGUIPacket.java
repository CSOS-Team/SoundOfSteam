package com.finchy.pipeorgans.network.packet;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class TrackerBarGUIPacket extends SimplePacketBase {



    public TrackerBarGUIPacket() {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        return false;
    }
}
