package committee.nova.mkw.gui;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum KeyboardLayout {
    MAIN("gui.keyboard_wizard_ce.layout.main"),
    NUMPAD("gui.keyboard_wizard_ce.layout.numpad"),
    AUXILIARY("gui.keyboard_wizard_ce.layout.auxiliary");

    private final String translationKey;

    KeyboardLayout(String translationKey) {
        this.translationKey = translationKey;
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent(this.translationKey);
    }
}
