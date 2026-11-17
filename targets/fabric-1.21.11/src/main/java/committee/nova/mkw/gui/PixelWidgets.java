package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

/** Version-native rendering adapters for the shared silver pixel palette. */
final class PixelWidgets {
    private PixelWidgets() {
    }

    static void drawButton(DrawContext context, Text label, int x, int y, int width, int height,
                           KeyState state, boolean hovered) {
        PixelKeyboardStyle.drawKeycap(context::fill, x, y, width, height, state, hovered);
        var font = MinecraftClient.getInstance().textRenderer;
        if (height < font.fontHeight + 3 || width < 5) {
            return;
        }
        String text = label.getString();
        int availableWidth = Math.max(0, width - 6);
        int textWidth = font.getWidth(text);
        if (textWidth > availableWidth && availableWidth > 0) {
            float scale = (float) availableWidth / textWidth;
            boolean functionKey = text.matches("(?i)F\\d+");
            if (functionKey || scale >= 2f / 3f) {
                float textX = x + (width - textWidth * scale) / 2f;
                float textY = y + (height - font.fontHeight * scale - 2) / 2f;
                context.getMatrices().pushMatrix();
                context.getMatrices().translate(textX, textY);
                context.getMatrices().scale(scale, scale);
                context.drawText(font, text, 0, 0, state.ink, false);
                context.getMatrices().popMatrix();
                return;
            }
            text = fitLabel(text, availableWidth);
        }
        int textX = x + (width - font.getWidth(text)) / 2;
        int textY = y + (height - font.fontHeight - 2) / 2;
        context.drawText(font, text, textX, textY, state.ink, false);
    }

    private static String fitLabel(String text, int availableWidth) {
        var font = MinecraftClient.getInstance().textRenderer;
        String ellipsis = "\u2026";
        if (font.getWidth(ellipsis) > availableWidth) {
            return "";
        }
        int end = text.length();
        while (end > 0 && font.getWidth(text.substring(0, end) + ellipsis) > availableWidth) {
            end = text.offsetByCodePoints(end, -1);
        }
        return text.substring(0, end) + ellipsis;
    }

    static void drawSearch(DrawContext context, TextFieldWidget field) {
        if (field == null) {
            return;
        }
        int x = field.getX();
        int y = field.getY();
        int width = field.getWidth();
        int height = field.getHeight();
        context.fill(x - 2, y - 2, x + width + 2, y + height + 2, PixelKeyboardStyle.BORDER);
        context.fill(x - 1, y - 1, x + width + 1, y + height + 1, PixelKeyboardStyle.LIST_SURFACE);
    }

    static Builder pixelBuilder(Text label, ButtonWidget.PressAction action) {
        return new Builder(label, action, false);
    }

    static Builder iconBuilder(Text label, ButtonWidget.PressAction action) {
        return new Builder(label, action, true);
    }

    static final class Builder {
        private final Text label;
        private final ButtonWidget.PressAction action;
        private final boolean keyboardIcon;
        private int x;
        private int y;
        private int width;
        private int height;

        private Builder(Text label, ButtonWidget.PressAction action, boolean keyboardIcon) {
            this.label = label;
            this.action = action;
            this.keyboardIcon = keyboardIcon;
        }

        Builder dimensions(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            return this;
        }

        ButtonWidget build() {
            return new PixelButton(this.x, this.y, this.width, this.height,
                    this.label, this.action, this.keyboardIcon);
        }
    }

    private static final class PixelButton extends ButtonWidget {
        private final boolean keyboardIcon;

        private PixelButton(int x, int y, int width, int height, net.minecraft.text.Text label,
                            PressAction action, boolean keyboardIcon) {
            super(x, y, width, height, label, action, DEFAULT_NARRATION_SUPPLIER);
            this.keyboardIcon = keyboardIcon;
        }

        @Override
        protected void drawIcon(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.keyboardIcon) {
                PixelKeyboardStyle.drawKeycap(context::fill, getX(), getY(), getWidth(), getHeight(),
                        KeyState.UNASSIGNED, isHovered());
                PixelKeyboardStyle.drawKeyboardIcon(context::fill, getX(), getY(), getWidth(), getHeight());
            } else {
                PixelWidgets.drawButton(context, getMessage(), getX(), getY(), getWidth(), getHeight(),
                        KeyState.UNASSIGNED, isHovered());
            }
        }
    }
}
