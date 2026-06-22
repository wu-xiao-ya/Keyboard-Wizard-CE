package committee.nova.mkw.mixin;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.gui.KeyWizardScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ControlsOptionsScreen.class)
public abstract class MixinControlsOptionsScreen extends GameOptionsScreen {
    public MixinControlsOptionsScreen(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("TAIL"), method = "init()V")
    private void init(CallbackInfo info) {
        ModernKeyWizard.LOGGER.debug("Controls screen injector mixin loaded!");
        ButtonWidget screenToggleButton = KeyWizardScreen.createScreenToggleButton(this.width - 22, this.height - 22, (btn) -> {
            if (client == null) return;
            client.setScreen(new KeyWizardScreen(this.parent));
        });
        this.addDrawableChild(screenToggleButton);
    }

}
