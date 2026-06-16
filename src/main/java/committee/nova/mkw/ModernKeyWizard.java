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

@Mod(ModernKeyWizard.MODID)
public class ModernKeyWizard {
    public static final String MODID = "keyboard_wizard_ce";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    private static final KeyMapping KEY_OPEN_KEY_WIZARD = new KeyMapping(
            "key." + MODID + ".openKeyWizard",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_F7,
            "key.categories." + MODID + ".bindings"
    );

    public ModernKeyWizard() {
        LOGGER.debug("{} initialized!", MODID);
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
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
            event.addListener(KeyWizardScreen.createScreenToggleButton(
                    controlsScreen.width - 24,
                    controlsScreen.height - 24,
                    btn -> Minecraft.getInstance().setScreen(new KeyWizardScreen(controlsScreen))
            ));
        }
    }
}
