package committee.nova.mkw.gui;

import net.minecraft.network.chat.Component;

public enum KeyboardLayout {
    MAIN("gui.mkw.layout.main"),
    NUMPAD("gui.mkw.layout.numpad"),
    AUXILIARY("gui.mkw.layout.auxiliary");

    private final String translationKey;

    KeyboardLayout(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }

    public KeyboardLayout next() {
        KeyboardLayout[] layouts = values();
        return layouts[(this.ordinal() + 1) % layouts.length];
    }
}
