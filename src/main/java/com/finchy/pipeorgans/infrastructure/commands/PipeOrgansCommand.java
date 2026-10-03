package com.finchy.pipeorgans.infrastructure.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.createmod.catnip.command.CatnipCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class PipeOrgansCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> root = Commands.literal("pipeorgans")
                .requires(cs -> cs.hasPermission(0))
                .then(ListRedstoneLinkNetworkCommand.register());

        LiteralCommandNode<CommandSourceStack> node = dispatcher.register(root);
        CatnipCommands.createOrAddToShortcut(dispatcher, "csos", node);
    }
}
