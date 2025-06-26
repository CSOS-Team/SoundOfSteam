package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.block.WindchestMasterBlockEntity;
import com.finchy.pipeorgans.block.base.BaseBlockEntity;
import com.finchy.pipeorgans.block.pipes.diapason.DiapasonBlockEntity;
import com.finchy.pipeorgans.block.pipes.gamba.GambaBlockEntity;
import com.finchy.pipeorgans.block.pipes.gedeckt.GedecktBlockEntity;
import com.finchy.pipeorgans.block.pipes.nasard.NasardBlockEntity;
import com.finchy.pipeorgans.block.pipes.piccolo.PiccoloBlockEntity;
import com.finchy.pipeorgans.block.pipes.posaune.PosauneBlockEntity;
import com.finchy.pipeorgans.block.pipes.subbass.SubbassBlockEntity;
import com.finchy.pipeorgans.block.pipes.trompette.TrompetteBlockEntity;
import com.finchy.pipeorgans.block.pipes.vox_humana.VoxHumanaBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


public class AllBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PipeOrgans.MOD_ID);


    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BaseBlockEntity>> BASE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("base_block_entity",
                    () -> BlockEntityType.Builder.of(BaseBlockEntity::new, AllBlocks.BASE.get())
                            .build(null));



    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GedecktBlockEntity>> GEDECKT_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gedeckt_block_entity",
                    () -> BlockEntityType.Builder.of(GedecktBlockEntity::new, AllBlocks.GEDECKT.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DiapasonBlockEntity>> DIAPASON_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("diapason_block_entity",
                    () -> BlockEntityType.Builder.of(DiapasonBlockEntity::new, AllBlocks.DIAPASON.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GambaBlockEntity>> GAMBA_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("gamba_block_entity",
                    () -> BlockEntityType.Builder.of(GambaBlockEntity::new, AllBlocks.GAMBA.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PiccoloBlockEntity>> PICCOLO_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("piccolo_block_entity",
                    () -> BlockEntityType.Builder.of(PiccoloBlockEntity::new, AllBlocks.PICCOLO.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SubbassBlockEntity>> SUBBASS_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("subbass_block_entity",
                    () -> BlockEntityType.Builder.of(SubbassBlockEntity::new, AllBlocks.SUBBASS.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<TrompetteBlockEntity>> TROMPETTE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("trompette_block_entity",
                    () -> BlockEntityType.Builder.of(TrompetteBlockEntity::new, AllBlocks.TROMPETTE.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NasardBlockEntity>> NASARD_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("nasard_block_entity",
                    () -> BlockEntityType.Builder.of(NasardBlockEntity::new, AllBlocks.NASARD.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PosauneBlockEntity>> POSAUNE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("posaune_block_entity",
                    () -> BlockEntityType.Builder.of(PosauneBlockEntity::new, AllBlocks.POSAUNE.get())
                            .build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<VoxHumanaBlockEntity>> VOX_HUMANA_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("vox_humana_block_entity",
                    () -> BlockEntityType.Builder.of(VoxHumanaBlockEntity::new, AllBlocks.VOX_HUMANA.get())
                            .build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WindchestMasterBlockEntity>> WINDCHEST_MASTER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("windchest_master_block_entity",
                    () -> BlockEntityType.Builder.of(WindchestMasterBlockEntity::new, AllBlocks.WINDCHEST_MASTER.get())
                            .build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
