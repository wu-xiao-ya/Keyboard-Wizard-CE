package committee.nova.mkw;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.gui.KeyWizardScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(value = ModernKeyWizard.MODID, dist = Dist.CLIENT)
public class ModernKeyWizard {
    public static final String MODID = "keyboard_wizard_community_edition";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final String KEY_WIZARD_CATEGORY = "key.categories.keyboard_wizard_ce.bindings";

    // The mapping name is stored in options.txt.
    private static final String OPEN_KEY_WIZARD_MAPPING = "key.keyboard_wizard_ce.openKeyWizard";

    private static final KeyMapping KEY_OPEN_KEY_WIZARD = new KeyMapping(
            OPEN_KEY_WIZARD_MAPPING,
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            KEY_WIZARD_CATEGORY
    );

    public ModernKeyWizard() {
        LOGGER.debug("{} initialized!", MODID);
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            event.register(KEY_OPEN_KEY_WIZARD);
        }
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft client = Minecraft.getInstance();
            while (KEY_OPEN_KEY_WIZARD.consumeClick()) {
                client.setScreen(new KeyWizardScreen(null));
            }
        }

        @SubscribeEvent
        public static void onScreenInit(ScreenEvent.Init.Post event) {
            if (!(event.getScreen() instanceof ControlsScreen controlsScreen)) return;
            event.addListener(KeyWizardScreen.createControlsEntryButton(controlsScreen));
        }

        @SubscribeEvent
        public static void onScreenRender(ScreenEvent.Render.Pre event) {
            if (!(event.getScreen() instanceof ControlsScreen controlsScreen)) return;
            KeyWizardScreen.layoutControlsEntryButton(controlsScreen);
        }
    }
}
