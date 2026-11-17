package mrnerdy42.keywizard.gui;

import java.util.Collections;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.config.GuiUtils;

/** Pixel keyboard navigation button; keeps the legacy public constructor. */
public class TexturedToggleButton extends GuiButton {
    private final String tooltipKey;

    public TexturedToggleButton(int buttonId, int x, int y) {
        this(buttonId, x, y, "gui.openKeyWiz");
    }

    public TexturedToggleButton(int buttonId, int x, int y, String tooltipKey) {
        super(buttonId, x, y, 20, 20, "");
        this.tooltipKey = tooltipKey;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= this.x && mouseY >= this.y
                && mouseX < this.x + this.width && mouseY < this.y + this.height;
        PixelKeyboardStyle.drawKeycap(Gui::drawRect, x, y, width, height,
                PixelKeyboardStyle.KeyState.UNASSIGNED, hovered && enabled);
        PixelKeyboardStyle.drawKeyboardIcon(Gui::drawRect, x, y, width, height);
        if (hovered && mc.currentScreen != null) {
            GuiUtils.drawHoveringText(Collections.singletonList(I18n.format(tooltipKey)), mouseX, mouseY,
                    mc.currentScreen.width, mc.currentScreen.height, -1, mc.fontRenderer);
        }
    }
}
