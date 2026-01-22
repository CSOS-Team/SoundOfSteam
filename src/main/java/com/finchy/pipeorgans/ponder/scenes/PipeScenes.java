package com.finchy.pipeorgans.ponder.scenes;

import com.finchy.pipeorgans.content.pipes.generic.ExtensionShapes;
import com.finchy.pipeorgans.content.pipes.generic.PipeSize;
import com.finchy.pipeorgans.init.AllBlocks;
import com.finchy.pipeorgans.ponder.ponderWrappers.PonderPipe;
import com.finchy.pipeorgans.ponder.PonderTimings;
import com.finchy.pipeorgans.ponder.PonderUtil;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PipeScenes {
    public static void pipeAdjusting(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("pipe_adjusting", "Adjusting a pipe's pitch");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        Selection tank = util.select().position(2, 1, 3);
        Selection boiler = util.select().position(1, 2, 3);
        BlockPos boilerLeverPos = util.grid().at(0, 2, 3);
        Selection boilerLever = util.select().position(boilerLeverPos);
        Selection campfire = util.select().position(1, 1, 3);
        BlockPos boilerPipePos = util.grid().at(1, 2, 2);
        PonderPipe<ExtensionShapes.Double> boilerPipe = new PonderPipe<>(
                scene, util,
                boilerPipePos, PipeSize.MEDIUM, Direction.NORTH, true, false,
                0,
                AllBlocks.DIAPASON.get(), AllBlocks.DIAPASON_EXTENSION.get(),
                PonderPipe.Transition.NONE
        );

        BlockPos controllerPos = util.grid().at(4, 1, 2);
        Selection controller = util.select().position(controllerPos);
        BlockPos windchestPos = util.grid().at(3, 1, 2);
        Selection windchest = util.select().position(windchestPos);
        Selection controllerLever = util.select().position(4, 1, 1);
        BlockPos windchestLeverPos = util.grid().at(3, 1, 1);
        Selection windchestLever = util.select().position(windchestLeverPos);
        BlockPos fanPos = util.grid().at(5, 1, 2);
        Selection fan = util.select().position(fanPos);
        BlockPos windchestPipePos = util.grid().at(3, 2, 2);
        PonderPipe<ExtensionShapes.Double> windchestPipe = new PonderPipe<>(
                scene, util,
                boilerPipePos, PipeSize.MEDIUM, Direction.NORTH, false, false,
                0,
                AllBlocks.DIAPASON.get(), AllBlocks.DIAPASON_EXTENSION.get(),
                PonderPipe.Transition.NONE
        );

        scene.world().showSection(controller, Direction.DOWN); // reveal windchest controller
        scene.idle(PonderTimings.BUILD_STEP);

        scene.world().showSection(windchest, Direction.DOWN); // reveal windchest
        scene.idle(PonderTimings.BUILD_STEP);

        ElementLink<WorldSectionElement> tankElement = scene.world().showIndependentSection(tank, Direction.DOWN); // reveal non-boiler fluid tank
        scene.world().moveSection(tankElement, util.vector().of(-1, 0, 0), 0);
        scene.idle(PonderTimings.BUILD_STEP);

        windchestPipe.showPipe(Direction.DOWN); // reveal pipe attaching to windchest from the top
        scene.idle(PonderTimings.BUILD_STEP);

        ElementLink<WorldSectionElement> boilerPipeElement = scene.world().showIndependentSection( // reveal pipe attaching to tank from the front
                boilerPipe.getMaximumPipeSelection(), Direction.SOUTH);
        scene.world().moveSection(boilerPipeElement, util.vector().of(0, -1, 0), 0);
        scene.idle(15);

        scene.world().moveSection(tankElement, util.vector().of(0, -1000, 0), 0); // hide tank
        scene.world().hideIndependentSection(tankElement, null);
        ElementLink<WorldSectionElement> boilerElement = scene.world().showIndependentSectionImmediately(boiler); // reveal boiler tank in its place
        scene.world().moveSection(boilerElement, util.vector().of(0, -1, 0), 0);
        scene.effects().indicateSuccess(util.grid().at(1, 1, 3)); // show happy particle bits
        scene.idle(25);

        PonderUtil.displayTextAndWait(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH),
                "Pipes can be placed on Windchests and Fluid Tanks", true, true,
                70, 20);

        scene.world().showSection(fan, Direction.WEST); // reveal fan sliding in west
        scene.idle(PonderTimings.BUILD_STEP);
        scene.effects().rotationDirectionIndicator(fanPos); // show spinny particle bits
        scene.world().showSection(controllerLever, Direction.SOUTH); // reveal windchest controller lever
        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().toggleRedstonePower(util.select().fromTo(controllerPos, windchestPos)); // toggle windchest and controller power
        scene.world().toggleRedstonePower(controllerLever); // toggle controller lever power
        scene.world().showSection(windchestLever, Direction.SOUTH); // reveal windchest lever
        scene.idle(PonderTimings.BUILD_FINISH);

        PonderUtil.displayTextAndWait(scene, util.vector().blockSurface(windchestPos, Direction.NORTH),
                "If the Windchest is configured correctly...", true, true,
                70, 20);

        scene.world().moveSection(boilerElement, util.vector().of(0, 1, 0), 15); // move boiler up 1 block
        scene.world().moveSection(boilerPipeElement, util.vector().of(0, 1, 0), 15); // move boiler pipe up 1 block
        scene.idle(15);
        ElementLink<WorldSectionElement> campfireElement = scene.world().showIndependentSection(
                campfire, Direction.NORTH);
        scene.world().showSection(boilerLever, Direction.EAST); // reveal boiler lever
        scene.idle(15);

        PonderUtil.displayTextAndWait(scene, util.vector().blockSurface(util.grid().at(1, 1, 3), Direction.WEST),
                "...or, alternatively, if the tank receives sufficient heat...", true, true,
                70, 20);

        scene.world().toggleRedstonePower(boilerLever); // toggle boiler lever power
        scene.world().toggleRedstonePower(boilerPipe.getMaximumPipeSelection()); // toggle boiler pipe power
        scene.effects().indicateRedstone(boilerLeverPos); // show redstone particle bits

        scene.world().toggleRedstonePower(windchestLever); // toggle windchest lever power
        scene.world().toggleRedstonePower(windchestPipe.getMaximumPipeSelection()); // toggle windchest pipe power
        scene.effects().indicateRedstone(windchestLeverPos); // show redstone particle bits
        scene.idle(PonderTimings.BUILD_FINISH);

        PonderUtil.displayTextAndWait(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.NORTH),
                "...the Pipe will play a note when activated", true, true);

        scene.world().toggleRedstonePower(boilerLever); // toggle boiler lever power
        scene.world().toggleRedstonePower(boilerPipe.getMaximumPipeSelection()); // toggle boiler pipe power

        scene.idle(PonderTimings.BUILD_STEP);
        scene.world().hideSection(fan, Direction.EAST); // hide fan
        scene.world().hideSection(windchest, Direction.EAST); // hide windchest
        scene.world().hideSection(controller, Direction.EAST); // hide windchest controller
        scene.world().hideSection(windchestLever, Direction.EAST); // hide windchest lever
        scene.world().hideSection(controllerLever, Direction.EAST); // hide controller lever
        scene.world().hideSection(windchestPipe.getMaximumPipeSelection(), Direction.EAST); // hide pipe
        scene.world().hideSection(boilerLever, Direction.WEST); // hide boiler lever

        scene.world().moveSection(campfireElement, util.vector().of(1, 0, 0), 15); // move campfire over 1 block
        scene.world().moveSection(boilerElement, util.vector().of(1, 0, 0), 15); // move boiler over 1 block
        scene.world().moveSection(boilerPipeElement, util.vector().of(1, 0, 0), 15); // move pipe over 1 block
        scene.idle(25);

        // PITCH

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, 50)
                .withItem(AllBlocks.DIAPASON.asStack())
                .rightClick();

        scene.idle(6);
        boilerPipe.incrementPipeHeight(PonderPipe.Transition.NONE); // increase pipe length
        scene.idle(20);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Use the corresponding Pipe item on the block to lower its pitch")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.NORTH))
                .placeNearTarget();
        scene.idle(40);

        // increase pipe length 4 more times
        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, 2)
                .withItem(AllBlocks.DIAPASON.asStack())
                .rightClick();
        scene.idle(6);
        boilerPipe.incrementPipeHeight();
        scene.idle(4);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, 2)
                .withItem(AllBlocks.DIAPASON.asStack())
                .rightClick();
        scene.idle(6);
        boilerPipe.incrementPipeHeight();
        scene.idle(4);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, 2)
                .withItem(AllBlocks.DIAPASON.asStack())
                .rightClick();
        scene.idle(6);
        boilerPipe.incrementPipeHeight();
        scene.idle(4);

        scene.overlay().showControls(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, 2)
                .withItem(AllBlocks.DIAPASON.asStack())
                .rightClick();
        scene.idle(6);
        boilerPipe.incrementPipeHeight();
        scene.idle(20);

        // OCTAVE

        PonderUtil.showWrenchInteraction(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, false, false, 50);
        scene.idle(6);
        boilerPipe.cyclePipeSize(); // large
        scene.idle(20);

        scene.overlay().showText(70)
                .attachKeyFrame()
                .text("Cycle between five different octaves using a Wrench")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 3), Direction.NORTH))
                .placeNearTarget();

        scene.idle(40);
        PonderUtil.showWrenchInteraction(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, false, false, 4);
        scene.idle(6);
        boilerPipe.cyclePipeSize(); // huge

        scene.idle(15);
        PonderUtil.showWrenchInteraction(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, false, false, 4);
        scene.idle(6);
        boilerPipe.cyclePipeSize(); // tiny

        scene.idle(15);
        PonderUtil.showWrenchInteraction(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, false, false, 4);
        scene.idle(6);
        boilerPipe.cyclePipeSize(); // small

        scene.idle(15);
        PonderUtil.showWrenchInteraction(scene, util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.EAST), Pointing.RIGHT, false, false, 4);
        scene.idle(6);
        boilerPipe.cyclePipeSize(); // medium
        scene.idle(40);

        scene.overlay().showText(80)
                .attachKeyFrame()
                .colored(PonderPalette.BLUE)
                .text("Engineer's Goggles can help to find out the current pitch and octave of a Pipe")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 2), Direction.DOWN))
                .placeNearTarget();
        scene.idle(70);

        PonderUtil.displayGoggleHint(scene, util.grid().at(2, 2, 2).getCenter(),
                "C#4", 60,
                true, true);

        scene.markAsFinished();
    }

    public static void pipeSwapping(SceneBuilder builder, SceneBuildingUtil util) {
        CreateSceneBuilder scene = new CreateSceneBuilder(builder);

        scene.title("pipe_swapping", "Swapping different pipes");
        scene.configureBasePlate(0, 0, 5);

        scene.showBasePlate();
        scene.idle(PonderTimings.BUILD_STEP);

        PonderLevel level = scene.getScene().getWorld();

        Selection verticalBlocker = util.select().position(2, 6, 2);
        Selection horizontalBlocker = util.select().position(2, 2, 0);
        Selection boilerCampfire = util.select().fromTo(2, 1, 3, 2, 2, 3);

        BlockPos pipePos = util.grid().at(2, 2, 2);

        Selection diapason = util.select().fromTo(2, 2, 2, 2, 4, 2);
        Selection subbass = util.select().fromTo(0, 2, 2, 0, 6, 2);
        Selection voxHumana = util.select().fromTo(4, 2, 2, 4, 3, 2);
        Selection chamade = util.select().fromTo(6, 2, 2, 6, 2, 0);
        Selection trompette = util.select().fromTo(8, 2, 2, 8, 4, 2);

        scene.world().showSection(boilerCampfire, Direction.DOWN); // reveal boiler and campfire
        scene.idle(PonderTimings.BUILD_STEP);
        ElementLink<WorldSectionElement> diapasonElement = scene.world().showIndependentSection(diapason, Direction.SOUTH); // reveal diapason
        scene.idle(20);

        PonderUtil.displayTextAndWait(scene, util.vector().blockSurface(pipePos, Direction.WEST),
                "You can instantly change the type of a Pipe by using another Pipe's item on it", true, true);

        scene.overlay().showControls(util.vector().blockSurface(pipePos, Direction.EAST), Pointing.RIGHT, 50)
                .withItem(AllBlocks.TROMPETTE.asStack())
                .rightClick();
        scene.idle(6);
        scene.world().moveSection(diapasonElement, util.vector().of(0, -1000, 0), 0); // hide diapason
        scene.world().hideIndependentSection(diapasonElement, null);
        ElementLink<WorldSectionElement> trompetteElement = scene.world().showIndependentSectionImmediately(trompette); // instantly swap for trompette
        scene.world().moveSection(trompetteElement, util.vector().of(-6, 0, 0), 0);

        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos, AllBlocks.DIAPASON.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(), AllBlocks.DIAPASON.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(2), AllBlocks.DIAPASON.getDefaultState()));

        scene.idle(40);

        ElementLink<WorldSectionElement> verticalBlockerElement = scene.world().showIndependentSection(verticalBlocker, Direction.WEST); // reveal vertical blocker
        scene.idle(PonderTimings.BUILD_FINISH);
        scene.overlay().showOutline(PonderPalette.BLUE, new Object(), util.select().fromTo(2, 2, 2, 2, 5, 2), 70); // draw blue outline
        scene.overlay().showOutlineWithText(verticalBlocker, 60) // draw red outline and text
                .colored(PonderPalette.RED)
                .pointAt(util.vector().blockSurface(util.grid().at(2, 6, 2), Direction.WEST))
                .attachKeyFrame()
                .placeNearTarget()
                .text("However, if the new pipe is too long, blocks can get in the way");
        scene.idle(50);
        scene.world().moveSection(verticalBlockerElement, util.vector().of(0, -1000, 0), 0); // hide vertical blocker
        scene.world().hideIndependentSection(verticalBlockerElement, null);
        scene.addInstruction(s ->
                s.getWorld().addBlockDestroyEffects(util.grid().at(2, 6, 2), com.simibubi.create.AllBlocks.ANDESITE_CASING.getDefaultState()));

        scene.idle(20);
        scene.overlay().showText(60)
                .attachKeyFrame()
                .text("To fix this, simply remove any blocks that are in the way.")
                .pointAt(util.vector().blockSurface(util.grid().at(2, 6, 2), Direction.WEST))
                .placeNearTarget();
        scene.idle(20);

        scene.overlay().showControls(util.vector().blockSurface(pipePos, Direction.EAST), Pointing.RIGHT, 50)
                .withItem(AllBlocks.TROMPETTE.asStack())
                .rightClick();

        scene.idle(6);
        scene.world().moveSection(trompetteElement, util.vector().of(0, -1000, 0), 0); // hide trompette
        scene.world().hideIndependentSection(trompetteElement, null);
        ElementLink<WorldSectionElement> subbassElement = scene.world().showIndependentSectionImmediately(subbass); // instantly swap for subbass
        scene.world().moveSection(subbassElement, util.vector().of(2, 0, 0), 0);

        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos, AllBlocks.TROMPETTE.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(), AllBlocks.TROMPETTE.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(2), AllBlocks.TROMPETTE.getDefaultState()));
        scene.idle(60);

        ElementLink<WorldSectionElement> horizontalBlockerElement = scene.world().showIndependentSection(horizontalBlocker, Direction.WEST); // reveal horizontal blocker
        scene.idle(PonderTimings.BUILD_FINISH);
        scene.overlay().showOutline(PonderPalette.BLUE, new Object(), util.select().fromTo(2, 2, 2, 2, 2, 1), 70); // draw blue outline
        scene.overlay().showOutlineWithText(horizontalBlocker, 60) // draw red outline and text
                .colored(PonderPalette.RED)
                .pointAt(util.vector().blockSurface(util.grid().at(2, 2, 0), Direction.WEST))
                .attachKeyFrame()
                .placeNearTarget()
                .text("The same goes for horizontal pipes");
        scene.idle(50);

        scene.world().moveSection(horizontalBlockerElement, util.vector().of(0, -1000, 0), 0); // hide horizontal blocker
        scene.world().hideIndependentSection(horizontalBlockerElement, null);
        scene.addInstruction(s ->
                s.getWorld().addBlockDestroyEffects(util.grid().at(2, 2, 0), com.simibubi.create.AllBlocks.ANDESITE_CASING.getDefaultState()));
        scene.idle(20);

        scene.overlay().showControls(util.vector().blockSurface(pipePos, Direction.EAST), Pointing.RIGHT, 50)
                .withItem(AllBlocks.CHAMADE.asStack())
                .rightClick();

        scene.idle(6);
        scene.world().moveSection(subbassElement, util.vector().of(0, -1000, 0), 0); // hide subbass
        scene.world().hideIndependentSection(subbassElement, null);
        ElementLink<WorldSectionElement> chamadeElement = scene.world().showIndependentSectionImmediately(chamade); // instantly swap for chamade
        scene.world().moveSection(chamadeElement, util.vector().of(-4, 0, 0), 0);

        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos, AllBlocks.SUBBASS.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(), AllBlocks.SUBBASS.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(2), AllBlocks.SUBBASS.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(3), AllBlocks.SUBBASS.getDefaultState()));
        scene.addInstruction(s -> s.getWorld().addBlockDestroyEffects(pipePos.above(4), AllBlocks.SUBBASS.getDefaultState()));
        scene.idle(40);

        scene.markAsFinished();
    }
}
