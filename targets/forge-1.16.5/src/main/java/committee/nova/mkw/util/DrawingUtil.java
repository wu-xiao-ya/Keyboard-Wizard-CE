package committee.nova.mkw.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public class DrawingUtil {
    public static void drawHorizontalLine(MatrixStack matrices, int x1, int x2, int y, int color) {
        AbstractGui.fill(matrices, Math.min(x1, x2), y, Math.max(x1, x2) + 1, y + 1, color);
    }

    public static void drawVerticalLine(MatrixStack matrices, int x, int y1, int y2, int color) {
        AbstractGui.fill(matrices, x, Math.min(y1, y2), x + 1, Math.max(y1, y2) + 1, color);
    }

    public static void drawNoFillRect(MatrixStack matrices, int left, int top, int right, int bottom, int color) {
        drawHorizontalLine(matrices, left, right, top, color);
        drawHorizontalLine(matrices, left, right, bottom, color);
        drawVerticalLine(matrices, left, top, bottom, color);
        drawVerticalLine(matrices, right, top, bottom, color);
    }
}
