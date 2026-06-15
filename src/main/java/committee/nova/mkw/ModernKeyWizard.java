package committee.nova.mkw;

import committee.nova.mkw.gui.KeyWizardScreen;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(ModernKeyWizard.MODID)
public class ModernKeyWizard {
    public static final String MODID = "mkw";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ResourceLocation SCREEN_TOGGLE_WIDGETS = ResourceLocation.fromNamespaceAndPath(MODID, "textures/gui/screen_toggle_widgets.png");

    private static KeyMapping keyOpenKeyWizard;

    public ModernKeyWizard() {
        LOGGER.debug("{} initialized!", MODID);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void registerKeyMappings(RegisterKeyMappingsEvent event) {
            keyOpenKeyWizard = new KeyMapping("key." + MODID + ".openKeyWizard", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_F7, "key.categories." + MODID + ".bindings");
            event.register(keyOpenKeyWizard);
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeClientEvents {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase != TickEvent.Phase.END || keyOpenKeyWizard == null) return;
            Minecraft client = Minecraft.getInstance();
            while (keyOpenKeyWizard.consumeClick()) {
                client.setScreen(new KeyWizardScreen(client.screen));
            }
        }
    }

}

