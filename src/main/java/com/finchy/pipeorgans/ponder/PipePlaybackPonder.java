package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import com.finchy.pipeorgans.content.pipes.generic.EExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.EPipeSizes;
import com.finchy.pipeorgans.init.AllBlocks;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class PipePlaybackPonder {
    public static void windchestController(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("windchest_controller", "Playing pipes with Windchests");
        scene.configureBasePlate(0, 0, 7);

        // Begin building animation by showing base layer
        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos diapason1 = util.grid().at(4, 2, 4);
        BlockPos diapason2 = util.grid().at(3, 2, 4);
        BlockPos diapason3 = util.grid().at(2, 2, 4);

        BlockPos windchest1 = util.grid().at(4, 1, 4);
        BlockPos windchest2 = util.grid().at(3, 1, 4);
        BlockPos windchest3 = util.grid().at(2, 1, 4);
        BlockPos controller = util.grid().at(5, 1, 4);
        BlockPos fan = util.grid().at(6, 1, 4);

        BlockPos controllerLever = controller.north();
        BlockPos redstoneBlock = diapason1.south();
        BlockPos repeater = windchest2.north();
        BlockPos windchestLever = repeater.north();
        BlockPos redstoneLink = windchest3.north();

        Vec3 controllerVec = util.vector().blockSurface(controller, Direction.NORTH);
        Vec3 windchestVec = util.vector().blockSurface(windchest2, Direction.NORTH);
        Vec3 pipeVec = util.vector().blockSurface(diapason2, Direction.UP);
        Vec3 fanVec = util.vector().blockSurface(fan, Direction.NORTH);

        PonderUtil.revealBlock(scene, util, controller, PonderTimings.BUILD_STEP); // reveal windchest controller
        for (int x=4; x>=2; x--) { // reveal windchests and attached pipes in columns
            PonderUtil.revealBlocks(scene, util, util.select().fromTo(x, 1, 4, x, 3, 4), PonderTimings.BUILD_STEP);
        }
        scene.idle(PonderTimings.BUILD_STEP);

        // TEXT 1
        scene.overlay().showText(60) // show text for 3 seconds
                .text("Windchests allow you to toggle several pipes at once")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);
        scene.idle(80); // wait for 1 second after the text disappears

        // TEXT 2
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The Windchests must be placed with the red arrow pointing toward the Windchest Controller")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(windchestVec);
        scene.idle(PonderTimings.READING_WINDOW);

        scene.world().showSection(util.select().position(fan), Direction.WEST); // reveal fan sliding in west
        scene.world().showSection(util.select().fromTo(7, 0, 5, 7, 1, 4), Direction.WEST); // reveal cogs sliding in west

        scene.idle(PonderTimings.BUILD_FINISH);
        scene.effects().rotationDirectionIndicator(fan); // show spinny particle bits

        // TEXT 3
        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Then place a fan blowing air into the Windchest Controller")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(fanVec);
        scene.idle(PonderTimings.READING_WINDOW);

        // TEXT 4
        PonderUtil.revealBlock(scene, util, controllerLever, Direction.SOUTH, PonderTimings.BUILD_STEP); // reveal the controller's lever

        scene.overlay().showText(50) // show text for 2.5 seconds
                .text("When the Controller is powered by redstone...")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);
        scene.idle(70); // wait for 1 second after the text disappears

        scene.world().toggleRedstonePower(util.select().fromTo(controllerLever, controller)); // activate controller lever and controller
        scene.world().toggleRedstonePower(util.select().fromTo(windchest1, windchest3)); // activate the windchests

        scene.idle(PonderTimings.READING_BUFFER);

        // TEXT 5
        scene.overlay().showText(50) // show text for 2.5 seconds
                .text("... its attached Windchests will activate")
                .placeNearTarget()
                .pointAt(windchestVec);
        scene.idle(70); // wait for 1 second after the text disappears


        // TEXT 6
        scene.overlay().showText(80) // show text for 4 seconds
                .text("Now, any pipes attached to the Windchests will sound when powered by redstone")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(pipeVec);
        scene.idle(PonderTimings.READING_BUFFER); // wait for 1 second before activating pipes

        scene.world().showSection(util.select().position(redstoneBlock), Direction.NORTH); // reveal redstone block sliding in behind diapason1
        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().toggleRedstonePower(util.select().position(diapason1)); // activate diapason1
        scene.idle(20); // wait 1 second

        PonderUtil.revealBlock(scene, util, windchestLever, PonderTimings.BUILD_STEP); // reveal lever
        PonderUtil.revealBlock(scene, util, repeater, PonderTimings.BUILD_FINISH); // reveal repeater
        scene.world().toggleRedstonePower(util.select().fromTo(windchestLever, repeater)); // activate repeater and lever
        scene.idle(5); // wait 1 redstone tick
        scene.world().toggleRedstonePower(util.select().position(diapason2)); // activate diapason2
        scene.idle(20); // wait 1 second

        PonderUtil.revealBlock(scene, util, redstoneLink, PonderTimings.BUILD_FINISH); // reveal redstone link
        scene.world().toggleRedstonePower(util.select().position(redstoneLink)); // activate redstone link
        scene.world().toggleRedstonePower(util.select().position(diapason3)); // activate diapason3
        scene.idle(40); // wait 2 seconds

        // TEXT 6
        scene.overlay().showText(80) // show text for 4 seconds
                .text("... as long as the Windchest Controller has power")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(pipeVec);
        scene.idle(PonderTimings.READING_BUFFER); // wait for 1 second before deactivating windchest

        scene.world().toggleRedstonePower(util.select().fromTo(controllerLever, controller)); // deactivate controller lever and controller
        scene.world().toggleRedstonePower(util.select().fromTo(windchest1, windchest3)); // deactivate the windchests

        scene.idle(40); // wait for 3 seconds

        scene.markAsFinished();
    }

    public static void boilerPipePlaying(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("boiler_pipe_playback", "Playing pipes with a boiler");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        BlockPos magma = util.grid().at(2, 1, 2);
        BlockPos fluidTank = util.grid().at(2, 2, 2);
        BlockPos diapason = util.grid().at(2, 3, 2);
        BlockPos fluidTankLever = fluidTank.subtract(new Vec3i(0, 0, 1));

        PonderUtil.revealBlock(scene, util, magma, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, fluidTank, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, diapason, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, fluidTankLever,  Direction.SOUTH, PonderTimings.BUILD_STEP);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Pipes can be played by placing them on top of a boiler")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(diapason.getCenter());

        scene.idle(PonderTimings.READING_WINDOW);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("Then to activate the pipe a redstone signal must be passed through")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(fluidTank.getCenter());

        scene.idle(PonderTimings.READING_BUFFER);
        scene.world().toggleRedstonePower(util.select().fromTo(fluidTankLever, diapason));

        scene.idle(PonderTimings.afterBuffer());

        scene.markAsFinished();
    }

    public static void pipeAdjusting(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("pipe_adjusting", "Adjusting a pipe's pitch");
        scene.configureBasePlate(1, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        Selection windchest = util.select().fromTo(2, 1, 2, 3, 1, 2);
        Selection fan = util.select().position(1, 1, 2);
        Selection cogs = util.select().fromTo(0, 1, 2, 0, 0, 3);
        Selection torch = util.select().position(2, 1, 1);
        Selection lever = util.select().position(3, 1, 1);
        BlockPos diapason = util.grid().at(3, 2, 2);

        PonderPipe<EExtensionShapes.DoubleShape> pipe = new PonderPipe<>(
                scene, util,
                diapason,
                0, EPipeSizes.PipeSize.MEDIUM,
                AllBlocks.DIAPASON.get(), AllBlocks.DIAPASON_EXTENSION.get(),
                new PonderPipe.Transition(PonderTimings.INTERACTION_DISPLAY_TIME, PonderTimings.BUILD_FINISH)
                );

        scene.world().showSection(windchest, Direction.DOWN);
        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().showSection(fan, Direction.EAST);
        scene.idle(PonderTimings.BUILD_STEP);
        pipe.showPipe(Direction.DOWN);
        scene.world().showSection(cogs, Direction.EAST);
        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().showSection(torch, Direction.DOWN);
        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().showSection(lever, Direction.SOUTH);
        scene.idle(PonderTimings.BUILD_STEP);

        scene.idle(PonderTimings.BUILD_FINISH);

        // Text 1
        PonderUtil.displayTextAndWait(scene, diapason.getCenter(), "After placing, adjust a pipe's octave by using a wrench on it to change its size", true, true);
        pipe.showPitchOverlay(PonderTimings.INTERACTION_DISPLAY_TIME);
        scene.idle(PonderTimings.READING_BUFFER);

        for (int i = 0; i < 5; i++) {
            PonderUtil.showWrenchInteraction(scene, diapason.getCenter().add(0,.5f, 0), Pointing.DOWN, false, false);
            scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
            pipe.cyclePipeSize(true);
            scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);
        }
        scene.world().restoreBlocks(util.select().position(diapason));

        // Test 1.5
        PonderUtil.displayTextAndWait(scene, diapason.getCenter(), "A pipe's size determines its octave. Increasing the pipe diameter lowers the octave, and vice versa", false, true);

        // Text 2
        PonderUtil.displayTextAndWait(scene, diapason.getCenter(), "To adjust a pipe's pitch class, use its block on it to make it taller", true, true);
        for (int i = 0; i < 3; i++) {
            scene.overlay().showControls(diapason.getCenter().add(.5f, 0, -.5f), Pointing.RIGHT, PonderTimings.INTERACTION_DISPLAY_TIME)
                    .rightClick()
                    .withItem(AllBlocks.DIAPASON.asStack());
            scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
            pipe.incrementPipeHeight(true);
            scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);
        }

        PonderUtil.displayTextAndWait(scene, diapason.getCenter(), "Crouch and use the Wrench on the extensions to remove them, making the pipe shorter", true, true);
        PonderUtil.showWrenchInteraction(scene, util.vector().topOf(diapason).add(-.5f, pipe.getExtensionRealHeight() - pipe.getExtensionCenterOffset(), .5f), Pointing.LEFT, true, false);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME / 2);
        pipe.decrementPipeHeight(true);
        scene.idle(PonderTimings.INTERACTION_DISPLAY_TIME);

        scene.overlay().showOutlineWithText(util.select().position(diapason.above()), PonderTimings.READING_TIME)
                .text("You can also just break the extension blocks")
                .placeNearTarget()
                .pointAt(diapason.above().getCenter());
        scene.idle(PonderTimings.READING_WINDOW);
        pipe.destroyTopPipeExtension();

        PonderUtil.displayTextAndWait(scene, diapason.getCenter(), "Increasing the pipe length lowers the pitch, and vice versa", false, true);

        PonderUtil.displayGoggleHint(scene, diapason.getCenter(), "The Engineer's Goggles show you the note a pipe will play", PonderTimings.READING_TIME, true, true);

        scene.world().restoreBlocks(util.select().fromTo(diapason, diapason.above(6)));

        scene.markAsFinished();
    }
}