package com.finchy.pipeorgans.data;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.init.AllItems;
import com.finchy.pipeorgans.util.TextUtils;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import com.finchy.pipeorgans.content.base.BaseBlock;




public class PipeOrgansAdvancementGenerator implements ForgeAdvancementProvider.AdvancementGenerator {

    @java.lang.Override
    public void generate(HolderLookup.Provider registries, java.util.function.Consumer<Advancement> consumer, ExistingFileHelper existingFileHelper) {
        Advancement csosIntro = Advancement.Builder.advancement()
                .display(AllBlocks.TROMPETTE.get(),
                        TextUtils.getTranslation("advancement.csosintro"),
                        TextUtils.getTranslation("advancement.csosintro.desc"),
                        new ResourceLocation("minecraft:textures/block/cut_copper.png"),
                        FrameType.TASK, false, false, false)
                .addCriterion("basic_pipe", InventoryChangeTrigger.TriggerInstance.hasItems(new ItemLike[]{}))
                .save(consumer, getNameId("main/root"));

        Advancement craftingTheBasics = getAdvancement(csosIntro, AllBlocks.BASE.get(), "pipebase", FrameType.TASK, true, false, false)
                .addCriterion("craft_pipe_base", InventoryChangeTrigger.TriggerInstance.hasItems(AllBlocks.BASE.get()))
                .save(consumer, getNameId("main/craft_pipe_base"));

//principal branch
        Advancement principalVoice = getAdvancement(craftingTheBasics, AllBlocks.DIAPASON.get(), "diapason", FrameType.TASK, true, false, false)
                .addCriterion("place_diapason_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.DIAPASON.get()))
                .save(consumer, getNameId("main/place_diapason_pipe"));

        Advancement pressedAndPrestant = getAdvancement(principalVoice, AllBlocks.PRESTANT.get(), "prestant", FrameType.TASK, true, false, false)
                .addCriterion("place_prestant_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.PRESTANT.get()))
                .save(consumer, getNameId("main/place_prestant_pipe"));

        Advancement piercingTheSky = getAdvancement(pressedAndPrestant, AllBlocks.PICCOLO.get(), "piccolo", FrameType.TASK, true, false, false)
                .addCriterion("place_piccolo_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.PICCOLO.get()))
                .save(consumer, getNameId("main/place_piccolo_pipe"));

        Advancement bigBoom = getAdvancement(piercingTheSky, AllBlocks.OPEN_WOOD.get(), "open_wood", FrameType.TASK, true, false, false)
                .addCriterion("place_open_wood_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.OPEN_WOOD.get()))
                .save(consumer, getNameId("main/place_open_wood_pipe"));


        //Flute branch
        Advancement cappedHarmony = getAdvancement(craftingTheBasics, AllBlocks.GEDECKT.get(), "gedeckt", FrameType.TASK, true, false, false)
                .addCriterion("place_gedeckt_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.GEDECKT.get()))
                .save(consumer, getNameId("main/place_gedeckt_pipe"));

        Advancement pipeWithAHAt = getAdvancement(cappedHarmony, AllBlocks.ROHRFLOTE.get(), "rohrflote", FrameType.TASK, true, false, false)
                .addCriterion("place_rohrflote_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.ROHRFLOTE.get()))
                .save(consumer, getNameId("main/place_rohrflote_pipe"));

        Advancement hohlInOne = getAdvancement(pipeWithAHAt, AllBlocks.HOHLFLUTE.get(), "hohlflute", FrameType.TASK, true, false, false)
                .addCriterion("place_hohlflute_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.HOHLFLUTE.get()))
                .save(consumer, getNameId("main/place_hohlflute_pipe"));

        Advancement allAboutThatSubbass = getAdvancement(hohlInOne, AllBlocks.SUBBASS.get(), "subbass", FrameType.TASK, true, false, false)
                .addCriterion("place_subbass_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.SUBBASS.get()))
                .save(consumer, getNameId("main/place_subbass_pipe"));

        //Mutation Branch
        Advancement notesBetweenTheNotes = getAdvancement(craftingTheBasics, AllBlocks.NASARD.get(), "nasard", FrameType.TASK, true, false, false)
                .addCriterion("place_nasard_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.NASARD.get()))
                .save(consumer, getNameId("main/place_nasard_pipe"));

        Advancement addsFlavourRuinsChords = getAdvancement(notesBetweenTheNotes, AllBlocks.TIERCE.get(), "tierce", FrameType.TASK, true, false, false)
                .addCriterion("place_tierce_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.TIERCE.get()))
                .save(consumer, getNameId("main/place_tierce_pipe"));

        //Strings branch

        Advancement bowlessBeauty = getAdvancement(craftingTheBasics, AllBlocks.VIOLA.get(), "viola", FrameType.TASK, true, false, false)
                .addCriterion("place_viola_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.VIOLA.get()))
                .save(consumer, getNameId("main/place_viola_pipe"));

        Advancement pleasantlyOutOfTune = getAdvancement(bowlessBeauty, AllBlocks.VOX_CELESTE.get(), "vox_celeste", FrameType.TASK, true, false, false)
                .addCriterion("place_vox_celeste_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.VOX_CELESTE.get()))
                .save(consumer, getNameId("main/place_vox_celeste_pipe"));

        Advancement shrillStrings = getAdvancement(pleasantlyOutOfTune, AllBlocks.GAMBA.get(), "gamba", FrameType.TASK, true, false, false)
                .addCriterion("place_gamba_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.GAMBA.get()))
                .save(consumer, getNameId("main/place_gamba_pipe"));

        //Reed Branch

        Advancement areYouReedy = getAdvancement(craftingTheBasics, AllItems.BRASS_REED.get(), "reed", FrameType.TASK, true, false, false)
                .addCriterion("craft_reed", InventoryChangeTrigger.TriggerInstance.hasItems(AllItems.BRASS_REED.get()))
                .save(consumer, getNameId("main/craft_reed"));

        Advancement HONK = getAdvancement(areYouReedy, AllBlocks.TROMPETTE.get(), "trompette", FrameType.TASK, true, false, false)
                .addCriterion("place_trompette_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.TROMPETTE.get()))
                .save(consumer, getNameId("main/place_trompette_pipe"));

        Advancement actuallyFromGermany = getAdvancement(HONK, AllBlocks.ENGLISH_HORN.get(), "english_horn", FrameType.TASK, true, false, false)
                .addCriterion("place_english_horn_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.ENGLISH_HORN.get()))
                .save(consumer, getNameId("main/place_english_horn_pipe"));

        Advancement aChoirOfGoats = getAdvancement(actuallyFromGermany, AllBlocks.VOX_HUMANA.get(), "vox_humana", FrameType.TASK, true, false, false)
                .addCriterion("place_vox_humana_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.VOX_HUMANA.get()))
                .save(consumer, getNameId("main/place_vox_humana_pipe"));

        Advancement temparmentIssuesIncluded = getAdvancement(aChoirOfGoats, AllBlocks.BASSOON.get(), "bassoon", FrameType.TASK, true, false, false)
                .addCriterion("place_bassoon_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.BASSOON.get()))
                .save(consumer, getNameId("main/place_bassoon_pipe"));

        Advancement rumblingReeds = getAdvancement(temparmentIssuesIncluded, AllBlocks.POSAUNE.get(), "posaune", FrameType.TASK, true, false, false)
                .addCriterion("place_posaune_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.POSAUNE.get()))
                .save(consumer, getNameId("main/place_posaune_pipe"));

        Advancement theyreHorizontalNow = getAdvancement(rumblingReeds, AllBlocks.CHAMADE.get(), "chamade", FrameType.TASK, true, false, false)
                .addCriterion("place_chamade_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.CHAMADE.get()))
                .save(consumer, getNameId("main/place_chamade_pipe"));


        //Hidden Advancements
        //Don't ruin the fun and peek














        // THERE'S NOTHING YET!!! MWAHAHAHAHAHAHAHA
        //also, why'd you peek :(

        //except you Finchy, you're allowed to peek <3
    }


    protected static Advancement.Builder getAdvancement(Advancement parent, ItemLike display, String name, FrameType frame, boolean showToast, boolean announceToChat, boolean hidden) {
        return Advancement.Builder.advancement().parent(parent).display(display,
                TextUtils.getTranslation("advancement." + name),
                TextUtils.getTranslation("advancement." + name + ".desc"),
                null, frame, showToast, announceToChat, hidden);
    }

    private String getNameId(String id) {
        return PipeOrgans.MOD_ID + ":" + id;
    }
}
//Basic Advancement copy/paste
/*
       Advancement **advancementName** = getAdvancement(**whatToConnectItTo**, AllBlocks.**.get(), "**name**", FrameType.TASK, true, false, false)
                .addCriterion("place_diapason_pipe", ItemUsedOnLocationTrigger.TriggerInstance.placedBlock(AllBlocks.DIAPASON.get()))
                .save(consumer, getNameId("main/place_diapason_pipe"));
    }
 */