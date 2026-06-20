package committee.nova.mkw.mixin;

import committee.nova.mkw.api.IKeyBinding;
import committee.nova.mkw.api.IKeyConflictContext;
import committee.nova.mkw.keybinding.KeyModifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding implements IKeyBinding {
    @Shadow
    private InputUtil.Key boundKey;

    @Shadow
    public abstract void setBoundKey(InputUtil.Key boundKey);

    @Unique
    private IKeyConflictContext keyWizardCe$conflictContext = IKeyBinding.defaultContext();

    @Unique
    private KeyModifier keyWizardCe$keyModifier = KeyModifier.NONE;

    @Unique
    private KeyModifier keyWizardCe$keyModifierDefault = KeyModifier.NONE;

    @Override
    public InputUtil.Key getKey() {
        return this.boundKey;
    }

    @Override
    public IKeyConflictContext getKeyConflictContext() {
        return this.keyWizardCe$conflictContext;
    }

    @Override
    public KeyModifier getKeyModifierDefault() {
        return this.keyWizardCe$keyModifierDefault;
    }

    @Override
    public KeyModifier getKeyModifier() {
        return this.keyWizardCe$keyModifier;
    }

    @Override
    public void setKeyConflictContext(IKeyConflictContext keyConflictContext) {
        this.keyWizardCe$conflictContext = keyConflictContext;
    }

    @Override
    public void setKeyModifierAndCode(KeyModifier keyModifier, InputUtil.Key keyCode) {
        this.keyWizardCe$keyModifier = KeyModifier.isKeyCodeModifier(keyCode) ? KeyModifier.NONE : keyModifier;
        setBoundKey(keyCode);
    }
}
