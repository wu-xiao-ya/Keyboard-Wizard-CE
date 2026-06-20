package mrnerdy42.keywizard.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Small textured icon button shared by legacy screens.
 * If the sheet cannot be loaded in an older runtime, it falls back to a code-drawn icon.
 */
public class TexturedToggleButton extends GuiButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation("keyboard_wizard_ce", "textures/gui/screen_toggle_widgets.png");
    private static final ResourceLocation RETURN_TEXTURE = new ResourceLocation("keyboard_wizard_ce", "textures/gui/screen_toggle_return.png");
    private final int texU;

    public TexturedToggleButton(int buttonId, int x, int y, int texU) {
        super(buttonId, x, y, 20, 20, "");
        this.texU = texU;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        if (this.drawTexture(mc)) {
            return;
        }

        this.drawFallbackIcon();
    }

    private boolean drawTexture(Minecraft mc) {
        try {
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.enableBlend();
            if (this.texU == 20) {
                mc.getTextureManager().bindTexture(RETURN_TEXTURE);
                this.drawModalRectWithCustomSizedTexture(this.x, this.y, 0.0F, 0.0F, this.width, this.height, 20.0F, 20.0F);
            } else {
                mc.getTextureManager().bindTexture(TEXTURE);
                this.drawModalRectWithCustomSizedTexture(this.x, this.y, this.texU, this.hovered ? 20 : 0, this.width, this.height, 40.0F, 40.0F);
            }
            return true;
        } catch (RuntimeException ignored) {
            return false;
        }
    }

    private void drawFallbackIcon() {
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
