package committee.nova.mkw;

import committee.nova.mkw.gui.KeyWizardScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(ModernKeyWizard.MODID)
public class ModernKeyWizard {
    public static final String MODID = "keyboard_wizard_ce";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final Identifier SCREEN_TOGGLE_WIDGETS = Identifier.fromNamespaceAndPath(MODID, "textures/gui/screen_toggle_widgets.png");

    private static KeyMapping keyOpenKeyWizard;
    private static final KeyMapping.Category MKW_CATEGORY = KeyMapping.Category.register(Identifier.fromNamespaceAndPath(MODID, "bindings"));

    public ModernKeyWizard(IEventBus modEventBus) {
        LOGGER.debug("{} initialized!", MODID);
        modEventBus.addListener(ModernKeyWizard::registerKeyMappings);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        keyOpenKeyWizard = new KeyMapping(
                "key." + MODID + ".openKeyWizard",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_F7,
                MKW_CATEGORY
        );
        event.register(keyOpenKeyWizard);
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            Minecraft client = Minecraft.getInstance();
            if (keyOpenKeyWizard == null) {
                return;
            }

            while (keyOpenKeyWizard.consumeClick()) {
                client.setScreen(new KeyWizardScreen(client.screen));
            }
        }
    }
}
