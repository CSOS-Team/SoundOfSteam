package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public final class PonderUtil {
    private PonderUtil() {}

    public static void revealBlock(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, int idle) {
        scene.world().showSection(util.select().position(pos), Direction.DOWN);
        scene.idle(idle);
    }

    public static void revealBlock(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, Direction dir, int idle) {
        scene.world().showSection(util.select().position(pos), dir);
        scene.idle(idle);
    }

    public static void revealBasePlate(CreateSceneBuilder scene, SceneBuildingUtil util, int idle) {
        scene.world().showSection(util.select().layer(0), Direction.UP);
        scene.idle(idle);
    }
}
