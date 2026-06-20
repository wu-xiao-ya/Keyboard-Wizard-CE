package committee.nova.mkw.keybinding;

import net.minecraft.client.input.InputWithModifiers;

public enum KeyModifier {
    CONTROL,
    SHIFT,
    ALT,
    NONE;

    public static KeyModifier getActiveModifier(InputWithModifiers input) {
        if (input.hasControlDown()) {
            return CONTROL;
        }
        if (input.hasShiftDown()) {
            return SHIFT;
        }
        if (input.hasAltDown()) {
            return ALT;
        }
        return NONE;
    }
}
