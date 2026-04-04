package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.finchy.pipeorgans.ponder.ponderWrappers.PonderPipe;
import com.simibubi.create.AllItems;
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

        BlockPos trackerBar = util.grid().at(4, 1, 3);
        Vec3 zincLinksVec = util.vector().blockSurface(util.grid().at(6, 1, 5), Direction.SOUTH)
                .add(0, 0, -3 / 16f);
        
        BlockPos f4DiapasonPos = util.grid().at(7, 2, 6);

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
        
        //Each channel in the MIDI file is represented by a frequency
        //This frequency is used when the Tracker Bar plays a note on that channel
        //When playing a note, it will activate any note links that match the note and assigned frequency
        //Use the GUI to assign frequency items to each channel in the MIDI file

        scene.idle(PonderTimings.READING_WINDOW);

        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Each channel in the MIDI file is represented by a frequency")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());
        
        scene.idle(PonderTimings.READING_WINDOW/2);

        PonderUtil.showMidiGuiSlot(scene, trackerBar.getCenter().add(0, 0.5, 0), Pointing.DOWN, new ItemStack(AllItems.ZINC_INGOT),
                PonderTimings.READING_TIME-(PonderTimings.READING_WINDOW/2)
        );
        scene.idle(PonderTimings.READING_WINDOW/2);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("This frequency is used when the Tracker Bar plays a note on that channel")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());
        
        scene.idle(PonderTimings.READING_WINDOW);
        
        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("When playing a note, it will activate any note links that match the note and assigned frequency")
                .placeNearTarget()
                .pointAt(trackerBar.getCenter());
        scene.idle(PonderTimings.seconds(2));
        
        ItemStack zincStack = new ItemStack(AllItems.ZINC_INGOT);
        
        PonderUtil.showMidiGuiSlot(scene, trackerBar.getCenter().add(0, 0.5, 0), Pointing.DOWN, zincStack,
                30
        );
        scene.idle(40);

        for (int i = 5; i <= 7; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 5).subtract(0, 0.35, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, 30);
        }
        scene.overlay().showControls(zincLinksVec.add(0, 0.15, 0), Pointing.DOWN, 30).withItem(zincStack);
        scene.idle(50);


        PonderUtil.displayGoggleHint(scene, trackerBar.getCenter(),
                "F4", 30,
                false, true);
        scene.idle(10);
        
        scene.overlay().showFilterSlotInput(zincLinksVec.add(1, -2, 0), 30);
        PonderUtil.displayGoggleHint(scene, zincLinksVec.add(1, 0, 0),
                "F4", 30,
                false, true);
        scene.idle(10);
        
        scene.world().toggleRedstonePower(util.select().position(f4DiapasonPos));
        scene.effects().indicateRedstone(f4DiapasonPos);
        scene.idle(30);
        scene.world().toggleRedstonePower(util.select().position(f4DiapasonPos));
        
        scene.idle(25);
        
        
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
