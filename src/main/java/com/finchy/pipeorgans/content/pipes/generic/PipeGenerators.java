package com.finchy.pipeorgans.content.pipes.generic;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.DoubleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.QuadrupleExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.subtypes.SingleExtensionBlock;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.ModelFile;

public class PipeGenerators {

    public static class PipeGenerator extends SpecialBlockStateGen {
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
                    .texture("0", "pipeorgans:block/copper_redstone_plate_powered");
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

    public static class PipeExtensionGenerator extends SpecialBlockStateGen {
        @Override
        protected int getXRotation(BlockState state) {
            return 0;
        }

        @Override
        protected int getYRotation(BlockState state) {
            return state.hasProperty(GenericExtensionBlock.FACING) ? horizontalAngle(state.getValue(GenericExtensionBlock.FACING)) : 0;
        }

        @Override
        public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
            String size = state.getValue(GenericExtensionBlock.SIZE).getSerializedName();
            String shape;
            if (state.hasProperty(SingleExtensionBlock.SHAPE))
                shape = state.getValue(SingleExtensionBlock.SHAPE).getSerializedName();
            else if (state.hasProperty(DoubleExtensionBlock.SHAPE))
                shape = state.getValue(DoubleExtensionBlock.SHAPE).getSerializedName();
            else if (state.hasProperty(QuadrupleExtensionBlock.SHAPE))
                shape = state.getValue(QuadrupleExtensionBlock.SHAPE).getSerializedName();
            else {
                PipeOrgans.LOGGER.error("Pipe extension {} has no valid shape property", ctx.getName());
                shape = "";
            }
            return partialExtensionModel(ctx, prov, size, shape);
        }

        public static ModelFile partialExtensionModel(DataGenContext<?, ?> ctx, RegistrateBlockstateProvider prov, String... suffix) {
            String pipeName;
            if (ctx.getName().endsWith("_extension"))
                pipeName = ctx.getName().substring(0, ctx.getName().length() - "_extension".length());
            else
                pipeName = ctx.getName();

            String string = "/extension/"+pipeName;
            for (String suf : suffix)
                if (!suf.isEmpty())
                    string += "_" + suf;
            final String location = "block/"+pipeName+string;
            return prov.models().getExistingFile(prov.modLoc(location));
        }
    }
}
