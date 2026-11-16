package mrnerdy42.keywizard.gui;

import committee.nova.mkw.core.gui.ScreenToggleSprite;
import mrnerdy42.keywizard.KeyWizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/** Small textured icon button shared by the legacy screens. */
public class TexturedToggleButton extends GuiButton {
    private static final ResourceLocation TEXTURE = new ResourceLocation(KeyWizard.MODID, ScreenToggleSprite.TEXTURE_PATH);

    public TexturedToggleButton(int buttonId, int x, int y) {
        super(buttonId, x, y, ScreenToggleSprite.WIDTH, ScreenToggleSprite.HEIGHT, "");
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (!this.visible) {
            return;
        }
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        mc.getTextureManager().bindTexture(TEXTURE);
        this.drawModalRectWithCustomSizedTexture(
                this.x, this.y,
                ScreenToggleSprite.U, ScreenToggleSprite.textureV(this.hovered),
                this.width, this.height,
                ScreenToggleSprite.TEXTURE_WIDTH, ScreenToggleSprite.TEXTURE_HEIGHT
        );
    }
}
