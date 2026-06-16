package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class KeyboardWidgetBuilder {

    public static KeyboardWidget keyboard(KeyWizardScreen keyWizardScreen, KeyboardLayout layout, float anchorX, float anchorY, float width, float height) {
        return switch (layout) {
            case MAIN -> standardKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
            case NUMPAD -> numpadKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
            case AUXILIARY -> auxiliaryKeyboard(keyWizardScreen, anchorX, anchorY, width, height);
        };
    }

    public static KeyboardWidget standardKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        KeyboardWidget kb = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);

        float currentX;
        float currentY = 0;

        float keySpacing = 5;
        float keyWidth = width / 12 - keySpacing;
        float keyHeight = height / 6 - keySpacing;

        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_F1, GLFW.GLFW_KEY_F2, GLFW.GLFW_KEY_F3, GLFW.GLFW_KEY_F4, GLFW.GLFW_KEY_F5, GLFW.GLFW_KEY_F6, GLFW.GLFW_KEY_F7, GLFW.GLFW_KEY_F8, GLFW.GLFW_KEY_F9, GLFW.GLFW_KEY_F10, GLFW.GLFW_KEY_F11, GLFW.GLFW_KEY_F12}, 0, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 15 - keySpacing;
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_GRAVE_ACCENT, GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_3, GLFW.GLFW_KEY_4, GLFW.GLFW_KEY_5, GLFW.GLFW_KEY_6, GLFW.GLFW_KEY_7, GLFW.GLFW_KEY_8, GLFW.GLFW_KEY_9, GLFW.GLFW_KEY_0, GLFW.GLFW_KEY_MINUS, GLFW.GLFW_KEY_EQUAL}, 0, currentY, keyWidth, keyHeight, keySpacing);
        currentX = kb.addKey(currentX, currentY, (keyWidth * 2 + keySpacing), keyHeight, keySpacing, GLFW.GLFW_KEY_BACKSPACE);

        currentY += keyHeight + keySpacing;
        currentX = kb.addKey(0, currentY, keyWidth * 2 + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_TAB);
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_E, GLFW.GLFW_KEY_R, GLFW.GLFW_KEY_T, GLFW.GLFW_KEY_Y, GLFW.GLFW_KEY_U, GLFW.GLFW_KEY_I, GLFW.GLFW_KEY_O, GLFW.GLFW_KEY_P, GLFW.GLFW_KEY_LEFT_BRACKET, GLFW.GLFW_KEY_RIGHT_BRACKET}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        currentX = kb.addKey(currentX, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_BACKSLASH);

        currentY += keyHeight + keySpacing;
        currentX = kb.addKey(0, currentY, keyWidth * 2 + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_CAPS_LOCK);
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_F, GLFW.GLFW_KEY_G, GLFW.GLFW_KEY_H, GLFW.GLFW_KEY_J, GLFW.GLFW_KEY_K, GLFW.GLFW_KEY_L, GLFW.GLFW_KEY_SEMICOLON, GLFW.GLFW_KEY_APOSTROPHE}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        kb.addKey(currentX, currentY, (keyWidth * 2 + keySpacing), keyHeight, keySpacing, GLFW.GLFW_KEY_ENTER);

        currentY += keyHeight + keySpacing;
        currentX = kb.addKey(0, currentY, keyWidth * 2 + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_LEFT_SHIFT);
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_Z, GLFW.GLFW_KEY_X, GLFW.GLFW_KEY_C, GLFW.GLFW_KEY_V, GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_N, GLFW.GLFW_KEY_M, GLFW.GLFW_KEY_COMMA, GLFW.GLFW_KEY_PERIOD, GLFW.GLFW_KEY_SLASH}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        currentX = kb.addKey(currentX, currentY, (keyWidth * 3 + keySpacing * 2), keyHeight, keySpacing, GLFW.GLFW_KEY_RIGHT_SHIFT);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 7 - keySpacing;
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_SPACE, GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_RIGHT_SUPER, GLFW.GLFW_KEY_RIGHT_CONTROL}, 0, currentY, keyWidth, keyHeight, keySpacing);

        return kb;
    }

    public static KeyboardWidget numpadKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        KeyboardWidget kb = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);

        float keySpacing = 5;
        float keyWidth = Math.max(34, Math.min((width - keySpacing * 3) / 4, 75));
        float keyHeight = Math.max(24, (height - keySpacing * 4) / 5);
        float layoutWidth = keyWidth * 4 + keySpacing * 3;
        float xOffset = Math.max(0, (width - layoutWidth) / 2);
        float currentX = 0;
        float currentY = 0;

        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_NUM_LOCK, GLFW.GLFW_KEY_KP_DIVIDE, GLFW.GLFW_KEY_KP_MULTIPLY, GLFW.GLFW_KEY_KP_SUBTRACT}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_KP_7, GLFW.GLFW_KEY_KP_8, GLFW.GLFW_KEY_KP_9}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        kb.addKey(currentX, currentY, keyWidth, keyHeight * 2 + keySpacing, keySpacing, GLFW.GLFW_KEY_KP_ADD);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_KP_4, GLFW.GLFW_KEY_KP_5, GLFW.GLFW_KEY_KP_6}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_KP_1, GLFW.GLFW_KEY_KP_2, GLFW.GLFW_KEY_KP_3}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        kb.addKey(currentX, currentY, keyWidth, keyHeight * 2 + keySpacing, keySpacing, GLFW.GLFW_KEY_KP_ENTER);

        currentY += keyHeight + keySpacing;
        currentX = kb.addKey(xOffset, currentY, keyWidth * 2 + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_KP_0);
        kb.addKey(currentX, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_KP_DECIMAL);

        return kb;
    }

    public static KeyboardWidget auxiliaryKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height) {
        KeyboardWidget kb = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);

        float keySpacing = 5;
        float keyWidth = Math.max(42, Math.min((width - keySpacing * 6) / 6, 95));
        float keyHeight = Math.max(24, (height - keySpacing * 3) / 4);
        float clusterWidth = keyWidth * 3 + keySpacing * 2;
        float clusterGap = keySpacing * 4;
        float layoutWidth = clusterWidth * 2 + clusterGap;
        float xOffset = Math.max(0, (width - layoutWidth) / 2);
        float leftStart = xOffset;
        float arrowStart = xOffset + clusterWidth + clusterGap;
        float currentY = 0;

        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_PRINT_SCREEN, GLFW.GLFW_KEY_SCROLL_LOCK, GLFW.GLFW_KEY_PAUSE}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_INSERT, GLFW.GLFW_KEY_HOME, GLFW.GLFW_KEY_PAGE_UP}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_DELETE, GLFW.GLFW_KEY_END, GLFW.GLFW_KEY_PAGE_DOWN}, leftStart, currentY, keyWidth, keyHeight, keySpacing);
        kb.addKey(arrowStart + keyWidth + keySpacing, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_UP);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(kb, new int[]{GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_RIGHT}, arrowStart, currentY, keyWidth, keyHeight, keySpacing);

        return kb;
    }

    public static KeyboardWidget singleKeyKeyboard(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY, float width, float height, int keyCode, InputConstants.Type keyType) {
        KeyboardWidget kb = new KeyboardWidget(keyWizardScreen, anchorX, anchorY);
        kb.addKey(0, 0, width, height, 0, keyCode, keyType);
        return kb;
    }

    /**
     * Adds a uniformly spaced row to the keyboard it is passed.
     *
     * @return x position of left edge of the last key added
     */
    private static float addHorizontalRow(KeyboardWidget kb, int[] keys, float startX, float y, float width, float height, float spacing) {
        float currentX = startX;
        for (int k : keys) {
            currentX = kb.addKey(currentX, y, width, height, spacing, k);
        }
        return currentX;
    }

}

