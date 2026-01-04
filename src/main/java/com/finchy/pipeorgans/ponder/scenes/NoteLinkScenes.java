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
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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

        Vec3 linkReceiverLeftVisualCenter = linkReceiverLeft.getCenter().add(0, 0, .5f - 1.5f / 16f);
        Vec3 linkReceiverMiddleVisualCenter = linkReceiverMiddle.getCenter().add(0, 0, .5f - 1.5f / 16f);
        Vec3 linkReceiverRightVisualCenter = linkReceiverRight.getCenter().add(0, 0, .5f - 1.5f / 16f);

        BlockPos linkTransmitterLeft = util.grid().at(5, 1, 2);
        BlockPos linkTransmitterMiddle = util.grid().at(4, 1, 2);
        BlockPos linkTransmitterRight = util.grid().at(3, 1, 2);

        BlockPos leverLeft = util.grid().at(5, 1, 1);
        BlockPos leverMiddle = util.grid().at(4, 1, 1);
        BlockPos leverRight = util.grid().at(3, 1, 1);

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
        SmartText modeSwitchWrenchText = receiverLeft.textOnCenter("They can be toggled between transmitter and receiver modes by using a Wrench on them", true, true);

        PonderUtil.displayText(scene, linkReceiverLeftVisualCenter, "They can be toggled between transmitter and receiver modes by using a Wrench on them", true, true, PonderTimings.READING_TIME + PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.idle(PonderTimings.READING_TIME + PonderTimings.CONTEXT_INFO_BUFFER);
        PonderUtil.showWrenchInteraction(scene, linkReceiverLeftVisualCenter.add(.5f, 0, 1.5f / 16f), Pointing.RIGHT, false, false);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().cycleBlockProperty(linkReceiverLeft, NoteLinkBlock.RECEIVER);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2 + PonderTimings.READING_BUFFER);

        PonderUtil.displayText(scene, linkReceiverLeftVisualCenter, "... or interacting with them while crouching", true, true, PonderTimings.READING_TIME + PonderTimings.CONTEXT_INFO_BUFFER + PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.idle(PonderTimings.READING_TIME + PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(linkReceiverLeftVisualCenter.add(.5f, 0, 1.5f / 16f), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .whileSneaking();
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        scene.world().cycleBlockProperty(linkReceiverLeft, NoteLinkBlock.RECEIVER);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2 + PonderTimings.READING_BUFFER);

        // Pitch frequency selection
        BlockState bs = scene.getScene().getWorld().getBlockState(linkReceiverLeft);
        Vec3 leftReceiverPitchSlotPos = linkReceiverLeft.getCenter().subtract(.5f, .5f, .5f).add(NoteLinkBlockEntity.PITCH_SLOT_TRANSFORM.getLocalOffset(scene.getScene().getWorld(), linkReceiverLeft, bs));
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.WHITE,
                linkReceiverLeft,
                new AABB(leftReceiverPitchSlotPos, leftReceiverPitchSlotPos).inflate(3f / 16f, 3f / 16f, 0f),
                PonderTimings.READING_WINDOW * 2 + PonderTimings.CONTEXT_INFO_BUFFER
        );
        PonderUtil.displayTextAndWait(scene, linkReceiverLeftVisualCenter, "This is the Note Link's main feature: The Pitch Frequency slot", true, true);
        PonderUtil.displayText(scene, linkReceiverLeftVisualCenter, "Hold right-click and select the desired note from the menu by dragging the mouse over it", false, true, PonderTimings.READING_TIME);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(leftReceiverPitchSlotPos.add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.READING_TIME - PonderTimings.CONTEXT_INFO_BUFFER)
                .rightClick();
        scene.idle(PonderTimings.READING_TIME);
        scene.overlay().showText(PonderTimings.INTERACTION_DISPLAY_TIME)
                        .pointAt(leftReceiverPitchSlotPos)
                        .text("F4")
                        .colored(PonderPalette.GREEN)
                        .placeNearTarget();

        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME + PonderTimings.READING_BUFFER);

        Vec3 middleReceiverPitchSlotPos = leftReceiverPitchSlotPos.subtract(1, 0, 0);
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.WHITE,
                linkReceiverMiddle,
                new AABB(middleReceiverPitchSlotPos, middleReceiverPitchSlotPos).inflate(3f / 16f, 3f / 16f, 0f),
                PonderTimings.INTERACTION_DISPLAY_TIME
        );
        scene.overlay().showText(PonderTimings.INTERACTION_DISPLAY_TIME)
                .pointAt(middleReceiverPitchSlotPos)
                .text("D4")
                .colored(PonderPalette.GREEN)
                .placeNearTarget();

        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME + PonderTimings.CONTEXT_INFO_BUFFER);

        Vec3 rightReceiverPitchSlotPos = leftReceiverPitchSlotPos.subtract(2, 0, 0);
        scene.overlay().chaseBoundingBoxOutline(
                PonderPalette.WHITE,
                linkReceiverRight,
                new AABB(rightReceiverPitchSlotPos, rightReceiverPitchSlotPos).inflate(3f / 16f, 3f / 16f, 0f),
                PonderTimings.INTERACTION_DISPLAY_TIME
        );
        scene.overlay().showText(PonderTimings.INTERACTION_DISPLAY_TIME)
                .pointAt(rightReceiverPitchSlotPos)
                .text("A#3")
                .colored(PonderPalette.GREEN)
                .placeNearTarget();

        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME + PonderTimings.CONTEXT_INFO_BUFFER);

        // Key frequency selection
        Vec3 leftReceiverKeySlotPos = linkReceiverLeft.getCenter().subtract(.5f, .5f, .5f).add(NoteLinkBlockEntity.KEY_SLOT_TRANSFORM.getLocalOffset(scene.getScene().getWorld(), linkReceiverLeft, bs));
        scene.overlay().showFilterSlotInput(leftReceiverKeySlotPos, Direction.NORTH, PonderTimings.READING_WINDOW * 2 + PonderTimings.CONTEXT_INFO_BUFFER);
        PonderUtil.displayTextAndWait(scene, leftReceiverKeySlotPos, "Use the Key Frequency slot to differentiate between different sounds on the same pitch", true, true);
        PonderUtil.displayText(scene, leftReceiverKeySlotPos, "Like on a Redstone Link, right-click it with an item to set it as the key frequency", false, true, PonderTimings.READING_TIME);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(leftReceiverKeySlotPos.add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(linkReceiverLeft, NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(PonderTimings.READING_TIME - PonderTimings.CONTEXT_INFO_BUFFER - PonderTimings.INTERACTION_DISPLAY_TIME);

        Vec3 middleReceiverKeySlotPos = leftReceiverKeySlotPos.subtract(1, 0, 0);
        scene.overlay().showFilterSlotInput(middleReceiverKeySlotPos, Direction.NORTH, PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(middleReceiverKeySlotPos.add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(linkReceiverMiddle, NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        Vec3 rightReceiverKeySlotPos = leftReceiverKeySlotPos.subtract(2, 0, 0);
        scene.overlay().showFilterSlotInput(rightReceiverKeySlotPos, Direction.NORTH, PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.overlay().showControls(rightReceiverKeySlotPos.add(.125f, 0, 0), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                .rightClick()
                .withItem(AllBlocks.DIAPASON.asStack());
        scene.idle(PonderTimings.CONTEXT_INFO_BUFFER);
        scene.world().modifyBlockEntity(linkReceiverRight, NoteLinkBlockEntity.class, be -> be.setKey(AllBlocks.DIAPASON.asStack()));
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        // Demonstration
        revealer.revealSections(revealer.constructSectionsFromPositions(Direction.DOWN, null, linkTransmitterLeft, leverLeft, linkTransmitterMiddle, leverMiddle, linkTransmitterRight, leverRight));


        // Keyboard Relay / Tracker Bar interaction

    }
}
