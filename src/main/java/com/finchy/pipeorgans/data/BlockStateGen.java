package com.finchy.pipeorgans.data;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericExtensionBlock;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlock;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.simibubi.create.foundation.data.SpecialBlockStateGen;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import net.createmod.catnip.data.Iterate;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;

public class BlockStateGen {
    
    public static <T extends GenericPipeBlock> NonNullBiConsumer<DataGenContext<Block, T>, RegistrateBlockstateProvider> pipe() {
        return (c, p) -> {
            BlockModelProvider models = p.models();
            MultiPartBlockStateBuilder builder = p.getMultipartBuilder(c.get());

            String name = c.getName();
            String horizontalBasePrefix = c.get().isHorizontal() ? "horizontal_" : "";
            
            for (PipeSize size : PipeSize.values()) {
                String s = size.getSerializedName();
                ModelFile pipe = AssetLookup.partialStandardModel(c, p, s);
                
                for (Direction facing : Direction.Plane.HORIZONTAL) {
                    int yRot = (int) facing.toYRot();
                    
                    builder.part()
                            .modelFile(pipe).rotationY(yRot).addModel()
                            .condition(GenericPipeBlock.SIZE, size)
                            .condition(GenericPipeBlock.FACING, facing)
                            .end();
                    
                    for (boolean wall : Iterate.trueAndFalse) {
                        String w = wall ? "wall" : "floor";
                        String baseName = horizontalBasePrefix + "base_" + s + "_" + w;
                        ModelFile base = models.getExistingFile(p.modLoc(baseName));
                        ModelFile basePowered = models
                                .withExistingParent(baseName + "_powered", p.modLoc(baseName))
                                .texture("1", "pipeorgans:block/copper_redstone_plate_powered");
                        
                        for (boolean powered : Iterate.trueAndFalse) {
                            builder.part()
                                    .modelFile(powered ? basePowered : base).rotationY(yRot).addModel()
                                    .condition(GenericPipeBlock.SIZE, size)
                                    .condition(GenericPipeBlock.FACING, facing)
                                    .condition(GenericPipeBlock.WALL, wall)
                                    .condition(GenericPipeBlock.POWERED, powered)
                                    .end();
                        }
                    }
                }
            }
        };
    }

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
            String powered = state.getValue(GenericPipeBlock.POWERED) ? "powered" : "";
            ModelFile model = AssetLookup.partialStandardModel(ctx, prov, size, wall, powered);
            return model;
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
            String shape = ((GenericExtensionBlock<?>) state.getBlock()).getShapeSerialisedName(state);
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
