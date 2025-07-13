package com.finchy.pipeorgans.content.pipes.generic;

import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class PipeGenerator extends SpecialBlockStateGen {
    @Override
    protected int getXRotation(BlockState state) {
        return 0;
    }

    @Override
    protected int getYRotation(BlockState state) {
        return horizontalAngle(state.getValue(GenericPipeBlock.FACING));
    }

    @Override
    public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
        String wall = state.getValue(GenericPipeBlock.WALL) ? "wall" : "floor";
        String size = state.getValue(GenericPipeBlock.SIZE).getSerializedName();
        boolean powered = state.getValue(GenericPipeBlock.POWERED);
        ModelFile model = partialPipeModel(ctx, prov, size, wall);
        if (!powered)
            return model;
        ResourceLocation parentLocation = model.getLocation();
        return prov.models()
                .withExistingParent(parentLocation.getPath() + "_powered", parentLocation)
                .texture("2", "pipeorgans:block/copper_redstone_plate_powered");
    }

    public static ModelFile partialPipeModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String... suffix) {
        String string = "/"+ctx.getName();
        for (String suf : suffix)
            if (!suf.isEmpty())
                string += "_" + suf;
        final String location = "block/"+ctx.getName()+string;
        return prov.models().getExistingFile(prov.modLoc(location));
    }
}
