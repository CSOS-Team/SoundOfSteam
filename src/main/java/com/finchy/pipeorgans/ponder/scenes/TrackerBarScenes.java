package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

public class TrackerBarScenes {

    public static void musicRollPlayback(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("tracker_bar", "Playing music rolls");
        scene.configureBasePlate(1, 1, 7);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos trackerBar = util.grid().at(4, 1, 4);

        // Large cogwheel
        scene.world().showSection(util.select().position(8, 0, 4), Direction.DOWN);
        scene.idle(PonderTimings.BUILD_STEP);

        for (int i = 8; i > 3; i--) { // reveal shafts and tracker bar
            scene.world().showSection(util.select().position(i, 1, 3), Direction.DOWN);
            scene.idle(PonderTimings.BUILD_STEP);
        }

        for (int xPos = 7; xPos >= 5; xPos--) { // reveal diapasons
            PonderUtil.revealBlocks(scene, util, util.select().fromTo(xPos, 1, 5, xPos, 6, 6), 2);
        }
        
        scene.idle(6);

        for (int xPos = 3; xPos >= 1; xPos--) { // reveal trompettes
            PonderUtil.revealBlocks(scene, util, util.select().fromTo(xPos, 1, 5, xPos, 6, 6), 2);
        }

        scene.addKeyframe();

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The Tracker Bar is used to play MIDI files from music rolls")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());
        
        //When playing a note, it will activate any note links that match the note and assigned frequency
        //Use the GUI to assign frequency items to each instrument in the MIDI file

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

        PonderUtil.showMidiGuiSlot(scene, trackerBar.east().getCenter(), Pointing.RIGHT, new ItemStack(Items.SNOW_BLOCK), PonderTimings.READING_TIME);

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
