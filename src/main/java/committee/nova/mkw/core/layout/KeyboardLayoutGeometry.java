package committee.nova.mkw.core.layout;

import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardLayoutGeometry {
    private KeyboardLayoutGeometry() {
    }

    public static List<KeyPlacement> standard(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float currentY = 0.0F;
        float keySpacing = 5.0F;
        float keyWidth = width / 12.0F - keySpacing;
        float keyHeight = height / 6.0F - keySpacing;

        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_F1, GLFW.GLFW_KEY_F2, GLFW.GLFW_KEY_F3, GLFW.GLFW_KEY_F4, GLFW.GLFW_KEY_F5, GLFW.GLFW_KEY_F6, GLFW.GLFW_KEY_F7, GLFW.GLFW_KEY_F8, GLFW.GLFW_KEY_F9, GLFW.GLFW_KEY_F10, GLFW.GLFW_KEY_F11, GLFW.GLFW_KEY_F12}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 15.0F - keySpacing;
        float currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_GRAVE_ACCENT, GLFW.GLFW_KEY_1, GLFW.GLFW_KEY_2, GLFW.GLFW_KEY_3, GLFW.GLFW_KEY_4, GLFW.GLFW_KEY_5, GLFW.GLFW_KEY_6, GLFW.GLFW_KEY_7, GLFW.GLFW_KEY_8, GLFW.GLFW_KEY_9, GLFW.GLFW_KEY_0, GLFW.GLFW_KEY_MINUS, GLFW.GLFW_KEY_EQUAL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        currentX = addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_BACKSPACE, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_TAB, KeyInputKind.KEYSYM);
        currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_Q, GLFW.GLFW_KEY_W, GLFW.GLFW_KEY_E, GLFW.GLFW_KEY_R, GLFW.GLFW_KEY_T, GLFW.GLFW_KEY_Y, GLFW.GLFW_KEY_U, GLFW.GLFW_KEY_I, GLFW.GLFW_KEY_O, GLFW.GLFW_KEY_P, GLFW.GLFW_KEY_LEFT_BRACKET, GLFW.GLFW_KEY_RIGHT_BRACKET}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_BACKSLASH, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_CAPS_LOCK, KeyInputKind.KEYSYM);
        currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_A, GLFW.GLFW_KEY_S, GLFW.GLFW_KEY_D, GLFW.GLFW_KEY_F, GLFW.GLFW_KEY_G, GLFW.GLFW_KEY_H, GLFW.GLFW_KEY_J, GLFW.GLFW_KEY_K, GLFW.GLFW_KEY_L, GLFW.GLFW_KEY_SEMICOLON, GLFW.GLFW_KEY_APOSTROPHE}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_ENTER, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_LEFT_SHIFT, KeyInputKind.KEYSYM);
        currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_Z, GLFW.GLFW_KEY_X, GLFW.GLFW_KEY_C, GLFW.GLFW_KEY_V, GLFW.GLFW_KEY_B, GLFW.GLFW_KEY_N, GLFW.GLFW_KEY_M, GLFW.GLFW_KEY_COMMA, GLFW.GLFW_KEY_PERIOD, GLFW.GLFW_KEY_SLASH}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 3.0F + keySpacing * 2.0F, keyHeight, keySpacing, GLFW.GLFW_KEY_RIGHT_SHIFT, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 7.0F - keySpacing;
        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_LEFT_SUPER, GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_SPACE, GLFW.GLFW_KEY_RIGHT_ALT, GLFW.GLFW_KEY_RIGHT_SUPER, GLFW.GLFW_KEY_RIGHT_CONTROL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        return placements;
    }

    public static List<KeyPlacement> numpad(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float keySpacing = 5.0F;
        float keyWidth = Math.max(34.0F, Math.min((width - keySpacing * 3.0F) / 4.0F, 75.0F));
        float keyHeight = Math.max(24.0F, (height - keySpacing * 4.0F) / 5.0F);
        float layoutWidth = keyWidth * 4.0F + keySpacing * 3.0F;
        float xOffset = Math.max(0.0F, (width - layoutWidth) / 2.0F);
        float currentY = 0.0F;

        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_NUM_LOCK, GLFW.GLFW_KEY_KP_DIVIDE, GLFW.GLFW_KEY_KP_MULTIPLY, GLFW.GLFW_KEY_KP_SUBTRACT}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        float currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_KP_7, GLFW.GLFW_KEY_KP_8, GLFW.GLFW_KEY_KP_9}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, GLFW.GLFW_KEY_KP_ADD, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_KP_4, GLFW.GLFW_KEY_KP_5, GLFW.GLFW_KEY_KP_6}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_KP_1, GLFW.GLFW_KEY_KP_2, GLFW.GLFW_KEY_KP_3}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, GLFW.GLFW_KEY_KP_ENTER, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, xOffset, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, GLFW.GLFW_KEY_KP_0, KeyInputKind.KEYSYM);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_KP_DECIMAL, KeyInputKind.KEYSYM);
        return placements;
    }

    public static List<KeyPlacement> auxiliary(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float keySpacing = 5.0F;
        float keyWidth = Math.max(42.0F, Math.min((width - keySpacing * 6.0F) / 6.0F, 95.0F));
        float keyHeight = Math.max(24.0F, (height - keySpacing * 3.0F) / 4.0F);
        float clusterWidth = keyWidth * 3.0F + keySpacing * 2.0F;
        float clusterGap = keySpacing * 4.0F;
        float layoutWidth = clusterWidth * 2.0F + clusterGap;
        float xOffset = Math.max(0.0F, (width - layoutWidth) / 2.0F);
        float leftStart = xOffset;
        float arrowStart = xOffset + clusterWidth + clusterGap;
        float currentY = 0.0F;

        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_PRINT_SCREEN, GLFW.GLFW_KEY_SCROLL_LOCK, GLFW.GLFW_KEY_PAUSE}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_INSERT, GLFW.GLFW_KEY_HOME, GLFW.GLFW_KEY_PAGE_UP}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_DELETE, GLFW.GLFW_KEY_END, GLFW.GLFW_KEY_PAGE_DOWN}, leftStart, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, arrowStart + keyWidth + keySpacing, currentY, keyWidth, keyHeight, keySpacing, GLFW.GLFW_KEY_UP, KeyInputKind.KEYSYM);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{GLFW.GLFW_KEY_LEFT, GLFW.GLFW_KEY_DOWN, GLFW.GLFW_KEY_RIGHT}, arrowStart, currentY, keyWidth, keyHeight, keySpacing);
        return placements;
    }

    public static List<KeyPlacement> singleKey(float width, float height, int keyCode, KeyInputKind inputKind) {
        return List.of(new KeyPlacement(0.0F, 0.0F, width, height, keyCode, inputKind));
    }

    private static float addHorizontalRow(List<KeyPlacement> placements, int[] keys, float startX, float y, float width, float height, float spacing) {
        float currentX = startX;
        for (int key : keys) {
            currentX = addKey(placements, currentX, y, width, height, spacing, key, KeyInputKind.KEYSYM);
        }
        return currentX;
    }

    private static float addKey(List<KeyPlacement> placements, float x, float y, float width, float height, float spacing, int keyCode, KeyInputKind inputKind) {
        placements.add(new KeyPlacement(x, y, width, height, keyCode, inputKind));
        return x + width + spacing;
    }
}
