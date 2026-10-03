package com.finchy.pipeorgans.compat.create_connected;


import net.minecraft.world.level.block.state.BlockState;

public class CreateConnectedCompat {

    public static boolean isFluidVessel(BlockState state) {
        return state.getBlock().getClass().getName()
                .equals("com.hlysine.create_connected.content.fluidvessel.FluidVesselBlock");
    }
}
