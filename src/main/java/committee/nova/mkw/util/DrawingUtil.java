package committee.nova.mkw.util;

import net.minecraft.client.gui.DrawContext;

public class DrawingUtil {
    public static void drawHorizontalLine(DrawContext ctx, float x1, float x2, float y, int color) {
        ctx.drawHorizontalLine(Math.round(x1), Math.round(x2), Math.round(y), color);
    }

    public static void drawVerticalLine(DrawContext ctx, float x, float y1, float y2, int color) {
        ctx.drawVerticalLine(Math.round(x), Math.round(y1), Math.round(y2), color);
    }

    public static void drawNoFillRect(DrawContext ctx, float left, float top, float right, float bottom, int color) {
        drawHorizontalLine(ctx, left, right, top, color);
        drawHorizontalLine(ctx, left, right, bottom, color);
        drawVerticalLine(ctx, left, top, bottom, color);
        drawVerticalLine(ctx, right, top, bottom, color);
    }
}

