package com.finchy.pipeorgans.init;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.content.base.BaseBlockEntity;
import com.finchy.pipeorgans.content.midi.keyboardRelay.KeyboardRelayBlockEntity;
import com.finchy.pipeorgans.content.midi.rollPuncher.RollPuncherBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarBlockEntity;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarRenderer;
import com.finchy.pipeorgans.content.midi.trackerBar.TrackerBarVisual;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.content.pipes.*;
import com.finchy.pipeorgans.content.pipes.generic.GenericPipeBlockEntity;
import com.finchy.pipeorgans.infrastructure.rendering.VerySmartBlockEntityRenderer;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Block;

//All dem Block Entities
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
            .renderer(() -> TrackerBarRenderer::new)
            .register();

    public static final BlockEntityEntry<RollPuncherBlockEntity> ROLL_PUNCHER_BLOCK_ENTITY = REGISTRATE
            .blockEntity("roll_puncher_block_entity", RollPuncherBlockEntity::new)
            .validBlock(AllBlocks.ROLL_PUNCHER)
            .register();


    public static final BlockEntityEntry<Diapason.DiapasonBlockEntity> DIAPASON_BLOCK_ENTITY = registerPipeBlockEntity(
            "diapason_block_entity",
            Diapason.DiapasonBlockEntity::new,
            AllBlocks.DIAPASON,
            () -> Diapason.DiapasonRenderer::new);

    public static final BlockEntityEntry<HauntedWhistle.HauntedWhistleBlockEntity> HAUNTED_WHISTLE_BLOCK_ENTITY = registerPipeBlockEntity(
            "haunted_whistle_block_entity",
            HauntedWhistle.HauntedWhistleBlockEntity::new,
            AllBlocks.HAUNTED_WHISTLE,
            () -> HauntedWhistle.HauntedWhistleRenderer::new);

    public static final BlockEntityEntry<Prestant.PrestantBlockEntity> PRESTANT_BLOCK_ENTITY = registerPipeBlockEntity(
            "prestant_block_entity",
            Prestant.PrestantBlockEntity::new,
            AllBlocks.PRESTANT,
            () -> Prestant.PrestantRenderer::new);

    public static final BlockEntityEntry<Gamba.GambaBlockEntity> GAMBA_BLOCK_ENTITY = registerPipeBlockEntity(
            "gamba_block_entity",
            Gamba.GambaBlockEntity::new,
            AllBlocks.GAMBA,
            () -> Gamba.GambaRenderer::new);

    public static final BlockEntityEntry<Gedeckt.GedecktBlockEntity> GEDECKT_BLOCK_ENTITY = registerPipeBlockEntity(
            "gedeckt_block_entity",
            Gedeckt.GedecktBlockEntity::new,
            AllBlocks.GEDECKT,
            () -> Gedeckt.GedecktRenderer::new);

    public static final BlockEntityEntry<Hohlflute.HohlfluteBlockEntity> HOHLFLUTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "hohlflute_block_entity",
            Hohlflute.HohlfluteBlockEntity::new,
            AllBlocks.HOHLFLUTE,
            () -> Hohlflute.HohlfluteRenderer::new);

    public static final BlockEntityEntry<Rohrflote.RohrfloteBlockEntity> ROHRFLOTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "rohrflote_block_entity",
            Rohrflote.RohrfloteBlockEntity::new,
            AllBlocks.ROHRFLOTE,
            () -> Rohrflote.RohrfloteRenderer::new);

    public static final BlockEntityEntry<Nasard.NasardBlockEntity> NASARD_BLOCK_ENTITY = registerPipeBlockEntity(
            "nasard_block_entity",
            Nasard.NasardBlockEntity::new,
            AllBlocks.NASARD,
            () -> Nasard.NasardRenderer::new);

    public static final BlockEntityEntry<Tierce.TierceBlockEntity> TIERCE_BLOCK_ENTITY = registerPipeBlockEntity(
            "tierce_block_entity",
            Tierce.TierceBlockEntity::new,
            AllBlocks.TIERCE,
            () -> Tierce.TierceRenderer::new);

    public static final BlockEntityEntry<Piccolo.PiccoloBlockEntity> PICCOLO_BLOCK_ENTITY = registerPipeBlockEntity(
            "piccolo_block_entity",
            Piccolo.PiccoloBlockEntity::new,
            AllBlocks.PICCOLO,
            () -> Piccolo.PiccoloRenderer::new);

    /*
    public static final BlockEntityEntry<Oktav.OktavBlockEntity> OKTAV_BLOCK_ENTITY = registerPipeBlockEntity(
            "oktav_block_entity",
            Oktav.OktavBlockEntity::new,
            AllBlocks.OKTAV,
            () -> Oktav.OktavRenderer::new);
     */

    public static final BlockEntityEntry<Posaune.PosauneBlockEntity> POSAUNE_BLOCK_ENTITY = registerPipeBlockEntity(
            "posaune_block_entity",
            Posaune.PosauneBlockEntity::new,
            AllBlocks.POSAUNE,
            () -> Posaune.PosauneRenderer::new);

    public static final BlockEntityEntry<Bassoon.BassoonBlockEntity> BASSOON_BLOCK_ENTITY = registerPipeBlockEntity(
            "bassoon_block_entity",
            Bassoon.BassoonBlockEntity::new,
            AllBlocks.BASSOON,
            () -> Bassoon.BassoonRenderer::new);

    public static final BlockEntityEntry<Hautbois.HautboisBlockEntity> HAUTBOIS_BLOCK_ENTITY = registerPipeBlockEntity(
            "hautbois_block_entity",
            Hautbois.HautboisBlockEntity::new,
            AllBlocks.HAUTBOIS,
            () -> Hautbois.HautboisRenderer::new);

    public static final BlockEntityEntry<Subbass.SubbassBlockEntity> SUBBASS_BLOCK_ENTITY = registerPipeBlockEntity(
            "subbass_block_entity",
            Subbass.SubbassBlockEntity::new,
            AllBlocks.SUBBASS,
            () -> Subbass.SubbassRenderer::new);

    /*
    public static final BlockEntityEntry<Untersatz.UntersatzBlockEntity> UNTERSATZ_BLOCK_ENTITY = registerPipeBlockEntity(
            "untersatz_block_entity",
            Untersatz.UntersatzBlockEntity::new,
            AllBlocks.UNTERSATZ,
            () -> Untersatz.UntersatzRenderer::new);
     */

    public static final BlockEntityEntry<OpenWood.OpenWoodBlockEntity> OPEN_WOOD_BLOCK_ENTITY = registerPipeBlockEntity(
            "open_wood_block_entity",
            OpenWood.OpenWoodBlockEntity::new,
            AllBlocks.OPEN_WOOD,
            () -> OpenWood.OpenWoodRenderer::new);

    public static final BlockEntityEntry<Trompette.TrompetteBlockEntity> TROMPETTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "trompette_block_entity",
            Trompette.TrompetteBlockEntity::new,
            AllBlocks.TROMPETTE,
            () -> Trompette.TrompetteRenderer::new);

    public static final BlockEntityEntry<Chamade.ChamadeBlockEntity> CHAMADE_BLOCK_ENTITY = registerPipeBlockEntity(
            "chamade_block_entity",
            Chamade.ChamadeBlockEntity::new,
            AllBlocks.CHAMADE,
            () -> Chamade.ChamadeRenderer::new);

    public static final BlockEntityEntry<Krummhorn.KrummhornBlockEntity> KRUMMHORN_BLOCK_ENTITY = registerPipeBlockEntity(
            "krumhorn_block_entity",
            Krummhorn.KrummhornBlockEntity::new,
            AllBlocks.KRUMMHORN,
            () -> Krummhorn.KrummhornRenderer::new);

    public static final BlockEntityEntry<EnglishHorn.EnglishHornBlockEntity> ENGLISH_HORN_BLOCK_ENTITY = registerPipeBlockEntity(
            "english_horn_block_entity",
            EnglishHorn.EnglishHornBlockEntity::new,
            AllBlocks.ENGLISH_HORN,
            () -> EnglishHorn.EnglishHornRenderer::new);

    public static final BlockEntityEntry<Viola.ViolaBlockEntity> VIOLA_BLOCK_ENTITY = registerPipeBlockEntity(
            "viola_block_entity",
            Viola.ViolaBlockEntity::new,
            AllBlocks.VIOLA,
            () -> Viola.ViolaRenderer::new);

    public static final BlockEntityEntry<VoxCeleste.VoxCelesteBlockEntity> VOX_CELESTE_BLOCK_ENTITY = registerPipeBlockEntity(
            "vox_celeste_block_entity",
            VoxCeleste.VoxCelesteBlockEntity::new,
            AllBlocks.VOX_CELESTE,
            () -> VoxCeleste.VoxCelesteRenderer::new);

    public static final BlockEntityEntry<VoxHumana.VoxHumanaBlockEntity> VOX_HUMANA_BLOCK_ENTITY = registerPipeBlockEntity(
            "vox_humana_block_entity",
            VoxHumana.VoxHumanaBlockEntity::new,
            AllBlocks.VOX_HUMANA,
            () -> VoxHumana.VoxHumanaRenderer::new);

    public static final BlockEntityEntry<NoteLinkBlockEntity> NOTE_LINK_BLOCK_ENTITY = REGISTRATE
            .blockEntity("note_link", NoteLinkBlockEntity::new)
            .validBlocks(AllBlocks.NOTE_LINK)
            .renderer(() -> VerySmartBlockEntityRenderer::new)
            .register();

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
