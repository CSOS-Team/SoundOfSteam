package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.content.noteLink.NoteLinkBlock;
import com.finchy.pipeorgans.content.noteLink.NoteLinkBlockEntity;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.ponder.BoxVolume;
import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.finchy.pipeorgans.ponder.PonderWorldRevealer;
import com.finchy.pipeorgans.ponder.ponderWrappers.PonderNoteLink;
import com.finchy.pipeorgans.ponder.util.smartText.SmartText;
import com.finchy.pipeorgans.ponder.util.smartText.SmartTextDisplay;
import com.finchy.pipeorgans.ponder.util.timing.TimingMap;
import com.finchy.pipeorgans.ponder.util.timing.overrides.AdditiveTimeOverride;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class NoteLinkScenes {
    public static void noteLinkBasics(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        PonderWorldRevealer revealer = new PonderWorldRevealer(scene, util, PonderTimings.BUILD_STEP);
        SmartTextDisplay textDisplay = new SmartTextDisplay(scene, util);

        scene.title("note_link_basics", "Note Link Basics");
        scene.configureBasePlate(1, 0, 7);

        BlockPos windChestLeft = util.grid().at(5, 1, 5);
        BlockPos windChestMiddle = util.grid().at(4, 1, 5);
        BlockPos windChestRight = util.grid().at(3, 1, 5);

        Selection pipeLeft = util.select().fromTo(5, 2, 5, 5, 3, 5);
        Selection pipeMiddle = util.select().fromTo(4, 2, 5, 4, 4, 5);
        Selection pipeRight = util.select().fromTo(3, 2, 5, 3, 6, 5);

        BlockPos linkReceiverLeft = util.grid().at(5, 1, 4);
        BlockPos linkReceiverMiddle = util.grid().at(4, 1, 4);
        BlockPos linkReceiverRight = util.grid().at(3, 1, 4);

        PonderNoteLink receiverLeft = new PonderNoteLink(scene, util, linkReceiverLeft, Direction.NORTH, false, true);
        PonderNoteLink receiverMiddle = new PonderNoteLink(scene, util, linkReceiverMiddle, Direction.NORTH, false, true);
        PonderNoteLink receiverRight = new PonderNoteLink(scene, util, linkReceiverRight, Direction.NORTH, false, true);

        BlockPos linkTransmitterLeft = util.grid().at(5, 1, 2);
        BlockPos linkTransmitterMiddle = util.grid().at(4, 1, 2);
        BlockPos linkTransmitterRight = util.grid().at(3, 1, 2);

        PonderNoteLink transmitterLeft = new PonderNoteLink(scene, util, linkTransmitterLeft, Direction.NORTH, false, false);
        PonderNoteLink transmitterMiddle = new PonderNoteLink(scene, util, linkTransmitterMiddle, Direction.NORTH, false, false);
        PonderNoteLink transmitterRight = new PonderNoteLink(scene, util, linkTransmitterRight, Direction.NORTH, false, false);

        BlockPos leverLeft = util.grid().at(5, 1, 1);
        BlockPos leverMiddle = util.grid().at(4, 1, 1);
        BlockPos leverRight = util.grid().at(3, 1, 1);

        List<PonderWorldRevealer.Section> controlsSections = revealer.constructSectionsFromPositions(Direction.DOWN, null, linkTransmitterLeft, leverLeft, linkTransmitterMiddle, leverMiddle, linkTransmitterRight, leverRight);

        Selection redstoneRegionLeft = pipeLeft
                .add(util.select().position(windChestLeft))
                .add(util.select().position(linkReceiverLeft))
                .add(util.select().position(linkTransmitterLeft))
                .add(util.select().position(leverLeft))
                ;
        Selection redstoneRegionMiddle = pipeMiddle
                .add(util.select().position(windChestMiddle))
                .add(util.select().position(linkReceiverMiddle))
                .add(util.select().position(linkTransmitterMiddle))
                .add(util.select().position(leverMiddle))
                ;
        Selection redstoneRegionRight = pipeRight
                .add(util.select().position(windChestRight))
                .add(util.select().position(linkReceiverRight))
                .add(util.select().position(linkTransmitterRight))
                .add(util.select().position(leverRight))
                ;


        BlockPos keyboardRelayPos = util.grid().at(5, 1, 0);
        BlockPos trackerBarPos = util.grid().at(3, 1, 0);
        Selection midiBlocks = util.select().position(keyboardRelayPos).add(util.select().position(trackerBarPos));

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        revealer.revealSections(
            revealer.constructSectionsFromVolumes(
                Direction.DOWN,
                (v, i) -> {
                    if (i == 0) return Direction.NORTH;
                    else if (i % 3 != 2) return Direction.EAST;
                    else return null;
                },
                BoxVolume.point(1, 0, 7),                                   // cogwheel behind baseplate
                BoxVolume.point(0, 0, 6),                                   // big cogwheel on the side
                BoxVolume.singleAxis(2, 1,5, 4, Direction.EAST),     // Windchests
                BoxVolume.point(1, 1, 5),                                   // Fan
                BoxVolume.point(0, 1, 5),                                   // small cogwheel on the side (on the fan)
                BoxVolume.singleAxis(2, 1,4, 2, Direction.NORTH)     // Restone powering Windchest Master
            )
        );

        revealer.revealSections(revealer.constructSectionsFromSelections(Direction.DOWN, null, pipeLeft, pipeMiddle, pipeRight));

        scene.idle(PonderTimings.BUILD_FINISH);

        // Introduction
        revealer.revealSections(revealer.constructSectionsFromSelections(Direction.SOUTH, null, receiverLeft, receiverMiddle, receiverRight));
        textDisplay.showAndIdle(receiverRight.textOnCenter("Note links are a kind of Redstone Link that let you transmit and receive musical notes", true, true));
        textDisplay.showAndIdle(receiverRight.textOnCenter("Like Redstone Links, Note Links can transmit redstone signals wirelessly within 256 blocks", false, true));

        // Mode switching
        scene.idle(PonderTimings.READING_BUFFER);

        scene.overlay().showControls(receiverLeft.visualCenter().add(.5f, 0, 1.5f / 16f), Pointing.UP, 50)
                .rightClick()
                .whileSneaking();
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().modifyBlock(receiverLeft.getPos(), s -> s.cycle(NoteLinkBlock.RECEIVER), true);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        textDisplay.showAndIdle(receiverLeft.textOnCenter("Right-click while Sneaking to toggle receive mode", true, true).withTimings(new AdditiveTimeOverride(PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME), null));

        PonderUtil.showWrenchInteraction(scene, receiverMiddle.visualCenter().add(.5f, 0, 1.5f / 16f), Pointing.UP, false, false, 50);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().modifyBlock(receiverMiddle.getPos(), s -> s.cycle(NoteLinkBlock.RECEIVER), true);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        textDisplay.showAndIdle(receiverMiddle.textOnCenter("A simple Right-click with a Wrench can do the same", false, true).withTimings(new AdditiveTimeOverride(PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME), null));

        PonderUtil.showWrenchInteraction(scene, receiverRight.visualCenter().add(.5f, 0, 1.5f / 16f), Pointing.UP, false, false);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().modifyBlock(receiverRight.getPos(), s -> s.cycle(NoteLinkBlock.RECEIVER), true);
        scene.idle(PonderTimings.seconds(1));



        // Pitch frequency selection
        TimingMap times = textDisplay.show(receiverLeft.textOnPitchSlot("The bottom slot on the Note Link lets you select a note to receive or transmit", true, true));
        SmartTextDisplay.ShowAction secondText = textDisplay.getShowAction(receiverLeft.textOnPitchSlot("Hold right-click and select the desired note from the menu", false, true));
        SmartTextDisplay.ShowAction noteDisplayAction = textDisplay.getShowAction(receiverLeft.demonstratePitch("F4"));

        int firstReadingWindow = times.getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL);
        int secondReadingWindow = secondText.timings().getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL);
        receiverLeft.showPitchSlot(PonderPalette.WHITE, firstReadingWindow + secondReadingWindow + noteDisplayAction.timings().getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL) + PonderTimings.CONTEXT_INFO_BUFFER);
        scene.idle(firstReadingWindow);
        secondText.run(scene);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        int secondReadingTime = secondText.timings().get(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL, SmartTextDisplay.TimingMapHelp.DURATION_SLOT);
        scene.overlay().showControls(receiverLeft.pitchSlotPosition().add(.125f, 0, 0), Pointing.RIGHT, secondReadingTime - PonderTimings.CONTEXT_INFO_BUFFER)
                .rightClick();
        scene.idle(secondReadingWindow);
        noteDisplayAction.runAndIdle(scene);

        times = textDisplay.show(receiverMiddle.demonstratePitch("D4"));
        receiverMiddle.showPitchSlot(PonderPalette.WHITE, times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL, SmartTextDisplay.TimingMapHelp.DURATION_SLOT));
        scene.idle(times.getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL));

        times = textDisplay.show(receiverRight.demonstratePitch("A#3"));
        receiverRight.showPitchSlot(PonderPalette.WHITE, times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL, SmartTextDisplay.TimingMapHelp.DURATION_SLOT));
        scene.idle(times.getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL));

        // Key frequency selection
        times = textDisplay.show(receiverLeft.textOnKeySlot("Use the top slot to designate different groups of links", true, true));
        secondText = textDisplay.getShowAction(receiverLeft.textOnKeySlot("Place an item in the slot to specify the group", false, true));
        firstReadingWindow = times.getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL);
        secondReadingWindow = secondText.timings().getChannelTotal(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL);
        receiverLeft.showKeySlot(PonderPalette.WHITE, firstReadingWindow + secondReadingWindow + PonderTimings.CONTEXT_INFO_BUFFER);
        scene.idle(firstReadingWindow);
        secondText.run(scene);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        secondReadingTime = secondText.timings().get(SmartTextDisplay.TimingMapHelp.CHANNEL_TOTAL, SmartTextDisplay.TimingMapHelp.DURATION_SLOT);
        scene.overlay().showControls(receiverLeft.keySlotPosition().add(.125f, 0, 0), Pointing.RIGHT,  secondReadingTime - PonderTimings.CONTEXT_INFO_BUFFER)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(receiverLeft.getPos(), NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(secondReadingWindow - PonderTimings.CONTEXT_INFO_BUFFER);

        receiverMiddle.showKeySlot(PonderPalette.WHITE, PonderTimings.INTERACTION_DISPLAY_TIME + PonderTimings.CONTEXT_INFO_BUFFER * 3);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(receiverMiddle.keySlotPosition().add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(receiverMiddle.getPos(), NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER * 2);

        receiverRight.showKeySlot(PonderPalette.WHITE, PonderTimings.INTERACTION_DISPLAY_TIME + PonderTimings.CONTEXT_INFO_BUFFER * 3);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(receiverRight.keySlotPosition().add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(receiverRight.getPos(), NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER * 2);

        // Demonstration
        scene.addKeyframe();
        revealer.revealSections(controlsSections);
        textDisplay.showAndIdle(transmitterLeft.demonstratePitch("F4"));
        textDisplay.showAndIdle(transmitterMiddle.demonstratePitch("D4"));
        textDisplay.showAndIdle(transmitterRight.demonstratePitch("A#3"));

        textDisplay.showAndIdle(transmitterRight.textOnCenter("Note Links will transmit Redstone signals to other Note Links with the same pitch and the same group", true, true));

        scene.world().toggleRedstonePower(redstoneRegionLeft);
        scene.effects().indicateRedstone(leverLeft);
        scene.idle(1);
        scene.effects().indicateRedstone(receiverLeft.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionLeft);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        scene.world().toggleRedstonePower(redstoneRegionMiddle);
        scene.effects().indicateRedstone(leverMiddle);
        scene.idle(1);
        scene.effects().indicateRedstone(receiverMiddle.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionMiddle);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        scene.world().toggleRedstonePower(redstoneRegionRight);
        scene.effects().indicateRedstone(leverRight);
        scene.idle(1);
        scene.effects().indicateRedstone(receiverRight.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionRight);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        // Keyboard Relay / Tracker Bar interaction
        revealer.hideSections(controlsSections);
        revealer.revealSectionWithOffset(new PonderWorldRevealer.Section(midiBlocks, Direction.DOWN, Optional.of(10)), new Vec3(0, 0, 1));
        textDisplay.showAndIdle(SmartText.plainPointing("They can also interface with the Tracker Bar...", trackerBarPos.getCenter().add(0, 0, 1), true, true));
        textDisplay.showAndIdle(SmartText.plainPointing("...and the Keyboard Relay", keyboardRelayPos.getCenter().add(0, 0, 1), false, true));
        scene.idle(10);
        PonderUtil.showMidiGuiSlot(scene, util.vector().blockSurface(new BlockPos(4, 1, 1), Direction.UP), Pointing.DOWN, AllBlocks.DIAPASON.asStack(), 1, 70);
        scene.idle(10);
        textDisplay.showAndIdle(SmartText.plainPointing("Simply set the MIDI channel in the block's GUI, and it will automatically activate the correct Note Links", new BlockPos(3, 1, 1).getCenter(), false, true));

        textDisplay.showAndIdle(SmartText.coloredFloating("Note Links are backwards-compatible with Redstone Links via the Legacy Frequency Map. You can find a chart for it online", PonderPalette.RED, true));
    }
}
