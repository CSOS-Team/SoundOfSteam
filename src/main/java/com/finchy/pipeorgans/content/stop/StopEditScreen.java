package com.finchy.pipeorgans.content.stop;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.StopActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class StopEditScreen extends AbstractSimiContainerScreen<StopEditMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_SLOT_BORDER = 0xFF373737;
    private static final int COLOR_SLOT_INNER = 0xFF8B8B8B;

    private final BlockPos pos;
    private final int editIndex;

    private EditBox nameBox;
    private EditBox descriptorBox;

    public StopEditScreen(StopEditMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.pos = container.getStopBE().getBlockPos();
        this.editIndex = container.getEditIndex();
    }

    @Override
    protected void init() {
        setWindowSize(StopEditMenu.guiWidth(), StopEditMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();

        Stop stop = menu.getStopBE().getStop(editIndex);
        String name = stop != null ? stop.name : "";
        String descriptor = stop != null ? stop.descriptor : "";

        nameBox = new EditBox(font, leftPos + StopEditMenu.FIELD_X, topPos + StopEditMenu.NAME_BOX_Y,
                StopEditMenu.FIELD_W, StopEditMenu.BOX_H, Component.empty());
        nameBox.setMaxLength(Stop.MAX_NAME_LENGTH);
        nameBox.setValue(name);

        descriptorBox = new EditBox(font, leftPos + StopEditMenu.FIELD_X, topPos + StopEditMenu.DESC_BOX_Y,
                StopEditMenu.FIELD_W, StopEditMenu.BOX_H, Component.empty());
        descriptorBox.setMaxLength(Stop.MAX_DESCRIPTOR_LENGTH);
        descriptorBox.setValue(descriptor);

        addRenderableWidget(nameBox);
        addRenderableWidget(descriptorBox);

        int by = topPos + StopEditMenu.BUTTONS_Y;
        int bw = 50;
        addRenderableWidget(Button.builder(Component.translatable("gui.pipeorgans.stop.delete"), b -> sendDelete())
                .bounds(leftPos + StopEditMenu.FIELD_X, by, bw, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.pipeorgans.stop.discard"), b -> sendDiscard())
                .bounds(leftPos + StopEditMenu.FIELD_X + bw + 4, by, bw, 18).build());
        addRenderableWidget(Button.builder(Component.translatable("gui.pipeorgans.stop.save"), b -> sendSave())
                .bounds(leftPos + StopEditMenu.FIELD_X + 2 * (bw + 4), by, bw, 18).build());
    }

    // Actions

    private void sendSave() {
        ItemStack filter = menu.getGhostInventory().getStackInSlot(0);
        AllPackets.getChannel().sendToServer(new StopActionPacket(
                pos, StopActionPacket.SAVE, editIndex, nameBox.getValue(), descriptorBox.getValue(), filter));
    }

    private void sendDelete() {
        AllPackets.getChannel().sendToServer(StopActionPacket.simple(pos, StopActionPacket.DELETE, editIndex));
    }

    private void sendDiscard() {
        AllPackets.getChannel().sendToServer(StopActionPacket.simple(pos, StopActionPacket.OPEN_MAIN, -1));
    }

    // Input

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameBox.isFocused() || descriptorBox.isFocused()) {
            if (keyCode == 256) { // ESC -> discard back to main
                sendDiscard();
                return true;
            }
            if (nameBox.isFocused())
                nameBox.keyPressed(keyCode, scanCode, modifiers);
            else
                descriptorBox.keyPressed(keyCode, scanCode, modifiers);
            return true; // consume so the inventory key doesn't close the screen
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (nameBox.isFocused())
            return nameBox.charTyped(codePoint, modifiers);
        if (descriptorBox.isFocused())
            return descriptorBox.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    // Rendering

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        drawSlotBackground(graphics, StopEditMenu.FILTER_BG_X + 1, StopEditMenu.FILTER_BG_Y + 1);

        int invX = StopEditMenu.playerInvX();
        int invY = StopEditMenu.playerInvY();
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                drawSlotBackground(graphics, invX + col * 18, invY + row * 18);
        for (int col = 0; col < 9; col++)
            drawSlotBackground(graphics, invX + col * 18, invY + 58);
    }

    private void drawSlotBackground(GuiGraphics graphics, int guiX, int guiY) {
        int x = leftPos + guiX;
        int y = topPos + guiY;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, COLOR_SLOT_BORDER);
        graphics.fill(x, y, x + 16, y + 16, COLOR_SLOT_INNER);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, StopEditMenu.FIELD_X, StopEditMenu.TITLE_Y, 0xFFFFFF, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.stop.name"),
                StopEditMenu.FIELD_X, StopEditMenu.NAME_LABEL_Y, 0xFFB0B0B0, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.stop.descriptor"),
                StopEditMenu.FIELD_X, StopEditMenu.DESC_LABEL_Y, 0xFFB0B0B0, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.stop.item_filter"),
                StopEditMenu.FIELD_X + 22, StopEditMenu.FILTER_BG_Y + 5, 0xFFB0B0B0, false);
    }
}
