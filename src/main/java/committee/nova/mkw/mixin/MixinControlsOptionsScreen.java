package committee.nova.mkw.mixin;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.gui.KeyWizardScreen;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public abstract class MixinControlsOptionsScreen extends OptionsSubScreen {
    public MixinControlsOptionsScreen(Screen parent, Options gameOptions, Component title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    private void init(CallbackInfo info) {
        ModernKeyWizard.LOGGER.debug("Controls screen injector mixin loaded!");
        ImageButton screenToggleButton = new ImageButton(this.width - 22, this.height - 22, 20, 20, 0, 0, 20, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, (btn) -> {
            if (minecraft == null) return;
            minecraft.setScreen(new KeyWizardScreen(this.lastScreen));
        });
        this.addRenderableWidget(screenToggleButton);
    }

}

