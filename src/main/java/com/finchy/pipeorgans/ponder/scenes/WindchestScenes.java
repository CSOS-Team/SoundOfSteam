package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class WindchestScenes {
    public static void windchests(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("windchests", "Using Windchests");
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
                .pointAt(windchestVec);
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

        // TEXT 7
        scene.overlay().showText(PonderTimings.seconds(4)) // show text for 4 seconds
                .text("... as long as the Windchest Controller has power")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);
        scene.idle(PonderTimings.READING_BUFFER); // wait for 1 second before deactivating windchest

        scene.world().toggleRedstonePower(util.select().fromTo(controllerLever, controller)); // deactivate controller lever and controller
        scene.world().toggleRedstonePower(util.select().fromTo(windchest1, windchest3)); // deactivate the windchests

        scene.idle(PonderTimings.seconds(3)); // wait for 3 seconds

        scene.markAsFinished();
    }
}