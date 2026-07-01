package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.content.console.OrganConsoleBlockEntity;
import com.finchy.pipeorgans.content.console.OrganConsoleMenu;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;


public class OrganConsoleNotePacket extends SimplePacketBase {

    private final BlockPos pos;
    private final int section;
    private final int key;
    private final boolean on;
    private final int velocity;

    public OrganConsoleNotePacket(BlockPos pos, int section, int key, boolean on, int velocity) {
        this.pos = pos;
        this.section = section;
        this.key = key;
        this.on = on;
        this.velocity = velocity;
    }

    public OrganConsoleNotePacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.section = buffer.readVarInt();
        this.key = buffer.readVarInt();
        this.on = buffer.readBoolean();
        this.velocity = buffer.readVarInt();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeVarInt(section);
        buffer.writeVarInt(key);
        buffer.writeBoolean(on);
        buffer.writeVarInt(velocity);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null || !(player.containerMenu instanceof OrganConsoleMenu menu))
                return;

            ServerLevel level = player.serverLevel();
            if (!(level.getBlockEntity(pos) instanceof OrganConsoleBlockEntity console))
                return;

            // Make sure the player actually has  this console's menu open.
            if (menu.getConsole() != console)
                return;

            console.handleGuiNote(player, section, key, on, velocity);
        });
        return true;
    }
}
