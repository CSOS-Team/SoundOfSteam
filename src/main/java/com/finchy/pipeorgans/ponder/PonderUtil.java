package com.finchy.pipeorgans.ponder;

import com.finchy.pipeorgans.ponder.element.MidiGuiSlotElement;
import com.finchy.pipeorgans.ponder.instruction.ShowMidiGuiSlotInstruction;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.CreateSceneBuilder;
import net.createmod.ponder.api.element.ElementLink;
import net.createmod.ponder.api.element.WorldSectionElement;
import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.PonderPalette;
import net.createmod.ponder.api.element.InputElementBuilder;
import net.createmod.ponder.api.element.TextElementBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.api.scene.Selection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
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

    public static void revealBlocks(CreateSceneBuilder scene, SceneBuildingUtil util, Selection selection, int idle) {
        scene.world().showSection(selection, Direction.DOWN);
        scene.idle(idle);
    }

    public static void revealBlockWithOffset(CreateSceneBuilder scene, SceneBuildingUtil util, BlockPos pos, Vec3 revealOffset, int idle) {
        ElementLink<WorldSectionElement> blockElement = scene.world().makeSectionIndependent(util.select().position(pos));

        scene.world().moveSection(blockElement, revealOffset, idle);

        scene.idle(idle);
    }

    public static void showWrenchInteraction(CreateSceneBuilder scene, Vec3 pos, Pointing dir, boolean shift, boolean control, int duration) {
        InputElementBuilder builder = scene.overlay().showControls(pos, dir, duration)
                .rightClick()
                .withItem(AllItems.WRENCH.asStack());
        if (shift)
            builder.whileSneaking();
        if (control)
            builder.whileCTRL();
    }

    public static void showWrenchInteraction(CreateSceneBuilder scene, Vec3 pos, Pointing dir, boolean shift, boolean control) {
        showWrenchInteraction(scene, pos, dir, shift, control, PonderTimings.INTERACTION_DISPLAY_TIME);
    }

    public static void showMidiGuiSlot(CreateSceneBuilder scene, Vec3 pos, Pointing dir, ItemStack item, int duration) {
        MidiGuiSlotElement midiGuiSlotElement = new MidiGuiSlotElement(pos, dir, item);
        scene.addInstruction(new ShowMidiGuiSlotInstruction(midiGuiSlotElement, duration));
    }

    public static void displayText(CreateSceneBuilder scene, Vec3 pos, String text, boolean attachKeyFrame, boolean placeNearTarget, int duration) {
        TextElementBuilder textBuilder = scene.overlay().showText(duration)
                .text(text)
                .pointAt(pos);

        if (attachKeyFrame)
            textBuilder.attachKeyFrame();
        if (placeNearTarget)
            textBuilder.placeNearTarget();
    }

    public static void displayTextAndWait(CreateSceneBuilder scene, Vec3 pos, String text, boolean attachKeyFrame, boolean placeNearTarget, int duration, int buffer) {
        displayText(scene, pos, text, attachKeyFrame, placeNearTarget, duration);
        scene.idle(duration + buffer);
    }

    public static void displayTextAndWait(CreateSceneBuilder scene, Vec3 pos, String text, boolean attachKeyFrame, boolean placeNearTarget) {
        displayTextAndWait(scene, pos, text, attachKeyFrame, placeNearTarget, PonderTimings.READING_TIME, PonderTimings.READING_BUFFER);
    }

    public static void displayGoggleHint(CreateSceneBuilder scene, Vec3 pos, String text, int duration, boolean attachKeyFrame, boolean placeNearTarget) {
        scene.overlay().showControls(pos.add(0, -.5f, 0), Pointing.UP, duration)
                .withItem(AllItems.GOGGLES.asStack());
        scene.idle(6);

        TextElementBuilder textBuilder = scene.overlay().showText(duration - 10)
                .text(text)
                .colored(PonderPalette.BLUE)
                .pointAt(pos.add(-.5f, 0, .5f));

        if (attachKeyFrame)
            textBuilder.attachKeyFrame();

        if (placeNearTarget)
            textBuilder.placeNearTarget();

        scene.idle(duration);
    }
}
