package com.finchy.pipeorgans.content.traps.zimblestern;

import com.finchy.pipeorgans.init.AllPartialModels;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.state.BlockState;

public class ZimblesternRenderer extends KineticBlockEntityRenderer<ZimblesternBlockEntity> {

    public ZimblesternRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    //TODO Isn't rendering... No idea what I'm doing - Deano
    @Override
    protected SuperByteBuffer getRotatedModel(ZimblesternBlockEntity be, BlockState state) {
        return CachedBuffers.partial(AllPartialModels.ZIMBLE_SPINNYTHING, state);
    }

}