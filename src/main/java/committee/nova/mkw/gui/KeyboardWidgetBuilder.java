package committee.nova.mkw.gui;

import committee.nova.mkw.core.layout.KeyInputKind;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;
import net.minecraft.client.util.InputUtil;

public class KeyboardWidgetBuilder {

    public static KeyboardWidget keyboard(KeyWizardScreen keyWizardScreen, KeyboardLayout layout, float anchorX, float anchorY, float width, float height) {
        return switch (layout) {
            case NUMPAD -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.numpad(width, height));
            case AUXILIARY -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.auxiliary(width, height));
            case MAIN -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.standard(width, height));
        };
    }

    public static KeyboardWidget standardKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.standard(width, height));
    }

    public static KeyboardWidget numpadKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.numpad(width, height));
    }

    public static KeyboardWidget auxiliaryKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.auxiliary(width, height));
    }

    public static KeyboardWidget singleKeyKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height, int keyCode, InputUtil.Type keyType) {
        KeyInputKind inputKind = keyType == InputUtil.Type.MOUSE ? KeyInputKind.MOUSE : KeyInputKind.KEYSYM;
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.singleKey(width, height, keyCode, inputKind));
    }

    private static KeyboardWidget keyboardFromGeometry(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, java.util.List<KeyPlacement> placements) {
        KeyboardWidget keyboard = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        for (KeyPlacement placement : placements) {
            keyboard.addKey(placement.x(), placement.y(), placement.width(), placement.height(), 0.0F, placement.keyCode(), toInputType(placement.inputKind()));
        }
        return keyboard;
    }

    private static InputUtil.Type toInputType(KeyInputKind inputKind) {
        return inputKind == KeyInputKind.MOUSE ? InputUtil.Type.MOUSE : InputUtil.Type.KEYSYM;
    }

}


