package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;

import java.util.ArrayList;
import java.util.List;

public final class KeyboardLayoutGeometry {
    // GLFW key tokens. Minecraft 26.3 no longer exposes org.lwjgl.glfw to mods.
    private static final int KEY_SPACE = 32;
    private static final int KEY_APOSTROPHE = 39;
    private static final int KEY_COMMA = 44;
    private static final int KEY_MINUS = 45;
    private static final int KEY_PERIOD = 46;
    private static final int KEY_SLASH = 47;
    private static final int KEY_0 = 48;
    private static final int KEY_1 = 49;
    private static final int KEY_2 = 50;
    private static final int KEY_3 = 51;
    private static final int KEY_4 = 52;
    private static final int KEY_5 = 53;
    private static final int KEY_6 = 54;
    private static final int KEY_7 = 55;
    private static final int KEY_8 = 56;
    private static final int KEY_9 = 57;
    private static final int KEY_SEMICOLON = 59;
    private static final int KEY_EQUAL = 61;
    private static final int KEY_A = 65;
    private static final int KEY_B = 66;
    private static final int KEY_C = 67;
    private static final int KEY_D = 68;
    private static final int KEY_E = 69;
    private static final int KEY_F = 70;
    private static final int KEY_G = 71;
    private static final int KEY_H = 72;
    private static final int KEY_I = 73;
    private static final int KEY_J = 74;
    private static final int KEY_K = 75;
    private static final int KEY_L = 76;
    private static final int KEY_M = 77;
    private static final int KEY_N = 78;
    private static final int KEY_O = 79;
    private static final int KEY_P = 80;
    private static final int KEY_Q = 81;
    private static final int KEY_R = 82;
    private static final int KEY_S = 83;
    private static final int KEY_T = 84;
    private static final int KEY_U = 85;
    private static final int KEY_V = 86;
    private static final int KEY_W = 87;
    private static final int KEY_X = 88;
    private static final int KEY_Y = 89;
    private static final int KEY_Z = 90;
    private static final int KEY_LEFT_BRACKET = 91;
    private static final int KEY_BACKSLASH = 92;
    private static final int KEY_RIGHT_BRACKET = 93;
    private static final int KEY_GRAVE_ACCENT = 96;
    private static final int KEY_ESCAPE = 256;
    private static final int KEY_ENTER = 257;
    private static final int KEY_TAB = 258;
    private static final int KEY_BACKSPACE = 259;
    private static final int KEY_INSERT = 260;
    private static final int KEY_DELETE = 261;
    private static final int KEY_RIGHT = 262;
    private static final int KEY_LEFT = 263;
    private static final int KEY_DOWN = 264;
    private static final int KEY_UP = 265;
    private static final int KEY_PAGE_UP = 266;
    private static final int KEY_PAGE_DOWN = 267;
    private static final int KEY_HOME = 268;
    private static final int KEY_END = 269;
    private static final int KEY_CAPS_LOCK = 280;
    private static final int KEY_SCROLL_LOCK = 281;
    private static final int KEY_NUM_LOCK = 282;
    private static final int KEY_PRINT_SCREEN = 283;
    private static final int KEY_PAUSE = 284;
    private static final int KEY_F1 = 290;
    private static final int KEY_F2 = 291;
    private static final int KEY_F3 = 292;
    private static final int KEY_F4 = 293;
    private static final int KEY_F5 = 294;
    private static final int KEY_F6 = 295;
    private static final int KEY_F7 = 296;
    private static final int KEY_F8 = 297;
    private static final int KEY_F9 = 298;
    private static final int KEY_F10 = 299;
    private static final int KEY_F11 = 300;
    private static final int KEY_F12 = 301;
    private static final int KEY_KP_0 = 320;
    private static final int KEY_KP_1 = 321;
    private static final int KEY_KP_2 = 322;
    private static final int KEY_KP_3 = 323;
    private static final int KEY_KP_4 = 324;
    private static final int KEY_KP_5 = 325;
    private static final int KEY_KP_6 = 326;
    private static final int KEY_KP_7 = 327;
    private static final int KEY_KP_8 = 328;
    private static final int KEY_KP_9 = 329;
    private static final int KEY_KP_DECIMAL = 330;
    private static final int KEY_KP_DIVIDE = 331;
    private static final int KEY_KP_MULTIPLY = 332;
    private static final int KEY_KP_SUBTRACT = 333;
    private static final int KEY_KP_ADD = 334;
    private static final int KEY_KP_ENTER = 335;
    private static final int KEY_LEFT_SHIFT = 340;
    private static final int KEY_LEFT_CONTROL = 341;
    private static final int KEY_LEFT_ALT = 342;
    private static final int KEY_LEFT_SUPER = 343;
    private static final int KEY_RIGHT_SHIFT = 344;
    private static final int KEY_RIGHT_CONTROL = 345;
    private static final int KEY_RIGHT_ALT = 346;
    private static final int KEY_RIGHT_SUPER = 347;
    private KeyboardLayoutGeometry() {
    }

    public static List<KeyPlacement> standard(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float currentY = 0.0F;
        float keySpacing = 5.0F;
        float keyWidth = width / 12.0F - keySpacing;
        float keyHeight = height / 6.0F - keySpacing;

        addHorizontalRow(placements, new int[]{KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10, KEY_F11, KEY_F12}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 15.0F - keySpacing;
        float currentX = addHorizontalRow(placements, new int[]{KEY_GRAVE_ACCENT, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_0, KEY_MINUS, KEY_EQUAL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        currentX = addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_BACKSPACE, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_TAB, InputConstants.Type.KEYBOARD);
        currentX = addHorizontalRow(placements, new int[]{KEY_Q, KEY_W, KEY_E, KEY_R, KEY_T, KEY_Y, KEY_U, KEY_I, KEY_O, KEY_P, KEY_LEFT_BRACKET, KEY_RIGHT_BRACKET}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, KEY_BACKSLASH, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_CAPS_LOCK, InputConstants.Type.KEYBOARD);
        currentX = addHorizontalRow(placements, new int[]{KEY_A, KEY_S, KEY_D, KEY_F, KEY_G, KEY_H, KEY_J, KEY_K, KEY_L, KEY_SEMICOLON, KEY_APOSTROPHE}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_ENTER, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_LEFT_SHIFT, InputConstants.Type.KEYBOARD);
        currentX = addHorizontalRow(placements, new int[]{KEY_Z, KEY_X, KEY_C, KEY_V, KEY_B, KEY_N, KEY_M, KEY_COMMA, KEY_PERIOD, KEY_SLASH}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 3.0F + keySpacing * 2.0F, keyHeight, keySpacing, KEY_RIGHT_SHIFT, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 7.0F - keySpacing;
        addHorizontalRow(placements, new int[]{KEY_LEFT_CONTROL, KEY_LEFT_SUPER, KEY_LEFT_ALT, KEY_SPACE, KEY_RIGHT_ALT, KEY_RIGHT_SUPER, KEY_RIGHT_CONTROL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
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

        addHorizontalRow(placements, new int[]{KEY_NUM_LOCK, KEY_KP_DIVIDE, KEY_KP_MULTIPLY, KEY_KP_SUBTRACT}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        float currentX = addHorizontalRow(placements, new int[]{KEY_KP_7, KEY_KP_8, KEY_KP_9}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, KEY_KP_ADD, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{KEY_KP_4, KEY_KP_5, KEY_KP_6}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(placements, new int[]{KEY_KP_1, KEY_KP_2, KEY_KP_3}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, KEY_KP_ENTER, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, xOffset, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_KP_0, InputConstants.Type.KEYBOARD);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, KEY_KP_DECIMAL, InputConstants.Type.KEYBOARD);
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

        addHorizontalRow(placements, new int[]{KEY_PRINT_SCREEN, KEY_SCROLL_LOCK, KEY_PAUSE}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{KEY_INSERT, KEY_HOME, KEY_PAGE_UP}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{KEY_DELETE, KEY_END, KEY_PAGE_DOWN}, leftStart, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, arrowStart + keyWidth + keySpacing, currentY, keyWidth, keyHeight, keySpacing, KEY_UP, InputConstants.Type.KEYBOARD);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new int[]{KEY_LEFT, KEY_DOWN, KEY_RIGHT}, arrowStart, currentY, keyWidth, keyHeight, keySpacing);
        return placements;
    }

    public static List<KeyPlacement> singleKey(float width, float height, int keyCode, InputConstants.Type inputKind) {
        return List.of(new KeyPlacement(0.0F, 0.0F, width, height, keyCode, inputKind));
    }

    private static float addHorizontalRow(List<KeyPlacement> placements, int[] keys, float startX, float y, float width, float height, float spacing) {
        float currentX = startX;
        for (int key : keys) {
            currentX = addKey(placements, currentX, y, width, height, spacing, key, InputConstants.Type.KEYBOARD);
        }
        return currentX;
    }

    private static float addKey(List<KeyPlacement> placements, float x, float y, float width, float height, float spacing, int keyCode, InputConstants.Type inputKind) {
        placements.add(new KeyPlacement(x, y, width, height, keyCode, inputKind));
        return x + width + spacing;
    }
}
