package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;

final class PixelWidgets {
    private PixelWidgets() {}

    static PixelKeyboardStyle.RectangleRenderer painter(GuiGraphicsExtractor g) {
        return g::fill;
    }

    static void panel(GuiGraphicsExtractor g, int x, int y, int w, int h) {
        PixelKeyboardStyle.drawPanel(painter(g), x, y, w, h);
    }

    static void key(
            GuiGraphicsExtractor g,
            String label,
            int x,
            int y,
            int w,
            int h,
            int count,
            boolean hover) {
        KeyState state =
                count > 1
                        ? KeyState.CONFLICT
                        : count == 1 ? KeyState.ASSIGNED : KeyState.UNASSIGNED;
        PixelKeyboardStyle.drawKeycap(painter(g), x, y, w, h, state, hover);
        Font font = Minecraft.getInstance().font;
        if (w < 5 || h < font.lineHeight + 3) return;
        int labelWidth = font.width(label);
        float scale = labelWidth == 0 ? 1f : Math.min(1f, (float) (w - 4) / labelWidth);
        if (scale < 1f && (scale >= 2f / 3f || label.matches("F[0-9]+"))) {
            float tx = x + (w - labelWidth * scale) / 2f,
                    ty = y + (h - font.lineHeight * scale - 2) / 2f;
            g.pose().pushMatrix();
            g.pose().translate(tx, ty);
            g.pose().scale(scale, scale);
            g.text(font, label, 0, 0, state.ink);
            g.pose().popMatrix();
            return;
        }
        String text = label;
        while (font.width(text) > w - 4 && !text.isEmpty())
            text = text.substring(0, text.offsetByCodePoints(text.length(), -1));
        int tx = x + (w - font.width(text)) / 2, ty = y + (h - font.lineHeight - 2) / 2;
        g.text(font, text, tx, ty, state.ink);
    }

    static class PixelButton extends Button {
        PixelButton(int x, int y, int w, int h, Component label, OnPress press) {
            super(x, y, w, h, label, press, DEFAULT_NARRATION);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor g, int mx, int my, float delta) {
            key(
                    g,
                    getMessage().getString(),
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    0,
                    active && isHovered());
            if (!active) {
                int textWidth =
                        Math.min(
                                getWidth() - 4,
                                Minecraft.getInstance().font.width(getMessage().getString()));
                int underlineX = getX() + (getWidth() - textWidth) / 2;
                int underlineY =
                        getY() + (getHeight() + Minecraft.getInstance().font.lineHeight - 2) / 2;
                painter(g)
                        .fill(
                                underlineX,
                                underlineY,
                                underlineX + textWidth,
                                underlineY + 1,
                                PixelKeyboardStyle.INK);
            }
        }
    }

    static Button button(int x, int y, int w, int h, Component label, Button.OnPress press) {
        return new PixelButton(x, y, w, h, label, press);
    }

    static class PixelSearch extends EditBox {
        PixelSearch(Font font, int x, int y, int w, int h, Component label) {
            super(font, x, y, w, h, label);
            setBordered(false);
            setTextColor(0xFF202020);
        }

        @Override
        public void extractWidgetRenderState(GuiGraphicsExtractor g, int mx, int my, float delta) {
            painter(g)
                    .fill(
                            getX() - 3,
                            getY() - 3,
                            getX() + getWidth() + 3,
                            getY() + getHeight() + 3,
                            PixelKeyboardStyle.BORDER);
            painter(g)
                    .fill(
                            getX() - 2,
                            getY() - 2,
                            getX() + getWidth() + 2,
                            getY() + getHeight() + 2,
                            PixelKeyboardStyle.LIST_SURFACE);
            super.extractWidgetRenderState(g, mx, my, delta);
        }
    }

    static class IconButton extends PixelButton {
        IconButton(int x, int y, Component label, Button.OnPress press) {
            super(x, y, 20, 20, label, press);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor g, int mx, int my, float delta) {
            PixelKeyboardStyle.drawKeycap(
                    painter(g),
                    getX(),
                    getY(),
                    getWidth(),
                    getHeight(),
                    KeyState.UNASSIGNED,
                    isHovered());
            PixelKeyboardStyle.drawKeyboardIcon(
                    painter(g), getX(), getY(), getWidth(), getHeight());
        }
    }
}
