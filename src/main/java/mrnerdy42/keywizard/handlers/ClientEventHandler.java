package mrnerdy42.keywizard.handlers;

import org.lwjgl.input.Keyboard;

import mrnerdy42.keywizard.KeyWizardConfig;
import mrnerdy42.keywizard.gui.GuiKeyWizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
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
        event.getButtonList().add(new GuiButton(
                OPEN_KEY_WIZARD_BUTTON_ID,
                x,
                y,
                20,
                20,
                "K"));
    }

    @SubscribeEvent
    public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (event.getButton().id == OPEN_KEY_WIZARD_BUTTON_ID && event.getGui() instanceof GuiOptions) {
            Minecraft minecraft = Minecraft.getMinecraft();
            minecraft.displayGuiScreen(new GuiKeyWizard(minecraft, event.getGui()));
        }
    }
}
