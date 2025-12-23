package com.finchy.pipeorgans.ponder;

import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

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

    public static void revealBlockWithOffset(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, Vec3 revealOffset, int idle) {
        ElementLink<WorldSectionElement> blockElement = scene.world().makeSectionIndependent(util.select().position(pos));

        scene.world().moveSection(blockElement, revealOffset, idle);

        scene.idle(idle);
    }
}
