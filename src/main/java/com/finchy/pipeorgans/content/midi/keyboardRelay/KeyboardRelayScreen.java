package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class KeyboardRelayScreen extends AbstractSimiContainerScreen<KeyboardRelayMenu> {

    public KeyboardRelayScreen(KeyboardRelayMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

    }
}
