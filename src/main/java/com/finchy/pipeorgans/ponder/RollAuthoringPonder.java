package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class RollAuthoringPonder {
    public static void rollAuthoringTable(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("roll_authoring", "Making music rolls");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos rollPuncher = util.grid().at(2, 1, 2);
        BlockPos trackerBar = util.grid().at(2, 2, 2);

        PonderUtil.revealBlock(scene, util, rollPuncher, PonderTimings.BUILD_STEP);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("To create a music roll you need a roll authoring table")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(rollPuncher.getCenter());

        scene.idle(PonderTimings.READING_WINDOW);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Once placed, you take a paper and place it in the authoring table interface")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(rollPuncher.getCenter());

        scene.idle(PonderTimings.READING_BUFFER);

        // Show paper being used
        scene.overlay().showControls(util.vector().centerOf(rollPuncher), Pointing.RIGHT, PonderTimings.afterBuffer(2)).withItem(new ItemStack(Items.PAPER, 1)).rightClick();

        scene.idle(PonderTimings.afterBuffer());

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("There, you scroll through the filenames to select the MIDI file you wish to punch onto the paper")
                .placeNearTarget()
                .pointAt(rollPuncher.getCenter());

        scene.idle(PonderTimings.READING_WINDOW);

        scene.addKeyframe();

        scene.world().hideSection(util.select().position(rollPuncher), Direction.DOWN);
        scene.idle(PonderTimings.BUILD_FINISH);

        PonderUtil.revealBlock(scene, util, util.grid().at(5, 0, 3), PonderTimings.BUILD_STEP);

        // Begin animating rotational power loading in for tracker bar
        for (int i = 5; i > 2; i--) {
            scene.world().showSection(util.select().position(i, 1, 2), Direction.DOWN);
            scene.idle(PonderTimings.BUILD_STEP);
        }

        PonderUtil.revealBlockWithOffset(scene, util, trackerBar, new Vec3(0, -1, 0), PonderTimings.BUILD_FINISH);

        scene.world().setKineticSpeed(util.select().position(trackerBar), 16);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The music roll can then be played with a tracker bar")
                .placeNearTarget()
                .pointAt(rollPuncher.getCenter());

        scene.idle(PonderTimings.READING_BUFFER);

        scene.idle(PonderTimings.afterBuffer());

        scene.markAsFinished();
    }

    public static void musicRollPlayback(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("tracker_bar", "Playing music rolls");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos trackerBar = util.grid().at(2, 1, 1);

        // Large cogwheel
        scene.world().showSection(util.select().position(5, 0, 0), Direction.DOWN);
        scene.idle(PonderTimings.BUILD_STEP);

        for (int i = 5; i > 1; i--) {
            scene.world().showSection(util.select().position(i, 1, 1), Direction.DOWN);
            scene.idle(PonderTimings.BUILD_STEP);
        }

        for (int xPos = 0; xPos < 6; xPos++) {
            PonderUtil.revealBlocks(scene, util, util.select().fromTo(xPos, 1, 3, xPos, 6, 4), 2);
        }

        scene.addKeyframe();

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The tracker bar is used to play MIDI files through music rolls, which you can create with the Roll Authoring Table")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());

        scene.idle(PonderTimings.READING_WINDOW);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("When the tracker bar plays a music roll, it sends out a redstone signal to the associated first frequency on a Redstone link")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(trackerBar.getCenter());

        for (int i = 0; i < 5; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 3).subtract(0, 0.35, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, PonderTimings.READING_WINDOW * 2);
        }

        scene.idle(PonderTimings.READING_WINDOW);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The item associated with a frequency corresponds to an instrument used by the MIDI file")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());

        scene.idle(PonderTimings.READING_WINDOW);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Then it sends out the note to play on the second frequency, which you can find a chart for online")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(trackerBar.getCenter());

        for (int i = 0; i < 5; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 3).subtract(0, 0.65, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, PonderTimings.READING_TIME);
        }

        scene.idle(PonderTimings.READING_WINDOW);

        scene.markAsFinished();
    }
}
