package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;

import committee.nova.mkw.bridge.input.GlfwKeys;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;

import java.util.List;

public class KeyboardWidgetBuilder {

    public static KeyboardWidget keyboard(KeyWizardScreen keyWizardScreen, KeyboardLayout layout, float anchorX, float anchorY, float width, float height) {
        List<KeyPlacement> geometry = switch (layout) {
            case MAIN -> KeyboardLayoutGeometry.standard(width, height);
            case NUMPAD -> KeyboardLayoutGeometry.numpad(width, height);
            case AUXILIARY -> KeyboardLayoutGeometry.auxiliary(width, height);
        };
        return keyboardFromGeometry(keyWizardScreen, anchorX, anchorY, geometry);
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
        KeyboardWidget keyboard = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        keyboard.addKey(0.0F, 0.0F, width, height, 0.0F, keyCode, keyType);
        return keyboard;
    }

    private static KeyboardWidget keyboardFromGeometry(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, List<KeyPlacement> geometry) {
        KeyboardWidget kb = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        for (KeyPlacement placement : geometry) {
            kb.addKey(placement.x(), placement.y(), placement.width(), placement.height(), 0.0F, GlfwKeys.code(placement.key()), InputConstants.Type.KEYSYM);
        }
        return kb;
    }
}
