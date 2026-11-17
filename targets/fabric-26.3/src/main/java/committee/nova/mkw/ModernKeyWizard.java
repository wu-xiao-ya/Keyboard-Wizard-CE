package committee.nova.mkw;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.ScreenToggleSprite;
import committee.nova.mkw.gui.KeyWizardScreen;
import committee.nova.mkw.util.MinecraftCompat;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
public class ModernKeyWizard implements ClientModInitializer {
    public static final String MODID = "keyboard_wizard_community_edition";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Identifier SCREEN_TOGGLE_WIDGETS = Identifier.fromNamespaceAndPath(MODID, ScreenToggleSprite.TEXTURE_PATH);

    private static boolean wasOpenKeyDown;

    @Override
    public void onInitializeClient() {
        LOGGER.debug("{} initialized!", MODID);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Minecraft minecraft = Minecraft.getInstance();
            boolean isOpenKeyDown = InputConstants.isKeyDown(InputConstants.KEY_F7);
            if (isOpenKeyDown && !wasOpenKeyDown) {
                MinecraftCompat.setScreen(minecraft, new KeyWizardScreen(MinecraftCompat.getScreen(minecraft)));
            }
            wasOpenKeyDown = isOpenKeyDown;
        });
    }
}
