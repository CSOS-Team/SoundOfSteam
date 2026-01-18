package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.infrastructure.commands.PipeOrgansCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

public class AllCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        PipeOrgansCommand.register(dispatcher);
    }
}
