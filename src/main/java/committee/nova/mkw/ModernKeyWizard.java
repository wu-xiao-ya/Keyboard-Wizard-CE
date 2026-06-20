package committee.nova.mkw;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.gui.KeyWizardScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class ModernKeyWizard implements ClientModInitializer {
    public static final String MODID = "keyboard_wizard_ce";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Identifier SCREEN_TOGGLE_WIDGETS = Identifier.fromNamespaceAndPath(MODID, "textures/gui/screen_toggle_widgets.png");

    private static KeyMapping keyOpenKeyWizard;
    private static final KeyMapping.Category MKW_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MODID, "bindings"));

    @Override
    public void onInitializeClient() {
        LOGGER.debug("{} initialized!", MODID);
        keyOpenKeyWizard = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key." + MODID + ".openKeyWizard",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                MKW_CATEGORY
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft minecraft = Minecraft.getInstance();
            while (keyOpenKeyWizard.consumeClick()) {
                minecraft.setScreen(new KeyWizardScreen(minecraft.screen));
            }
        });
    }
}
