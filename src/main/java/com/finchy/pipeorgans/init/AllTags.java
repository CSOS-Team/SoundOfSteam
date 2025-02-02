package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class AllTags {

    public static class Blocks {

        public static final TagKey<Block> VALID_WHISTLES = tag("valid_whistles");


        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(PipeOrgans.MOD_ID, name));
        }
    }

}
