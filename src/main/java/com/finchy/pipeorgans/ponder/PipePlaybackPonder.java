package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
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
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(5);

        BlockPos subbass = util.grid().at(1, 2, 3);
        BlockPos windchest = util.grid().at(1, 1, 3);
        BlockPos fan = util.grid().at(3, 1, 3);
        BlockPos controller = util.grid().at(2, 1, 3);

        BlockPos controllerLever = controller.subtract(new Vec3i(0, 0, 1));
        BlockPos windchestLever = windchest.subtract(new Vec3i(0, 0, 1));

        Vec3 controllerVec = util.vector().blockSurface(controller, Direction.NORTH);
        Vec3 windchestVec = util.vector().blockSurface(windchest, Direction.NORTH);

        // Load in cog & shaft powering fan
        for (int i = 5; i >= 4; i--) {
            scene.world().showSection(util.select().position(i, 1, 3), Direction.DOWN);
            scene.idle(5);
        }

        scene.world().showSection(util.select().position(fan), Direction.DOWN);
        scene.idle(5);

        scene.world().showSection(util.select().position(controller), Direction.DOWN);
        scene.idle(5);

        scene.world().showSection(util.select().position(windchest), Direction.DOWN);
        scene.idle(5);

        scene.world().showSection(util.select().position(subbass), Direction.DOWN);
        scene.idle(10);

        scene.overlay().showText(70)
                .text("The windchest controller is powered by an encased fan pushing air")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);

        scene.idle(30);
        scene.effects().rotationDirectionIndicator(fan);

        scene.idle(80);

        scene.overlay().showText(70)
                .text("The windchest must be placed with the red arrow facing the controller")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(windchestVec);

        scene.idle(80);

        scene.world().showSection(util.select().position(controllerLever), Direction.SOUTH);
        scene.idle(10);

        scene.overlay().showText(70)
                .text("The controller is activated with a redstone signal")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(controllerVec);

        scene.idle(20);
        scene.world().toggleRedstonePower(util.select().fromTo(controllerLever, controller));

        scene.idle(60);

        scene.world().showSection(util.select().position(windchestLever), Direction.SOUTH);
        scene.idle(10);
        scene.overlay().showText(70)
                .text("The windchest itself also activates with a redstone signal")
                .placeNearTarget()
                .attachKeyFrame()
                .pointAt(windchestVec);

        scene.idle(20);
        scene.world().toggleRedstonePower(util.select().fromTo(windchestLever, subbass));

        scene.idle(60);

        scene.markAsFinished();
    }
}