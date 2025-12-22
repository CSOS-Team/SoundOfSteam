package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class PipePlaybackPonder {
    public static void windchestController(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("windchest_controller", "Playing pipes via the Windchest Controller");
        scene.configureBasePlate(0, 0, 5);

        // Begin building animation by showing base layer
        PonderUtil.revealBasePlate(scene, util, PonderTimings.BUILD_STEP);

        BlockPos subbass = util.grid().at(1, 2, 3);
        BlockPos windchest = util.grid().at(1, 1, 3);
        BlockPos fan = util.grid().at(3, 1, 3);
        BlockPos controller = util.grid().at(2, 1, 3);

        BlockPos controllerLever = controller.subtract(new Vec3i(0, 0, 1));
        BlockPos windchestLever = windchest.subtract(new Vec3i(0, 0, 1));

        Vec3 controllerVec = util.vector().blockSurface(controller, Direction.NORTH);
        Vec3 windchestVec = util.vector().blockSurface(windchest, Direction.NORTH);

        // Reveal cog & shaft powering fan
        for (int i = 5; i >= 4; i--) {
            scene.world().showSection(util.select().position(i, 1, 3), Direction.DOWN);
            scene.idle(PonderTimings.BUILD_STEP);
        }

        PonderUtil.revealBlock(scene, util, fan, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, controller, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, windchest, PonderTimings.BUILD_STEP);
        PonderUtil.revealBlock(scene, util, subbass, PonderTimings.BUILD_FINISH);

        // TEXT 1

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The windchest controller is powered by an encased fan pushing air")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);

        scene.idle(PonderTimings.READING_BUFFER);
        scene.effects().rotationDirectionIndicator(fan);

        scene.idle(PonderTimings.afterBuffer());

        // TEXT 2

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The windchest must be placed with the red arrow facing the controller")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(windchestVec);

        scene.idle(PonderTimings.READING_WINDOW);

        // TEXT 3
        PonderUtil.revealBlock(scene, util, controllerLever, Direction.SOUTH, PonderTimings.BUILD_STEP);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The controller is activated with a redstone signal")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);

        scene.idle(PonderTimings.READING_BUFFER);
        scene.world().toggleRedstonePower(util.select().fromTo(controllerLever, controller));

        scene.idle(PonderTimings.afterBuffer());

        // TEXT 4
        PonderUtil.revealBlock(scene, util, windchestLever, Direction.SOUTH, PonderTimings.BUILD_STEP);

        scene.overlay().showText(PonderTimings.READING_TIME)
                .text("The windchest itself also activates with a redstone signal")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(windchestVec);

        scene.idle(PonderTimings.READING_BUFFER);
        scene.world().toggleRedstonePower(util.select().fromTo(windchestLever, subbass));

        scene.idle(PonderTimings.afterBuffer());

        scene.markAsFinished();
    }

    public static void boilerPipePlaying(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("boiler_pipe_playback", "Playing pipes with a boiler");
        scene.configureBasePlate(0, 0, 5);

        PonderUtil.revealBasePlate(scene, util, PonderTimings.BUILD_STEP);

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
}