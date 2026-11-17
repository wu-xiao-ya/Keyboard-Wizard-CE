package mrnerdy42.keywizard.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

/** Java 8 / LWJGL2 adapter for the shared silver pixel theme. */
final class PixelWidgets {
    private PixelWidgets() {
    }

    static void panel(int x, int y, int width, int height) {
        PixelKeyboardStyle.drawPanel(Gui::drawRect, x, y, width, height);
    }

    static void key(FontRenderer font, String label, int x, int y, int width, int height,
                    KeyState state, boolean hovered, boolean selected) {
        PixelKeyboardStyle.drawKeycap(Gui::drawRect, x, y, width, height, state, hovered);
        if (width < 5 || height < 8) {
            return;
        }
        int textWidth = font.getStringWidth(label);
        float scale = Math.min(1F, Math.min((width - 4F) / Math.max(1, textWidth), (height - 4F) / font.FONT_HEIGHT));
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + (width - textWidth * scale) / 2F, y + (height - font.FONT_HEIGHT * scale - 2) / 2F, 0F);
        GlStateManager.scale(scale, scale, 1F);
        font.drawString(label, 0, 0, state.ink);
        if (selected) {
            Gui.drawRect(0, font.FONT_HEIGHT, textWidth, font.FONT_HEIGHT + 1, state.ink);
        }
        GlStateManager.popMatrix();
    }

    static GuiButton button(int id, int x, int y, int width, int height, String text) {
        return new GuiButton(id, x, y, width, height, text) {
            @Override
            public void drawButton(Minecraft mc, int mx, int my, float ticks) {
                if (!visible) {
                    return;
                }
                hovered = mx >= x && my >= y && mx < x + this.width && my < y + this.height;
                KeyState state = displayString.contains("\u00a7c") ? KeyState.CONFLICT
                        : displayString.contains("\u00a7a") ? KeyState.ASSIGNED : KeyState.UNASSIGNED;
                String label = net.minecraft.util.text.TextFormatting.getTextWithoutFormattingCodes(displayString);
                key(mc.fontRenderer, label, x, y, this.width, this.height, state, hovered && enabled, !enabled);
            }
        };
    }
}
