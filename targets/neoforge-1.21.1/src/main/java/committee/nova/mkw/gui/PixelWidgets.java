package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

final class PixelWidgets {
    private static final Component ELLIPSIS = Component.literal("…");
    private static final float MIN_KEYCAP_LABEL_SCALE = 2f / 3f;

    private PixelWidgets() {
    }

    static FormattedCharSequence fit(Font font, Component label, int width) {
        if (width <= 0) return FormattedCharSequence.EMPTY;
        FormattedCharSequence text = label.getVisualOrderText();
        if (font.width(text) <= width) return text;
        int prefixWidth = width - font.width(ELLIPSIS);
        if (prefixWidth < 0) return Language.getInstance().getVisualOrder(font.substrByWidth(label, width));
        return Language.getInstance().getVisualOrder(FormattedText.composite(font.substrByWidth(label, prefixWidth), ELLIPSIS));
    }

    static int labelWidth(int width) {
        return Math.max(0, width - 4);
    }

    static void drawButton(GuiGraphics graphics, Component label, int x, int y, int width, int height,
                           KeyState state, boolean highlighted, boolean selected) {
        drawButton(graphics, label, x, y, width, height, state, highlighted, selected, 1f);
    }

    static void drawKeyboardKey(GuiGraphics graphics, Component label, int x, int y, int width, int height,
                                KeyState state, boolean highlighted, boolean selected, boolean functionKey) {
        drawButton(graphics, label, x, y, width, height, state, highlighted, selected,
                functionKey ? 0f : MIN_KEYCAP_LABEL_SCALE);
    }

    private static void drawButton(GuiGraphics graphics, Component label, int x, int y, int width, int height,
                                   KeyState state, boolean highlighted, boolean selected, float minLabelScale) {
        PixelKeyboardStyle.drawKeycap(graphics::fill, x, y, width, height, state, highlighted);
        Font font = Minecraft.getInstance().font;
        if (height < font.lineHeight + 3 || width < 5) return;

        int availableWidth = labelWidth(width);
        FormattedCharSequence text = label.getVisualOrderText();
        int textWidth = font.width(text);
        if (textWidth > availableWidth) {
            float scale = (float) availableWidth / textWidth;
            if (scale >= minLabelScale) {
                float textX = x + (width - textWidth * scale) / 2f;
                float textY = y + (height - font.lineHeight * scale - 2) / 2f;
                graphics.pose().pushPose();
                graphics.pose().translate(textX, textY, 0);
                graphics.pose().scale(scale, scale, 1f);
                drawLabel(graphics, font, text, 0, 0, textWidth, state.ink, selected);
                graphics.pose().popPose();
                return;
            }
            text = fit(font, label, availableWidth);
            textWidth = font.width(text);
        }
        int textX = x + (width - textWidth) / 2;
        int textY = y + (height - font.lineHeight - 2) / 2;
        drawLabel(graphics, font, text, textX, textY, textWidth, state.ink, selected);
    }

    private static void drawLabel(GuiGraphics graphics, Font font, FormattedCharSequence text,
                                  int textX, int textY, int textWidth, int ink, boolean selected) {
        graphics.drawString(font, text, textX, textY, ink, false);
        if (selected && textWidth > 0) {
            graphics.fill(textX, textY + font.lineHeight, textX + textWidth, textY + font.lineHeight + 1, ink);
        }
    }

    static void renderTooltip(GuiGraphics graphics, Font font, List<Component> labels, int mouseX, int mouseY) {
        if (labels.isEmpty()) return;
        int width = Math.max(1, graphics.guiWidth() - 24);
        List<FormattedCharSequence> lines = labels.stream().flatMap(label -> font.split(label, width).stream()).toList();
        graphics.renderTooltip(font, lines, mouseX, mouseY);
    }

    static Button button(int x, int y, int width, int height, Component label, Button.OnPress onPress) {
        return new PixelButton(x, y, width, height, label, onPress);
    }

    static Button controlsEntryButton(ControlsScreen screen, Component label, Button.OnPress onPress) {
        return new PixelButton(0, 0, 164, 24, label, onPress) {
            @Override
            public int getX() {
                return (screen.width - getWidth()) / 2;
            }

            @Override
            public int getY() {
                return screen.height - 58;
            }
        };
    }

    private static class PixelButton extends Button {
        private PixelButton(int x, int y, int width, int height, Component label, OnPress onPress) {
            super(x, y, width, height, label, onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            drawButton(graphics, getMessage(), getX(), getY(), getWidth(), getHeight(),
                    KeyState.UNASSIGNED, isHoveredOrFocused() && active, !active);
        }
    }

    static EditBox searchBox(Font font, int x, int y, int width, Component label) {
        EditBox box = new EditBox(font, x + 4, y + 4, width - 8, 14, label) {
            @Override
            public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
                int border = isFocused() ? PixelKeyboardStyle.HOVER_BORDER : PixelKeyboardStyle.BORDER;
                graphics.fill(getX() - 4, getY() - 4, getRight() + 4, getY() + 14, border);
                graphics.fill(getX() - 2, getY() - 2, getRight() + 2, getY() + 12, PixelKeyboardStyle.LIST_SELECTED);
                super.renderWidget(graphics, mouseX, mouseY, partialTick);
            }
        };
        box.setBordered(false);
        box.setTextColor(PixelKeyboardStyle.INK);
        box.setTextColorUneditable(PixelKeyboardStyle.PANEL_FOOT);
        box.setHint(label);
        return box;
    }
}
