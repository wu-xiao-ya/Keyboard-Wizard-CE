package committee.nova.mkw.mixin;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.gui.KeyWizardScreen;
import committee.nova.mkw.util.MinecraftCompat;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.Options;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsScreen.class)
public abstract class MixinControlsOptionsScreen extends OptionsSubScreen {
    public MixinControlsOptionsScreen(Screen parent, Options options, Component title) {
        super(parent, options, title);
    }

    @Inject(at = @At("TAIL"), method = "addOptions")
    private void addScreenToggleButton(CallbackInfo info) {
        ModernKeyWizard.LOGGER.debug("Controls screen injector mixin loaded!");
        Button screenToggleButton = KeyWizardScreen.createScreenToggleButton(
                this.width - 22,
                this.height - 22,
                btn -> {
                    if (minecraft == null) return;
                    MinecraftCompat.setScreen(minecraft, new KeyWizardScreen(this.lastScreen));
                }
        );
        this.addRenderableWidget(screenToggleButton);
    }
}
