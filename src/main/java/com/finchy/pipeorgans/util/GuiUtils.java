package com.finchy.pipeorgans.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

public abstract class GuiUtils {

    public static void drawWordWrapDropShadow(GuiGraphics graphics, Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, boolean pDropShadow) {
        for (FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
            graphics.drawString(pFont, formattedcharsequence, pX, pY, pColor, pDropShadow);
            pY += 9;
        }
    }

}
