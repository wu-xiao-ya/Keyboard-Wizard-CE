package committee.nova.mkw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

final class PixelWidgets {
    private PixelWidgets() {}

    static PixelKeyboardStyle.RectangleRenderer painter(MatrixStack g) {
        return (a, b, c, d, color) ->
                net.minecraft.client.gui.AbstractGui.fill(g, a, b, c, d, color);
    }

    static void panel(MatrixStack g, int x, int y, int w, int h) {
        PixelKeyboardStyle.drawPanel(painter(g), x, y, w, h);
    }

    static void key(
            MatrixStack g, String label, int x, int y, int w, int h, int count, boolean hover) {
        KeyState state =
                count > 1
                        ? KeyState.CONFLICT
                        : count == 1 ? KeyState.ASSIGNED : KeyState.UNASSIGNED;
        PixelKeyboardStyle.drawKeycap(painter(g), x, y, w, h, state, hover);
        FontRenderer font = Minecraft.getInstance().font;
        if (w < 5 || h < font.lineHeight + 3) return;
        int labelWidth = font.width(label);
        float scale = labelWidth == 0 ? 1f : Math.min(1f, (float) (w - 4) / labelWidth);
        if (scale < 1f && (scale >= 2f / 3f || label.matches("F[0-9]+"))) {
            float tx = x + (w - labelWidth * scale) / 2f,
                    ty = y + (h - font.lineHeight * scale - 2) / 2f;
            g.pushPose();
            g.translate(tx, ty, 0);
            g.scale(scale, scale, 1f);
            font.draw(g, label, 0, 0, state.ink);
            g.popPose();
            return;
        }
        String text = label;
        while (font.width(text) > w - 4 && !text.isEmpty())
            text = text.substring(0, text.offsetByCodePoints(text.length(), -1));
        int tx = x + (w - font.width(text)) / 2, ty = y + (h - font.lineHeight - 2) / 2;
        font.draw(g, text, tx, ty, state.ink);
    }

    static class PixelButton extends Button {
        PixelButton(int x, int y, int w, int h, ITextComponent label, IPressable press) {
            super(x, y, w, h, label, press);
        }

        @Override
        public void renderButton(MatrixStack g, int mx, int my, float delta) {
            key(
                    g,
                    getMessage().getString(),
                    this.x,
                    this.y,
                    getWidth(),
                    getHeight(),
                    0,
                    active && isHovered());
            if (!active) {
                int textWidth =
                        Math.min(
                                getWidth() - 4,
                                Minecraft.getInstance().font.width(getMessage().getString()));
                int underlineX = this.x + (getWidth() - textWidth) / 2;
                int underlineY =
                        this.y + (getHeight() + Minecraft.getInstance().font.lineHeight - 2) / 2;
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

    static Button button(
            int x, int y, int w, int h, ITextComponent label, Button.IPressable press) {
        return new PixelButton(x, y, w, h, label, press);
    }

    static class PixelSearch extends TextFieldWidget {
        PixelSearch(FontRenderer font, int x, int y, int w, int h, ITextComponent label) {
            super(font, x, y, w, h, label);
            setBordered(false);
            setTextColor(0xFF202020);
        }

        @Override
        public void renderButton(MatrixStack g, int mx, int my, float delta) {
            painter(g)
                    .fill(
                            this.x - 3,
                            this.y - 3,
                            this.x + getWidth() + 3,
                            this.y + getHeight() + 3,
                            PixelKeyboardStyle.BORDER);
            painter(g)
                    .fill(
                            this.x - 2,
                            this.y - 2,
                            this.x + getWidth() + 2,
                            this.y + getHeight() + 2,
                            PixelKeyboardStyle.LIST_SURFACE);
            super.renderButton(g, mx, my, delta);
        }
    }

    static class IconButton extends PixelButton {
        IconButton(int x, int y, ITextComponent label, Button.IPressable press) {
            super(x, y, 20, 20, label, press);
        }

        @Override
        public void renderButton(MatrixStack g, int mx, int my, float delta) {
            PixelKeyboardStyle.drawKeycap(
                    painter(g),
                    this.x,
                    this.y,
                    getWidth(),
                    getHeight(),
                    KeyState.UNASSIGNED,
                    isHovered());
            PixelKeyboardStyle.drawKeyboardIcon(
                    painter(g), this.x, this.y, getWidth(), getHeight());
        }
    }
}
