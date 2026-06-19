package committee.nova.mkw.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;

public class DrawingUtil {
    public static void drawHorizontalLine(PoseStack poseStack, float x1, float x2, float y, int color) {
        GuiComponent.fill(poseStack, Math.round(x1), Math.round(y), Math.round(x2) + 1, Math.round(y) + 1, color);
    }

    public static void drawVerticalLine(PoseStack poseStack, float x, float y1, float y2, int color) {
        GuiComponent.fill(poseStack, Math.round(x), Math.round(y1), Math.round(x) + 1, Math.round(y2) + 1, color);
    }

    public static void drawNoFillRect(PoseStack poseStack, float left, float top, float right, float bottom, int color) {
        drawHorizontalLine(poseStack, left, right, top, color);
        drawHorizontalLine(poseStack, left, right, bottom, color);
        drawVerticalLine(poseStack, left, top, bottom, color);
        drawVerticalLine(poseStack, right, top, bottom, color);
    }
}
