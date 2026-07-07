package com.finchy.pipeorgans.content.coupler;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.CouplerActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class CouplerEditScreen extends AbstractSimiContainerScreen<CouplerEditMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_SLOT_BORDER = 0xFF373737;
    private static final int COLOR_SLOT_INNER = 0xFF8B8B8B;

    private final BlockPos pos;
    private final int editIndex;
    private EditBox nameBox;

    public CouplerEditScreen(CouplerEditMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.pos = container.getCouplerBE().getBlockPos();
        this.editIndex = container.getEditIndex();
    }

    @Override
    protected void init() {
        setWindowSize(CouplerEditMenu.guiWidth(), CouplerEditMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();

        Coupler coupler = menu.getCouplerBE().getCoupler(editIndex);
        String name = coupler != null ? coupler.name : "";

        nameBox = new EditBox(font, leftPos + CouplerEditMenu.FIELD_X, topPos + CouplerEditMenu.NAME_BOX_Y,
                CouplerEditMenu.FIELD_W, CouplerEditMenu.BOX_H, Component.empty());
        nameBox.setMaxLength(Coupler.MAX_NAME_LENGTH);
        nameBox.setValue(name);
        addRenderableWidget(nameBox);

        int by = topPos + CouplerEditMenu.BUTTONS_Y;
        int bw = 50;
        int startX = leftPos + CouplerEditMenu.FIELD_X + (CouplerEditMenu.FIELD_W - bw) / 2;
        addRenderableWidget(Button.builder(Component.translatable("gui.pipeorgans.stop.discard"), b -> sendSave())
                .bounds(startX, by, bw, 18).build());
    }

    private void sendSave() {
        ItemStack a = menu.getGhostInventory().getStackInSlot(0);
        ItemStack b = menu.getGhostInventory().getStackInSlot(1);
        AllPackets.getChannel().sendToServer(new CouplerActionPacket(
                pos, CouplerActionPacket.SAVE, editIndex, -1, nameBox.getValue(), a, b));
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (nameBox.isFocused()) {
            if (keyCode == 256) {
                sendSave();
                return true;
            }
            nameBox.keyPressed(keyCode, scanCode, modifiers);
            return true;
        }
        if (keyCode == 256 || minecraft.options.keyInventory.matches(keyCode, scanCode)) {
            sendSave();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void onClose() {
        sendSave();
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (nameBox.isFocused())
            return nameBox.charTyped(codePoint, modifiers);
        return super.charTyped(codePoint, modifiers);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        drawSlotBackground(graphics, CouplerEditMenu.A_BG_X + 1, CouplerEditMenu.FILTER_BG_Y + 1);
        drawSlotBackground(graphics, CouplerEditMenu.B_BG_X + 1, CouplerEditMenu.FILTER_BG_Y + 1);

        int invX = CouplerEditMenu.playerInvX();
        int invY = CouplerEditMenu.playerInvY();
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
        graphics.drawString(font, title, CouplerEditMenu.FIELD_X, CouplerEditMenu.TITLE_Y, 0xFFFFFF, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.stop.name"),
                CouplerEditMenu.FIELD_X, CouplerEditMenu.NAME_LABEL_Y, 0xFFB0B0B0, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.coupler.division_a"),
                CouplerEditMenu.A_BG_X, CouplerEditMenu.FILTER_LABEL_Y, 0xFFB0B0B0, false);
        graphics.drawString(font, Component.translatable("gui.pipeorgans.coupler.division_b"),
                CouplerEditMenu.B_BG_X, CouplerEditMenu.FILTER_LABEL_Y, 0xFFB0B0B0, false);
    }
}
