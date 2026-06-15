package committee.nova.mkw.util;

import net.minecraft.client.gui.GuiGraphics;

public class DrawingUtil {
    public static void drawNoFillRect(GuiGraphics graphics, float left, float top, float right, float bottom, int color) {
        int x1 = Math.round(left);
        int y1 = Math.round(top);
        int x2 = Math.round(right);
        int y2 = Math.round(bottom);
        graphics.hLine(x1, x2, y1, color);
        graphics.hLine(x1, x2, y2, color);
        graphics.vLine(x1, y1, y2, color);
        graphics.vLine(x2, y1, y2, color);
    }
}
