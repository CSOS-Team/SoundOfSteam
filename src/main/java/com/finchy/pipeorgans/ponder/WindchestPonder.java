package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.Direction;

public class WindchestPonder {
    public static void controller(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.configureBasePlate(0, 0, 6);
        scene.title("windchest_controller", "Windchest Controller");

        scene.showBasePlate();
        scene.idle(10);

        scene.world().showSection(util.select().layersFrom(1), Direction.DOWN);
    }
}