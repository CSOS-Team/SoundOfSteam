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

public class TrackerBarPonder {

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
