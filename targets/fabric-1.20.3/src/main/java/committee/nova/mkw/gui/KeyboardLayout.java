package committee.nova.mkw.gui;

import net.minecraft.text.Text;

public enum KeyboardLayout {
    MAIN("gui.keyboard_wizard_ce.layout.main"),
    NUMPAD("gui.keyboard_wizard_ce.layout.numpad"),
    AUXILIARY("gui.keyboard_wizard_ce.layout.auxiliary");

    private final String translationKey;

    KeyboardLayout(String translationKey) {
        this.translationKey = translationKey;
    }

    public Text getDisplayName() {
        return Text.translatable(this.translationKey);
    }

    public KeyboardLayout next() {
        KeyboardLayout[] layouts = values();
        return layouts[(this.ordinal() + 1) % layouts.length];
    }
}
