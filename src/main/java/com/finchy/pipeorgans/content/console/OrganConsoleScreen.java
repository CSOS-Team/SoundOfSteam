package com.finchy.pipeorgans.content.console;

import com.finchy.pipeorgans.network.AllPackets;
import com.finchy.pipeorgans.network.packet.OrganConsoleNotePacket;
import com.simibubi.create.foundation.gui.menu.AbstractSimiContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.SlotItemHandler;

import java.util.HashSet;
import java.util.Set;
public class OrganConsoleScreen extends AbstractSimiContainerScreen<OrganConsoleMenu> {

    private static final int NOTE_VELOCITY = 127;

    private static final int COLOR_PANEL = 0xFF313131;
    private static final int COLOR_PANEL_BORDER = 0xFF161616;
    private static final int COLOR_NATURAL = 0xFFE8E8E8;
    private static final int COLOR_SHARP = 0xFF1E1E1E;
    private static final int COLOR_PRESSED = 0xFFFFC83C;   // locally held (left momentary or right latch)
    private static final int COLOR_RECEIVED = 0xFF50C8FF;  // externally received
    private static final int COLOR_KEY_BORDER = 0xFF000000;
    private static final int COLOR_SLOT_BORDER = 0xFF373737;
    private static final int COLOR_SLOT_INNER = 0xFF8B8B8B;

    private final boolean pedalboardMode;
    private final BlockPos consolePos;
    private final OrganConsoleBlockEntity console;

    private final Set<Long> latched = new HashSet<>();
    private long momentaryKey = -1;
    private final Set<Long> active = new HashSet<>();

    public OrganConsoleScreen(OrganConsoleMenu container, Inventory inv, Component title) {
        super(container, inv, title);
        this.pedalboardMode = container.isPedalboardMode();
        this.console = container.getConsole();
        this.consolePos = console.getBlockPos();
    }

    // Read the count live from the menu so layout always matches the slots
    private int manualCount() {
        return menu.getManualCount();
    }

    private static long encode(int section, int key) {
        return (((long) section) << 32) | (key & 0xFFFFFFFFL);
    }

    @Override
    protected void init() {
        setWindowSize(
                OrganConsoleMenu.guiWidth(pedalboardMode, manualCount()),
                OrganConsoleMenu.guiHeight(pedalboardMode, manualCount()));
        setWindowOffset(0, 0);
        super.init();
    }

    // Geometry
    private int rowCount() {
        return OrganConsoleMenu.keyboardRows(pedalboardMode, manualCount());
    }

    private int sectionForRow(int row) {
        if (pedalboardMode)
            return row == 0 ? OrganConsoleBlockEntity.PEDAL_SECTION : -1;
        return row < manualCount() ? row : -1;
    }

    private long keyAt(int guiX, int guiY) {
        int keyW = OrganConsoleMenu.keyWidth(pedalboardMode);
        int keyboardWidth = OrganConsoleMenu.keyboardWidth(pedalboardMode);
        int keyCount = OrganConsoleMenu.keyCountFor(pedalboardMode);

        if (guiX < OrganConsoleMenu.KEYBOARD_X || guiX >= OrganConsoleMenu.KEYBOARD_X + keyboardWidth)
            return -1;

        for (int row = 0; row < rowCount(); row++) {
            int section = sectionForRow(row);
            if (section < 0)
                continue;
            int keyTop = OrganConsoleMenu.rowTop(row);
            if (guiY >= keyTop && guiY < keyTop + OrganConsoleMenu.KEY_H) {
                int key = (guiX - OrganConsoleMenu.KEYBOARD_X) / keyW;
                if (key >= 0 && key < keyCount)
                    return encode(section, key);
            }
        }
        return -1;
    }

    // Note state
    private void updateActive() {
        Set<Long> desired = new HashSet<>(latched);
        if (momentaryKey != -1)
            desired.add(momentaryKey);

        for (long encoded : new HashSet<>(active))
            if (!desired.contains(encoded)) {
                sendNote(encoded, false);
                active.remove(encoded);
            }
        for (long encoded : desired)
            if (active.add(encoded))
                sendNote(encoded, true);
    }

    private void sendNote(long encoded, boolean on) {
        int section = (int) (encoded >> 32);
        int key = (int) (encoded & 0xFFFFFFFFL);
        AllPackets.getChannel().sendToServer(new OrganConsoleNotePacket(consolePos, section, key, on, NOTE_VELOCITY));
    }

    private void clearAll() {
        latched.clear();
        momentaryKey = -1;
        updateActive();
    }

    // Input
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        long key = keyAt((int) mouseX - leftPos, (int) mouseY - topPos);
        if (key != -1) {
            if (button == 0) {
                momentaryKey = key;
                updateActive();
                return true;
            } else if (button == 1) {
                if (!latched.remove(key))
                    latched.add(key);
                updateActive();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        if (button == 0 && momentaryKey != -1) {
            long key = keyAt((int) mouseX - leftPos, (int) mouseY - topPos);
            if (key != momentaryKey) {
                momentaryKey = key;
                updateActive();
            }
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && momentaryKey != -1) {
            momentaryKey = -1;
            updateActive();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void onClose() {
        clearAll();
        super.onClose();
    }

    @Override
    public void removed() {
        clearAll();
        super.removed();
    }

    // Rendering
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int mc = manualCount();

        graphics.fill(leftPos - 1, topPos - 1, leftPos + imageWidth + 1, topPos + imageHeight + 1, COLOR_PANEL_BORDER);
        graphics.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, COLOR_PANEL);

        // Division filter slot background per section
        int groups = OrganConsoleMenu.emitFilterCount(pedalboardMode, mc);
        for (int i = 0; i < groups; i++)
            drawSlotBackground(graphics, OrganConsoleMenu.filterBgX(i) + 1, OrganConsoleMenu.FILTER_ROW_Y + 1);

        // Keyboards
        for (int row = 0; row < rowCount(); row++) {
            int section = sectionForRow(row);
            if (section >= 0)
                drawKeyboard(graphics, row, section);
        }

        // Player inventory slot backgrounds
        int invX = OrganConsoleMenu.playerInvX(pedalboardMode, mc);
        int invY = OrganConsoleMenu.playerInvY(pedalboardMode, mc);
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 9; col++)
                drawSlotBackground(graphics, invX + col * 18, invY + row * 18);
        for (int col = 0; col < 9; col++)
            drawSlotBackground(graphics, invX + col * 18, invY + 58);
    }

    private void drawKeyboard(GuiGraphics graphics, int row, int section) {
        int keyW = OrganConsoleMenu.keyWidth(pedalboardMode);
        int keyCount = OrganConsoleMenu.keyCountFor(pedalboardMode);
        int x0 = leftPos + OrganConsoleMenu.KEYBOARD_X;
        int y0 = topPos + OrganConsoleMenu.rowTop(row);
        int low = OrganConsoleBlockEntity.sectionLowPitch(section);
        int h = OrganConsoleMenu.KEY_H;

        for (int key = 0; key < keyCount; key++) {
            int kx = x0 + key * keyW;
            int pitch = low + key;
            int pc = pitch % 12;
            boolean sharp = pc == 1 || pc == 3 || pc == 6 || pc == 8 || pc == 10;

            boolean locallyOn = active.contains(encode(section, key));
            boolean received = console.isReceivedPitch(section, pitch);

            int color;
            if (locallyOn)
                color = COLOR_PRESSED;
            else if (received)
                color = COLOR_RECEIVED;
            else
                color = sharp ? COLOR_SHARP : COLOR_NATURAL;

            graphics.fill(kx, y0, kx + keyW - 1, y0 + h, color);
            graphics.fill(kx + keyW - 1, y0, kx + keyW, y0 + h, COLOR_KEY_BORDER);
        }
        graphics.fill(x0, y0 - 1, x0 + keyCount * keyW, y0, COLOR_KEY_BORDER);
        graphics.fill(x0, y0 + h, x0 + keyCount * keyW, y0 + h + 1, COLOR_KEY_BORDER);
        graphics.fill(x0 - 1, y0 - 1, x0, y0 + h + 1, COLOR_KEY_BORDER);
    }

    private void drawSlotBackground(GuiGraphics graphics, int guiX, int guiY) {
        int x = leftPos + guiX;
        int y = topPos + guiY;
        graphics.fill(x - 1, y - 1, x + 17, y + 17, COLOR_SLOT_BORDER);
        graphics.fill(x, y, x + 16, y + 16, COLOR_SLOT_INNER);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, OrganConsoleMenu.FILTERS_X, OrganConsoleMenu.TITLE_Y, 0xFFFFFF, false);
    }

    // Filter tooltips
    @Override
    protected void renderTooltip(GuiGraphics graphics, int x, int y) {
        if (hoveredSlot instanceof SlotItemHandler sih && sih.getItemHandler() == menu.ghostInventory) {
            graphics.renderTooltip(font, filterRoleLabel(sih.getSlotIndex()), x, y);
            return;
        }
        super.renderTooltip(graphics, x, y);
    }

    private static Component filterRoleLabel(int section) {
        return section == OrganConsoleBlockEntity.PEDAL_SECTION
                ? Component.translatable("gui.pipeorgans.organ_console.pedalboard_division")
                : Component.translatable("gui.pipeorgans.organ_console.manual_division", section + 1);
    }
}
