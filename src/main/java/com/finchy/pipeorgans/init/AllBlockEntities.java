package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.diapason.DiapasonBlockEntity;
import com.finchy.pipeorgans.block.gamba.GambaBlockEntity;
import com.finchy.pipeorgans.block.gedeckt.GedecktBlockEntity;
import com.finchy.pipeorgans.block.reed.ReedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PipeOrgans.MOD_ID);

    public static final RegistryObject<BlockEntityType<GedecktBlockEntity>> GEDECKT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gedeckt_block_entity",
                    () -> BlockEntityType.Builder.of(GedecktBlockEntity::new, AllBlocks.GEDECKT.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<DiapasonBlockEntity>> DIAPASON_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("diapason_block_entity",
                    () -> BlockEntityType.Builder.of(DiapasonBlockEntity::new, AllBlocks.DIAPASON.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<GambaBlockEntity>> GAMBA_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gamba_block_entity",
                    () -> BlockEntityType.Builder.of(GambaBlockEntity::new, AllBlocks.GAMBA.get())
                            .build(null));

    public static final RegistryObject<BlockEntityType<ReedBlockEntity>> REED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("reed_block_entity",
                    () -> BlockEntityType.Builder.of(ReedBlockEntity::new, AllBlocks.REED.get())
                            .build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
