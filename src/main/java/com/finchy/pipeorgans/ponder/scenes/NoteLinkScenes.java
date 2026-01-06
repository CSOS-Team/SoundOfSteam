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
import com.finchy.pipeorgans.ponder.util.timing.overrides.FixedTimeOverride;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

import java.util.List;

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

        PonderNoteLink receiverLeft = new PonderNoteLink(scene, util, linkReceiverLeft);
        PonderNoteLink receiverMiddle = new PonderNoteLink(scene, util, linkReceiverMiddle);
        PonderNoteLink receiverRight = new PonderNoteLink(scene, util, linkReceiverRight);

        BlockPos linkTransmitterLeft = util.grid().at(5, 1, 2);
        BlockPos linkTransmitterMiddle = util.grid().at(4, 1, 2);
        BlockPos linkTransmitterRight = util.grid().at(3, 1, 2);

        PonderNoteLink transmitterLeft = new PonderNoteLink(scene, util, linkTransmitterLeft);
        PonderNoteLink transmitterMiddle = new PonderNoteLink(scene, util, linkTransmitterMiddle);
        PonderNoteLink transmitterRight = new PonderNoteLink(scene, util, linkTransmitterRight);

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
        textDisplay.showAndIdle(receiverRight.textOnCenter("Note Links are specialized Redstone Links that simplify the frequency selection for musical applications", true, true));
        textDisplay.showAndIdle(receiverRight.textOnCenter("Similarly to their Redstone counterparts, Note Links can transmit Redstone signals wirelessly within a configurable range, 256 blocks by default", false, true));
        textDisplay.showAndIdle(receiverRight.textOnCenter("However, Note Links simplify frequency selection by providing a set of predefined musical note frequencies to choose from", false, true));

        // Mode switching
        scene.idle(PonderTimings.READING_BUFFER);

        TimingMap times = textDisplay.show(receiverLeft.textOnCenter("They can be toggled between transmitter and receiver modes by using a Wrench on them", true, true).withTimings(new AdditiveTimeOverride(PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME), null));
        scene.idle(times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_COMBINED_HIGHEST, SmartTextDisplay.TimingMapHelp.DURATION_SLOT) + PonderTimings.CONTEXT_INFO_BUFFER);
        PonderUtil.showWrenchInteraction(scene, receiverLeft.visualCenter().add(.5f, 0, 1.5f / 16f), Pointing.RIGHT, false, false);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().cycleBlockProperty(linkReceiverLeft, NoteLinkBlock.RECEIVER);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2 + times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_COMBINED_HIGHEST, SmartTextDisplay.TimingMapHelp.BUFFER_SLOT));

        times = textDisplay.show(receiverLeft.textOnCenter("... or interacting with them while crouching", true, true).withTimings(new AdditiveTimeOverride(PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME), null));
        scene.idle(times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_COMBINED_HIGHEST, SmartTextDisplay.TimingMapHelp.DURATION_SLOT) + PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(receiverLeft.visualCenter().add(.5f, 0, 1.5f / 16f), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .whileSneaking();
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().cycleBlockProperty(linkReceiverLeft, NoteLinkBlock.RECEIVER);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2 + times.get(SmartTextDisplay.TimingMapHelp.CHANNEL_COMBINED_HIGHEST, SmartTextDisplay.TimingMapHelp.BUFFER_SLOT));

        // Pitch frequency selection
        times = textDisplay.show(receiverLeft.textOnPitchSlot("This is the Note Link's main feature: The Pitch Frequency slot", true, true));
        SmartTextDisplay.ShowAction secondText = textDisplay.getShowAction(receiverLeft.textOnPitchSlot("Hold right-click and select the desired note from the menu by dragging the mouse over it", false, true));
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
        times = textDisplay.show(receiverLeft.textOnKeySlot("Use the Key Frequency slot to differentiate between different sounds of the same pitch", true, true));
        secondText = textDisplay.getShowAction(receiverLeft.textOnKeySlot("Like on a Redstone Link, right-click it with an item to set it as the key frequency", false, true));
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
        scene.world().cycleBlockProperty(receiverLeft.getPos(), NoteLinkBlock.RECEIVER);
        scene.world().cycleBlockProperty(receiverMiddle.getPos(), NoteLinkBlock.RECEIVER);
        scene.world().cycleBlockProperty(receiverRight.getPos(), NoteLinkBlock.RECEIVER);
        textDisplay.showAndIdle(transmitterLeft.demonstratePitch("F4"));
        textDisplay.showAndIdle(transmitterMiddle.demonstratePitch("D4"));
        textDisplay.showAndIdle(transmitterRight.demonstratePitch("A#3"));

        textDisplay.showAndIdle(transmitterRight.textOnCenter("Note Links only transmit Redstone signals to another when their Pitch and Key Frequencies match", false, true));

        scene.world().toggleRedstonePower(redstoneRegionLeft);
        scene.effects().indicateRedstone(leverLeft);
        scene.idle(5);
        scene.effects().indicateRedstone(receiverLeft.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionLeft);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        scene.world().toggleRedstonePower(redstoneRegionMiddle);
        scene.effects().indicateRedstone(leverMiddle);
        scene.idle(5);
        scene.effects().indicateRedstone(receiverMiddle.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionMiddle);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        scene.world().toggleRedstonePower(redstoneRegionRight);
        scene.effects().indicateRedstone(leverRight);
        scene.idle(5);
        scene.effects().indicateRedstone(receiverRight.getPos());
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME - 5);
        scene.world().toggleRedstonePower(redstoneRegionRight);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        // Keyboard Relay / Tracker Bar interaction
        scene.addKeyframe();
        revealer.hideSections(controlsSections);
        revealer.revealSectionWithOffset(new PonderWorldRevealer.Section(midiBlocks, Direction.DOWN), new Vec3(0, 0, 1));
        textDisplay.showAndIdle(SmartText.plainPointing("It also interfaces with the Tracker Bar...", trackerBarPos.getCenter().add(0, 0, 1), false, true));
        textDisplay.showAndIdle(SmartText.plainPointing("... and the Keyboard Relay block.", keyboardRelayPos.getCenter().add(0, 0, 1), false, true));

        textDisplay.showAndIdle(SmartText.coloredFloating("It is also compatible with Redstone Links with the Legacy Frequency Map. You can find a chart for it online", PonderPalette.RED, true));
    }
}
