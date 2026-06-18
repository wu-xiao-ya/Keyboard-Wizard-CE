package mrnerdy42.keywizard.handlers;

import org.lwjgl.input.Keyboard;

import mrnerdy42.keywizard.gui.GuiKeyWizard;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {

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
}
