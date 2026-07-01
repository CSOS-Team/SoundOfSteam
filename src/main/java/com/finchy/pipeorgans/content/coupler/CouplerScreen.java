package com.finchy.pipeorgans.content.coupler;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.CouplerActionPacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.ArrayList;
import java.util.List;

public class CouplerScreen extends AbstractSimiContainerScreen<CouplerMenu> {

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;

    private static final int COLOR_UNUSED = 0xFF333333;
    private static final int COLOR_UNUSED_HOVER = 0xFF444444;
    private static final int COLOR_USED = 0xFF6E6E6E;
    private static final int COLOR_USED_HOVER = 0xFF828282;
    private static final int COLOR_PRESSED = 0xFF50C8FF;
    private static final int COLOR_BORDER = 0xFF101010;
    private static final int COLOR_DRAG = 0xFFFFC83C;

    private final BlockPos pos;
    private final CouplerBlockEntity be;

    private int dragFrom = -1;

    public CouplerScreen(CouplerMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.be = container.getCouplerBE();
        this.pos = be.getBlockPos();
    }

    @Override
    protected void init() {
        setWindowSize(CouplerMenu.guiWidth(), CouplerMenu.guiHeight());
        setWindowOffset(0, 0);
        super.init();
    }

    private int cellAt(int guiX, int guiY) {
        for (int i = 0; i < CouplerBlockEntity.MAX_COUPLERS; i++) {
            int x = CouplerMenu.cellX(i);
            int y = CouplerMenu.cellY(i);
            if (guiX >= x && guiX < x + CouplerMenu.BUTTON_SIZE && guiY >= y && guiY < y + CouplerMenu.BUTTON_SIZE)
                return i;
        }
        return -1;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int cell = cellAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (cell != -1) {
            if (button == 0 && hasShiftDown()) {
                dragFrom = cell;
                return true;
            }
            if (button == 0) {
                if (!be.getCouplers().get(cell).isUnused())
                    send(CouplerActionPacket.simple(pos, CouplerActionPacket.TOGGLE, cell));
                return true;
            }
            if (button == 1) {
                send(CouplerActionPacket.simple(pos, CouplerActionPacket.OPEN_EDIT, cell));
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (dragFrom != -1 && button == 0) {
            int target = cellAt((int) mouseX - leftPos, (int) mouseY - topPos);
            if (target != -1 && target != dragFrom) {
                // Swap on the client side so UI changes
                java.util.Collections.swap(be.getCouplers(), dragFrom, target);

                // Tell server to save change
                send(CouplerActionPacket.move(pos, dragFrom, target));
            }
            dragFrom = -1;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private void send(CouplerActionPacket packet) {
        AllPackets.getChannel().sendToServer(packet);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        int hoveredCell = cellAt(mouseX - leftPos, mouseY - topPos);
        for (int i = 0; i < CouplerBlockEntity.MAX_COUPLERS; i++) {
            int x = leftPos + CouplerMenu.cellX(i);
            int y = topPos + CouplerMenu.cellY(i);
            boolean hovered = i == hoveredCell;
            Coupler coupler = be.getCouplers().get(i);

            int color;
            if (coupler.isUnused())
                color = hovered ? COLOR_UNUSED_HOVER : COLOR_UNUSED;
            else if (coupler.pressed)
                color = COLOR_PRESSED;
            else
                color = hovered ? COLOR_USED_HOVER : COLOR_USED;

            int border = (i == dragFrom) ? COLOR_DRAG : COLOR_BORDER;
            drawButton(graphics, x, y, color, border);

            if (coupler.isUnused())
                drawLabel(graphics, x, y, Component.translatable("gui.pipeorgans.stop.unused").getString(), 0x808080);
            else
                drawLabel(graphics, x, y, coupler.name, coupler.pressed ? 0xFF202020 : 0xFFFFFFFF);
        }
    }

    private void drawButton(GuiGraphics graphics, int x, int y, int color, int border) {
        int s = CouplerMenu.BUTTON_SIZE;
        graphics.fill(x - 1, y - 1, x + s + 1, y + s + 1, border);
        graphics.fill(x, y, x + s, y + s, color);
    }

    private void drawLabel(GuiGraphics graphics, int x, int y, String name, int color) {
        if (name == null || name.isEmpty())
            return;
        float scale = 0.5f;
        int maxFontWidth = (int) ((CouplerMenu.BUTTON_SIZE - 3) / scale);
        String shown = trimToWidth(name, maxFontWidth);
        int cx = x + CouplerMenu.BUTTON_SIZE / 2;
        graphics.pose().pushPose();
        graphics.pose().translate(cx, y + (CouplerMenu.BUTTON_SIZE - (int) (font.lineHeight * scale)) / 2, 0);
        graphics.pose().scale(scale, scale, 1f);
        int w = font.width(shown);
        graphics.drawString(font, shown, -w / 2, 0, color, false);
        graphics.pose().popPose();
    }

    private String trimToWidth(String text, int maxWidth) {
        if (font.width(text) <= maxWidth)
            return text;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            if (font.width(sb.toString() + text.charAt(i)) > maxWidth)
                break;
            sb.append(text.charAt(i));
        }
        return sb.toString();
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, CouplerMenu.GRID_X, CouplerMenu.TITLE_Y, 0xFFFFFF, false);
    }

    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        int cell = cellAt(x - leftPos, y - topPos);
        if (cell >= 0 && cell < CouplerBlockEntity.MAX_COUPLERS) {
            Coupler coupler = be.getCouplers().get(cell);
            if (coupler.isUnused()) {
                graphics.renderTooltip(font, Component.translatable("gui.pipeorgans.stop.unused"), x, y);
                return;
            }
            List<Component> lines = new ArrayList<>();
            lines.add(Component.literal(coupler.name.isEmpty() ? "Coupler " + (cell + 1) : coupler.name));
            lines.add(Component.translatable("gui.pipeorgans.coupler.routes").withStyle(s -> s.withColor(0xA0A0A0)));
            graphics.renderComponentTooltip(font, lines, x, y);
            return;
        }
        super.renderTooltip(graphics, x, y);
    }
}
