package com.finchy.pipeorgans.network.packet;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.util.redstoneLinkNetworkDebugging.RedstoneLinkNetworkDebugInfo;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.Objects;

public class RedstoneLinkNetworkDebugInfoPacket extends SimplePacketBase {
    public enum OutputMode {
        CHAT,
        CONSOLE,
        CLIPBOARD
    }

    private final OutputMode outputMode;
    private final RedstoneLinkNetworkDebugInfo serverInfo;

    public RedstoneLinkNetworkDebugInfoPacket(FriendlyByteBuf buffer) {
        this.outputMode = buffer.readEnum(OutputMode.class);
        this.serverInfo = RedstoneLinkNetworkDebugInfo.fromBuffer(buffer);
    }

    public RedstoneLinkNetworkDebugInfoPacket(RedstoneLinkNetworkDebugInfo serverInfo, OutputMode outputMode) {
        this.outputMode = outputMode;
        this.serverInfo = serverInfo;
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(outputMode);
        serverInfo.writeToBuffer(buffer);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleOnClient));
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    private void handleOnClient() {
        LocalPlayer player = Objects.requireNonNull(Minecraft.getInstance().player);
        ClientLevel level = Minecraft.getInstance().level;

        StringBuilder sb = new StringBuilder();
        sb.append("Redstone Link Network Debug Info:\n");
        sb.append("Server-side:\n");
        sb.append(serverInfo.toText());
        sb.append("\n==============================\n");
        sb.append("Client-side:\n");
        RedstoneLinkNetworkDebugInfo clientInfo = RedstoneLinkNetworkDebugInfo.forLevel(level);
        sb.append(clientInfo.toText());

        switch (outputMode) {
            case CHAT -> {
                for (String line : sb.toString().split("\n")) {
                    line = line.replace("\t", "    ");
                    player.sendSystemMessage(Component.literal(line));
                }
            }
            case CONSOLE -> {
                PipeOrgans.LOGGER.debug("Redstone Link Network Debug Info:\n{}", sb);
                player.sendSystemMessage(Component.literal("Redstone Link Network Debug Info logged to console."));
            }
            case CLIPBOARD -> {
                Minecraft.getInstance().keyboardHandler.setClipboard(sb.toString());

                player.sendSystemMessage(Component.literal("Redstone Link Network Debug Info copied to clipboard."));
            }
        }
    }
}
