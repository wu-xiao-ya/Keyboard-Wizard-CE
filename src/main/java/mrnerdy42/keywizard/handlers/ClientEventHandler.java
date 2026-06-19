package mrnerdy42.keywizard.handlers;

import org.lwjgl.input.Keyboard;

import mrnerdy42.keywizard.KeyWizardConfig;
import mrnerdy42.keywizard.gui.GuiKeyWizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    private static final int OPEN_KEY_WIZARD_BUTTON_ID = 420742;

    public static final KeyBinding keyOpenKeyWizard = new KeyBinding(
            "key.keyboard_wizard_ce.openKeyWizard",
            Keyboard.KEY_F7,
            "key.categories.keyboard_wizard_ce.bindings");

    public ClientEventHandler() {
        ClientRegistry.registerKeyBinding(keyOpenKeyWizard);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (keyOpenKeyWizard.isPressed()) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.displayGuiScreen(new GuiKeyWizard(minecraft, minecraft.currentScreen));
        }
    }

    @SubscribeEvent
    public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!KeyWizardConfig.openFromControlsGui || !(event.getGui() instanceof GuiOptions)) {
            return;
        }

        GuiButton controlsButton = null;
        for (GuiButton button : event.getButtonList()) {
            if (I18n.format("options.controls").equals(button.displayString)) {
                controlsButton = button;
                break;
            }
        }

        int x = controlsButton == null ? event.getGui().width / 2 + 160 : controlsButton.x + controlsButton.width + 4;
        int y = controlsButton == null ? event.getGui().height / 6 + 72 : controlsButton.y;
        event.getButtonList().add(new ScreenToggleButton(
                OPEN_KEY_WIZARD_BUTTON_ID,
                x,
                y));
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getButton().id == OPEN_KEY_WIZARD_BUTTON_ID && event.getGui() instanceof GuiOptions) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.displayGuiScreen(new GuiKeyWizard(minecraft, event.getGui()));
        }
    }

    private static class ScreenToggleButton extends GuiButton {
        private ScreenToggleButton(int buttonId, int x, int y) {
            super(buttonId, x, y, 20, 20, "");
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            if (!this.visible) {
                return;
            }

            minecraft.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int hoverState = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            this.drawTexturedModalRect(this.x, this.y, 0, 46 + hoverState * 20, this.width / 2, this.height);
            this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + hoverState * 20, this.width / 2, this.height);
            this.mouseDragged(minecraft, mouseX, mouseY);

            int iconColor = this.enabled ? (this.hovered ? 0xFFFFF0A8 : 0xFFE0E0E0) : 0xFFA0A0A0;
            this.drawIcon(iconColor);
        }

        private void drawIcon(int color) {
            int screenLeft = this.x + 5;
            int screenTop = this.y + 4;
            int screenRight = this.x + 15;
            int screenBottom = this.y + 11;

            drawRect(screenLeft, screenTop, screenRight, screenTop + 1, color);
            drawRect(screenLeft, screenTop, screenLeft + 1, screenBottom, color);
            drawRect(screenRight - 1, screenTop, screenRight, screenBottom, color);
            drawRect(screenLeft, screenBottom - 1, screenRight, screenBottom, color);
            drawRect(this.x + 9, this.y + 11, this.x + 11, this.y + 14, color);
            drawRect(this.x + 6, this.y + 14, this.x + 14, this.y + 15, color);
            drawRect(this.x + 4, this.y + 16, this.x + 16, this.y + 17, color);
            drawRect(this.x + 5, this.y + 17, this.x + 7, this.y + 18, color);
            drawRect(this.x + 9, this.y + 17, this.x + 11, this.y + 18, color);
            drawRect(this.x + 13, this.y + 17, this.x + 15, this.y + 18, color);
        }
    }
}
