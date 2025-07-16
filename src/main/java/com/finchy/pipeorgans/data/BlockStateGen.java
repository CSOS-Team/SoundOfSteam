package com.finchy.pipeorgans.data;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
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

public class BlockStateGen {

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
            ModelFile model = AssetLookup.partialStandardModel(ctx, prov, size, wall);
            if (!powered)
                return model;
            ResourceLocation parentLocation = model.getLocation();
            return prov.models()
                    .withExistingParent(parentLocation.getPath() + "_powered", parentLocation)
                    .texture("0", "pipeorgans:block/copper_redstone_plate_powered");
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
            return AssetLookup.partialExtensionModel(ctx, prov, size, shape);
        }
    }

    public static class BaseGenerator extends SpecialBlockStateGen {
        @Override
        protected int getXRotation(BlockState state) {
            return 0;
        }

        @Override
        protected int getYRotation(BlockState state) {
            return horizontalAngle(state.getValue(BaseBlock.FACING));
        }

        @Override
        public <T extends Block> ModelFile getModel(DataGenContext<Block, T> ctx, RegistrateBlockstateProvider prov, BlockState state) {
            String wall = state.getValue(BaseBlock.WALL) ? "wall" : "floor";
            boolean powered = state.getValue(GenericPipeBlock.POWERED);
            ModelFile model = AssetLookup.partialStandardModel(ctx, prov, wall);
            if (!powered)
                return model;
            ResourceLocation parentLocation = model.getLocation();
            return prov.models()
                    .withExistingParent(parentLocation.getPath() + "_powered", parentLocation)
                    .texture("0", "pipeorgans:block/copper_redstone_plate_powered");
        }
    }
}
