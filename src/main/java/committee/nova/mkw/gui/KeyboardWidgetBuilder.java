package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.layout.KeyInputKind;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;

public class KeyboardWidgetBuilder {

    public static KeyboardWidget keyboard(KeyWizardScreen keyWizardScreen, KeyboardLayout layout, float anchorX, float anchorY, float width, float height) {
        return switch (layout) {
            case MAIN -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.standard(width, height));
            case NUMPAD -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.numpad(width, height));
            case AUXILIARY -> keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.auxiliary(width, height));
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

    public static KeyboardWidget singleKeyKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height, int keyCode, InputConstants.Type keyType) {
        KeyInputKind inputKind = keyType == InputConstants.Type.MOUSE ? KeyInputKind.MOUSE : KeyInputKind.KEYSYM;
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, KeyboardLayoutGeometry.singleKey(width, height, keyCode, inputKind));
    }

    private static KeyboardWidget keyboardFromGeometry(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, java.util.List<KeyPlacement> placements) {
        KeyboardWidget keyboard = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        for (KeyPlacement placement : placements) {
            keyboard.addKey(placement.x(), placement.y(), placement.width(), placement.height(), 0.0F, placement.keyCode(), toInputType(placement.inputKind()));
        }
        return keyboard;
    }

    private static InputConstants.Type toInputType(KeyInputKind inputKind) {
        return inputKind == KeyInputKind.MOUSE ? InputConstants.Type.MOUSE : InputConstants.Type.KEYSYM;
    }
}
