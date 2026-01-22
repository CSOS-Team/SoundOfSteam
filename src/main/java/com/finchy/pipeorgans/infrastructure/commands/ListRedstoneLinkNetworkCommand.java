package com.finchy.pipeorgans.infrastructure.commands;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.RedstoneLinkNetworkDebugInfoPacket;
import com.finchy.pipeorgans.util.redstoneLinkNetworkDebugging.RedstoneLinkNetworkDebugInfo;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.PacketDistributor;

public class ListRedstoneLinkNetworkCommand {
    public static ArgumentBuilder<CommandSourceStack,?> register() {
        return Commands.literal("listRedstoneLinkNetwork")
            .requires(cs -> cs.hasPermission(0))
            .then(Commands.literal("all")
//                .then(Commands.argument("output", EnumArgument.enumArgument(RedstoneLinkNetworkDebugInfoPacket.OutputMode.class)))
                .executes(ctx -> {
                        ServerPlayer player = ctx.getSource().getPlayerOrException();
                        ServerLevel level = ctx.getSource().getLevel();
//                        RedstoneLinkNetworkDebugInfoPacket.OutputMode outputMode = ctx.getArgument("output", RedstoneLinkNetworkDebugInfoPacket.OutputMode.class);
                        AllPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new RedstoneLinkNetworkDebugInfoPacket(RedstoneLinkNetworkDebugInfo.forLevel(level), RedstoneLinkNetworkDebugInfoPacket.OutputMode.CHAT));
                        return Command.SINGLE_SUCCESS;
                    }
                )
            );
    }
}
