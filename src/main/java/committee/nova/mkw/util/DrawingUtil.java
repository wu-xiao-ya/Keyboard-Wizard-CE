package committee.nova.mkw.util;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;

public class DrawingUtil {
    public static void drawHorizontalLine(MatrixStack matrices, int x1, int x2, int y, int color) {
        AbstractGui.hLine(matrices, x1, x2, y, color);
    }

    public static void drawVerticalLine(MatrixStack matrices, int x, int y1, int y2, int color) {
        AbstractGui.vLine(matrices, x, y1, y2, color);
    }

    public static void drawNoFillRect(MatrixStack matrices, int left, int top, int right, int bottom, int color) {
        drawHorizontalLine(matrices, left, right, top, color);
        drawHorizontalLine(matrices, left, right, bottom, color);
        drawVerticalLine(matrices, left, top, bottom, color);
        drawVerticalLine(matrices, right, top, bottom, color);
    }
}
