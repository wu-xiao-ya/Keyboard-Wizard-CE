package committee.nova.mkw.gui;

import committee.nova.mkw.bridge.input.GlfwKeys;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;
import net.minecraft.client.util.InputMappings;

import java.util.List;

public class KeyboardWidgetBuilder {

    public static KeyboardWidget keyboard(KeyWizardScreen keyWizardScreen, KeyboardLayout layout, float anchorX, float anchorY, float width, float height) {
        switch (layout) {
            case NUMPAD:
                return numpadKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
            case AUXILIARY:
                return auxiliaryKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
            case MAIN:
            default:
                return standardKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
        }
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

    public static KeyboardWidget singleKeyKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height, int keyCode, InputMappings.Type keyType) {
        KeyboardWidget keyboard = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        keyboard.addKey(0.0F, 0.0F, width, height, 0.0F, keyCode, keyType);
        return keyboard;
    }

    private static KeyboardWidget keyboardFromGeometry(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, List<KeyPlacement> placements) {
        KeyboardWidget keyboard = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        for (KeyPlacement placement : placements) {
            keyboard.addKey(placement.x(), placement.y(), placement.width(), placement.height(), 0.0F, GlfwKeys.code(placement.key()), InputMappings.Type.KEYSYM);
        }
        return keyboard;
    }
}
