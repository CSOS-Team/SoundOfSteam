package com.finchy.pipeorgans.ponder.instruction;

import com.finchy.pipeorgans.ponder.element.MidiGuiSlotElement;
import net.createmod.ponder.foundation.PonderScene;
import net.createmod.ponder.foundation.instruction.FadeInOutInstruction;

public class ShowMidiGuiSlotInstruction extends FadeInOutInstruction {

    private final MidiGuiSlotElement element;

    public ShowMidiGuiSlotInstruction(MidiGuiSlotElement element, int ticks) {
        super(ticks);
        this.element = element;
    }

    @Override
    protected void show(PonderScene scene) {
        scene.addElement(element);
        element.setVisible(true);
    }

    @Override
    protected void hide(PonderScene scene) {
        element.setVisible(false);
    }

    @Override
    protected void applyFade(PonderScene scene, float fade) {
        element.setFade(fade);
    }
}
