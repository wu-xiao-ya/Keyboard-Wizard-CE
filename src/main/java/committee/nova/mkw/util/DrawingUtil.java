package committee.nova.mkw.util;

import net.minecraft.client.gui.GuiGraphics;

public class DrawingUtil {
    public static void drawHorizontalLine(GuiGraphics graphics, float x1, float x2, float y, int color) {
        graphics.hLine(Math.round(x1), Math.round(x2), Math.round(y), color);
    }

    public static void drawVerticalLine(GuiGraphics graphics, float x, float y1, float y2, int color) {
        graphics.vLine(Math.round(x), Math.round(y1), Math.round(y2), color);
    }

    public static void drawNoFillRect(GuiGraphics graphics, float left, float top, float right, float bottom, int color) {
        drawHorizontalLine(graphics, left, right, top, color);
        drawHorizontalLine(graphics, left, right, bottom, color);
        drawVerticalLine(graphics, left, top, bottom, color);
        drawVerticalLine(graphics, right, top, bottom, color);
    }
}

