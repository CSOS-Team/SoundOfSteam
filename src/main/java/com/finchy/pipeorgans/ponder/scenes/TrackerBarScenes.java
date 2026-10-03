package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class TrackerBarScenes {

    public static void musicRollPlayback(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("tracker_bar", "Playing music rolls");
        scene.configureBasePlate(1, 1, 7);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos trackerBarPos = util.grid().at(4, 1, 3);
        Vec3 zincLinksVec = util.vector().blockSurface(util.grid().at(6, 1, 5), Direction.SOUTH)
                .add(0, 0, -3 / 16f);
        Vec3 brassLinksVec = util.vector().blockSurface(util.grid().at(2, 1, 5), Direction.SOUTH)
                .add(0, 0, -3 / 16f);
        
        BlockPos f4DiapasonPos = util.grid().at(7, 2, 6);
        BlockPos d4TrompettePos = util.grid().at(2, 2, 6);

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
        
        scene.idle(6);

        scene.addKeyframe();

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The Tracker Bar is used to play MIDI files from music rolls")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(trackerBarPos, Direction.WEST));

        scene.idle(PonderTimings.READING_WINDOW);

        scene.addKeyframe();
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Each channel in the MIDI file is represented by a redstone frequency")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(trackerBarPos, Direction.WEST));
        
        scene.idle(PonderTimings.READING_WINDOW/2);

        PonderUtil.showMidiGuiSlot(scene, trackerBarPos.getCenter().add(0, 0.5, 0), Pointing.DOWN, new ItemStack(AllItems.ZINC_INGOT), 1,
                PonderTimings.READING_TIME-(PonderTimings.READING_WINDOW/2)
        );
        scene.idle(PonderTimings.READING_WINDOW/2);

        scene.overlay().showText(PonderTimings.READING_TIME-20)
                .text("This frequency is used when the Tracker Bar plays a note on that MIDI channel")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(trackerBarPos, Direction.WEST));
        
        scene.idle(PonderTimings.READING_WINDOW-20);
        
        scene.addKeyframe();
        scene.overlay().showText(110)
                .text("When playing a note, it will activate any note links that match the note and assigned frequency")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(trackerBarPos, Direction.WEST));
        scene.idle(40);
        
        
        
        PonderUtil.showMidiGuiSlot(scene, trackerBarPos.getCenter().add(0, 0.5, 0), Pointing.DOWN, AllItems.ZINC_INGOT.asStack(), 1,
                30
        );
        scene.idle(40);

        for (int i = 5; i <= 7; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 5).subtract(0, 0.35, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, 30);
        }
        scene.overlay().showControls(zincLinksVec.add(0, 0.15, 0), Pointing.DOWN, 30).withItem(AllItems.ZINC_INGOT.asStack());
        scene.idle(50);
        
        scene.overlay().showText(30) // similar to goggle tooltip, but without actually showing the goggles
                .text("F4")
                .colored(PonderPalette.BLUE)
                .pointAt(trackerBarPos.getCenter())
                .placeNearTarget();
        scene.idle(40);
        
        scene.overlay().showFilterSlotInput(zincLinksVec.add(1, 0, 0), 30);
        scene.overlay().showText(30) // similar to goggle tooltip, but without actually showing the goggles
                .text("F4")
                .colored(PonderPalette.BLUE)
                .pointAt(zincLinksVec.add(1, 0, 0))
                .placeNearTarget();
        scene.idle(50);
        
        scene.world().toggleRedstonePower(util.select().position(f4DiapasonPos));
        scene.effects().indicateRedstone(f4DiapasonPos);
        scene.idle(30);
        scene.world().toggleRedstonePower(util.select().position(f4DiapasonPos));
        
        scene.idle(25);
        
        
        
        scene.addKeyframe();
        scene.overlay().showText(90)
                .text("Each channel can use a unique frequency to play different pipes")
                .placeNearTarget()
                .pointAt(util.vector().blockSurface(trackerBarPos, Direction.WEST));
        scene.idle(40);
        
        PonderUtil.showMidiGuiSlot(scene, trackerBarPos.getCenter().add(0, 0.5, 0), Pointing.DOWN, AllItems.BRASS_INGOT.asStack(), 2,
                30
        );

        for (int i = 1; i <= 3; i++) {
            Vec3 redstoneLinkVec = util.vector().topOf(i, 1, 5).subtract(0, 0.35, -0.3);
            scene.overlay().showFilterSlotInput(redstoneLinkVec, 30);
        }
        scene.overlay().showControls(brassLinksVec.add(0, 0.15, 0), Pointing.DOWN, 30).withItem(AllItems.BRASS_INGOT.asStack());
        scene.idle(50);

        scene.overlay().showText(30) // similar to goggle tooltip, but without actually showing the goggles
                .text("D4")
                .colored(PonderPalette.BLUE)
                .pointAt(trackerBarPos.getCenter())
                .placeNearTarget();

        scene.overlay().showFilterSlotInput(brassLinksVec, 30);
        scene.overlay().showText(30) // similar to goggle tooltip, but without actually showing the goggles
                .text("D4")
                .colored(PonderPalette.BLUE)
                .pointAt(brassLinksVec)
                .placeNearTarget();
        scene.idle(50);

        scene.world().toggleRedstonePower(util.select().position(d4TrompettePos));
        scene.effects().indicateRedstone(d4TrompettePos);
        scene.idle(30);
        scene.world().toggleRedstonePower(util.select().position(d4TrompettePos));
        
        scene.idle(25);
        
        scene.markAsFinished();
    }
}
