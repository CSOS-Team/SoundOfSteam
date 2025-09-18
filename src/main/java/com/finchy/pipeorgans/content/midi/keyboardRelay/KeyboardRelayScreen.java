package com.finchy.pipeorgans.content.midi.keyboardRelay;

import com.finchy.pipeorgans.PipeOrgans;
import com.finchy.pipeorgans.init.AllBlocks;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.createmod.catnip.gui.element.GuiGameElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class KeyboardRelayScreen extends AbstractSimiContainerScreen<KeyboardRelayMenu> {

    private static final ResourceLocation GUI_TEXTURE = PipeOrgans.asResource("textures/gui/keyboard_relay.png");
    private static final int GUI_WIDTH = 193;
    private static final int GUI_HEIGHT = 139;

    private final ItemStack renderedItem = AllBlocks.KEYBOARD_RELAY.asStack();

    public KeyboardRelayScreen(KeyboardRelayMenu container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    @Override
    protected void init() {
        super.init();
        setWindowSize(GUI_WIDTH, GUI_HEIGHT + 4 + AllGuiTextures.PLAYER_INVENTORY.getHeight());
        setWindowOffset(-11, 8);
        super.init();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, leftPos+(178-font.width(title))/2, topPos+2, 0x505050, false);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        int invX = getLeftOfCentered(AllGuiTextures.PLAYER_INVENTORY.getWidth())-15;
        int invY = topPos + GUI_HEIGHT + 4;
        renderPlayerInventory(pGuiGraphics, invX, invY);

        pGuiGraphics.blit(GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        GuiGameElement.of(renderedItem)
                .<GuiGameElement.GuiRenderBuilder>at(leftPos + GUI_WIDTH, topPos + GUI_HEIGHT - 40, -200)
                .scale(3)
                .render(pGuiGraphics);

        int x = 31;
        int y = 24;
        int slot = 0;

        for (int column=0; column<4; column++) {
            for (int row=0; row<4; row++) {
                (slot++, column*42 + 31, row*20 + 24);
            }
        }
    }
}
