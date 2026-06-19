package committee.nova.mkw;

import committee.nova.mkw.gui.KeyWizardScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ClientRegistry;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Mod(ModernKeyWizard.MODID)
public class ModernKeyWizard {
    public static final String MODID = "keyboard_wizard_ce";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    public static final ResourceLocation SCREEN_TOGGLE_WIDGETS = new ResourceLocation(MODID, "textures/gui/screen_toggle_widgets.png");

    private static KeyBinding keyOpenKeyWizard;

    public ModernKeyWizard() {
        LOGGER.debug("{} initialized!", MODID);
    }

    @Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModClientEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            keyOpenKeyWizard = new KeyBinding("key." + MODID + ".openKeyWizard", GLFW.GLFW_KEY_F7, "key.categories." + MODID + ".bindings");
            ClientRegistry.registerKeyBinding(keyOpenKeyWizard);
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

        @SubscribeEvent
        public static void onScreenInit(ScreenEvent.Init.Post event) {
            if (!(event.getScreen() instanceof ControlsScreen)) return;
            ControlsScreen controlsScreen = (ControlsScreen) event.getScreen();
            ImageButton screenToggleButton = new ImageButton(
                    controlsScreen.width - 22,
                    controlsScreen.height - 22,
                    20,
                    20,
                    0,
                    0,
                    20,
                    SCREEN_TOGGLE_WIDGETS,
                    40,
                    40,
                    btn -> Minecraft.getInstance().setScreen(new KeyWizardScreen(controlsScreen))
            );
            event.addWidget(screenToggleButton);
        }
    }
}
