package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlockEntity;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarVisual;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonBlockEntity;
import com.finchy.pipeorgans.content.pipes.diapason.DiapasonRenderer;
import com.finchy.pipeorgans.content.pipes.gamba.GambaBlockEntity;
import com.finchy.pipeorgans.content.pipes.gamba.GambaRenderer;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktBlockEntity;
import com.finchy.pipeorgans.content.pipes.gedeckt.GedecktRenderer;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockEntity;
import com.finchy.pipeorgans.content.pipes.hohlflute.HohlfluteBlockEntity;
import com.finchy.pipeorgans.content.pipes.hohlflute.HohlfluteRenderer;
import com.finchy.pipeorgans.content.pipes.nasard.NasardBlockEntity;
import com.finchy.pipeorgans.content.pipes.nasard.NasardRenderer;
import com.finchy.pipeorgans.content.pipes.piccolo.PiccoloBlockEntity;
import com.finchy.pipeorgans.content.pipes.piccolo.PiccoloRenderer;
import com.finchy.pipeorgans.content.pipes.posaune.PosauneBlockEntity;
import com.finchy.pipeorgans.content.pipes.posaune.PosauneRenderer;
import com.finchy.pipeorgans.content.pipes.prestant.PrestantBlockEntity;
import com.finchy.pipeorgans.content.pipes.prestant.PrestantRenderer;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassBlockEntity;
import com.finchy.pipeorgans.content.pipes.subbass.SubbassRenderer;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteBlockEntity;
import com.finchy.pipeorgans.content.pipes.trompette.TrompetteRenderer;
import com.finchy.pipeorgans.content.pipes.viola.ViolaBlockEntity;
import com.finchy.pipeorgans.content.pipes.viola.ViolaRenderer;
import com.finchy.pipeorgans.content.pipes.voxCeleste.VoxCelesteBlockEntity;
import com.finchy.pipeorgans.content.pipes.voxCeleste.VoxCelesteRenderer;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaBlockEntity;
import com.finchy.pipeorgans.content.pipes.voxHumana.VoxHumanaRenderer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;

public class AllBlockEntities {
    private static final CreateRegistrate REGISTRATE = PipeOrgans.registrate();

    public static final BlockEntityEntry<BaseBlockEntity> BASE_BLOCK_ENTITY = REGISTRATE
            .blockEntity("base_block_entity", BaseBlockEntity::new)
            .validBlock(AllBlocks.BASE)
            .register();

    public static final BlockEntityEntry<KeyboardRelayBlockEntity> KEYBOARD_RELAY_BLOCK_ENTITY = REGISTRATE
            .blockEntity("keyboard_relay_block_entity", KeyboardRelayBlockEntity::new)
            .validBlock(AllBlocks.KEYBOARD_RELAY)
            .register();

    public static final BlockEntityEntry<TrackerBarBlockEntity> TRACKER_BAR_BLOCK_ENTITY = REGISTRATE
            .blockEntity("tracker_bar_block_entity", TrackerBarBlockEntity::new)
            .visual(() -> TrackerBarVisual::new)
            .validBlock(AllBlocks.TRACKER_BAR)
            .renderer(() -> KineticBlockEntityRenderer::new)
            .register();



    public static final BlockEntityEntry<DiapasonBlockEntity> DIAPASON_BLOCK_ENTITY = registerPipeBlockEntity(
            "diapason_block_entity",
            DiapasonBlockEntity::new,
            AllBlocks.DIAPASON,
            () -> DiapasonRenderer::new);

    public static final BlockEntityEntry<PrestantBlockEntity> PRESTANT_BLOCK_ENTITY = registerPipeBlockEntity(
            "prestant_block_entity",
            PrestantBlockEntity::new,
            AllBlocks.PRESTANT,
            () -> PrestantRenderer::new);

    public static final BlockEntityEntry<GambaBlockEntity> GAMBA_BLOCK_ENTITY = registerPipeBlockEntity(
            "gamba_block_entity",
            GambaBlockEntity::new,
            AllBlocks.GAMBA,
            () -> GambaRenderer::new);

    public static final BlockEntityEntry<GedecktBlockEntity> GEDECKT_BLOCK_ENTITY = registerPipeBlockEntity(
            "gedeckt_block_entity",
            GedecktBlockEntity::new,
            AllBlocks.GEDECKT,
            () -> GedecktRenderer::new);

    public static final BlockEntityEntry<HohlfluteBlockEntity> HOHLFLUTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "hohlflute_block_entity",
            HohlfluteBlockEntity::new,
            AllBlocks.HOHLFLUTE,
            () -> HohlfluteRenderer::new);

    public static final BlockEntityEntry<NasardBlockEntity> NASARD_BLOCK_ENTITY = registerPipeBlockEntity(
            "nasard_block_entity",
            NasardBlockEntity::new,
            AllBlocks.NASARD,
            () -> NasardRenderer::new);

    public static final BlockEntityEntry<PiccoloBlockEntity> PICCOLO_BLOCK_ENTITY = registerPipeBlockEntity(
            "piccolo_block_entity",
            PiccoloBlockEntity::new,
            AllBlocks.PICCOLO,
            () -> PiccoloRenderer::new);

    public static final BlockEntityEntry<PosauneBlockEntity> POSAUNE_BLOCK_ENTITY = registerPipeBlockEntity(
            "posaune_block_entity",
            PosauneBlockEntity::new,
            AllBlocks.POSAUNE,
            () -> PosauneRenderer::new);

    public static final BlockEntityEntry<SubbassBlockEntity> SUBBASS_BLOCK_ENTITY = registerPipeBlockEntity(
            "subbass_block_entity",
            SubbassBlockEntity::new,
            AllBlocks.SUBBASS,
            () -> SubbassRenderer::new);

    public static final BlockEntityEntry<TrompetteBlockEntity> TROMPETTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "trompette_block_entity",
            TrompetteBlockEntity::new,
            AllBlocks.TROMPETTE,
            () -> TrompetteRenderer::new);

    public static final BlockEntityEntry<ViolaBlockEntity> VIOLA_BLOCK_ENTITY = registerPipeBlockEntity(
            "viola_block_entity",
            ViolaBlockEntity::new,
            AllBlocks.VIOLA,
            () -> ViolaRenderer::new);

    public static final BlockEntityEntry<VoxCelesteBlockEntity> VOX_CELESTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "vox_celeste_block_entity",
            VoxCelesteBlockEntity::new,
            AllBlocks.VOX_CELESTE,
            () -> VoxCelesteRenderer::new);

    public static final BlockEntityEntry<VoxHumanaBlockEntity> VOX_HUMANA_BLOCK_ENTITY = registerPipeBlockEntity(
            "vox_humana_block_entity",
            VoxHumanaBlockEntity::new,
            AllBlocks.VOX_HUMANA,
            () -> VoxHumanaRenderer::new);

    private static <T extends GenericPipeBlockEntity> BlockEntityEntry<T> registerPipeBlockEntity(
            String name, BlockEntityBuilder.BlockEntityFactory<T> factory, NonNullSupplier<? extends Block> block,
            NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer) {
        return REGISTRATE.blockEntity(name, factory)
                .validBlock(block)
                .renderer(renderer)
                .register();
    }




    public static void register() {
    }
}
