package mrnerdy42.keywizard.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

/**
 * A 20x20 code-drawn icon button used for returning to the parent controls screen.
 * The icon is intentionally texture-free so the 1.12.2 branch does not depend on the
 * modern toggle sheet or any external PNG layout assumptions.
 */
public class TexturedToggleButton extends GuiButton {

    public TexturedToggleButton(int buttonId, int x, int y, int texU) {
        super(buttonId, x, y, 20, 20, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int baseColor = this.hovered ? 0xFF4A4A4A : 0xFF2F2F2F;
        int borderColor = this.hovered ? 0xFFB5B5B5 : 0xFF808080;
        int glyphColor = this.hovered ? 0xFFFFFFFF : 0xFFE0E0E0;

        drawRect(this.x, this.y, this.x + this.width, this.y + this.height, baseColor);
        drawRect(this.x, this.y, this.x + this.width, this.y + 1, borderColor);
        drawRect(this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, borderColor);
        drawRect(this.x, this.y, this.x + 1, this.y + this.height, borderColor);
        drawRect(this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, borderColor);

        int cx = this.x + 5;
        int cy = this.y + 4;
        drawRect(cx, cy + 4, cx + 7, cy + 6, glyphColor);
        drawRect(cx + 1, cy + 2, cx + 3, cy + 8, glyphColor);
        drawRect(cx + 3, cy + 1, cx + 5, cy + 2, glyphColor);
        drawRect(cx + 3, cy + 8, cx + 5, cy + 9, glyphColor);
        drawRect(cx + 5, cy + 3, cx + 7, cy + 4, glyphColor);
        drawRect(cx + 5, cy + 6, cx + 7, cy + 7, glyphColor);
    }
}
