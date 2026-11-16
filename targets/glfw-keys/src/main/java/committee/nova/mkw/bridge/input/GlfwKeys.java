package committee.nova.mkw.bridge.input;

import committee.nova.mkw.core.layout.PhysicalKey;

/** GLFW key symbols used by the modern Minecraft input APIs. */
public final class GlfwKeys {
    private GlfwKeys() {
    }

    public static int code(PhysicalKey key) {
        switch (key) {
            case KEY_SPACE: return 32;
            case KEY_APOSTROPHE: return 39;
            case KEY_COMMA: return 44;
            case KEY_MINUS: return 45;
            case KEY_PERIOD: return 46;
            case KEY_SLASH: return 47;
            case KEY_0: return 48;
            case KEY_1: return 49;
            case KEY_2: return 50;
            case KEY_3: return 51;
            case KEY_4: return 52;
            case KEY_5: return 53;
            case KEY_6: return 54;
            case KEY_7: return 55;
            case KEY_8: return 56;
            case KEY_9: return 57;
            case KEY_SEMICOLON: return 59;
            case KEY_EQUAL: return 61;
            case KEY_A: return 65;
            case KEY_B: return 66;
            case KEY_C: return 67;
            case KEY_D: return 68;
            case KEY_E: return 69;
            case KEY_F: return 70;
            case KEY_G: return 71;
            case KEY_H: return 72;
            case KEY_I: return 73;
            case KEY_J: return 74;
            case KEY_K: return 75;
            case KEY_L: return 76;
            case KEY_M: return 77;
            case KEY_N: return 78;
            case KEY_O: return 79;
            case KEY_P: return 80;
            case KEY_Q: return 81;
            case KEY_R: return 82;
            case KEY_S: return 83;
            case KEY_T: return 84;
            case KEY_U: return 85;
            case KEY_V: return 86;
            case KEY_W: return 87;
            case KEY_X: return 88;
            case KEY_Y: return 89;
            case KEY_Z: return 90;
            case KEY_LEFT_BRACKET: return 91;
            case KEY_BACKSLASH: return 92;
            case KEY_RIGHT_BRACKET: return 93;
            case KEY_GRAVE_ACCENT: return 96;
            case KEY_ENTER: return 257;
            case KEY_TAB: return 258;
            case KEY_BACKSPACE: return 259;
            case KEY_INSERT: return 260;
            case KEY_DELETE: return 261;
            case KEY_RIGHT: return 262;
            case KEY_LEFT: return 263;
            case KEY_DOWN: return 264;
            case KEY_UP: return 265;
            case KEY_PAGE_UP: return 266;
            case KEY_PAGE_DOWN: return 267;
            case KEY_HOME: return 268;
            case KEY_END: return 269;
            case KEY_CAPS_LOCK: return 280;
            case KEY_SCROLL_LOCK: return 281;
            case KEY_NUM_LOCK: return 282;
            case KEY_PRINT_SCREEN: return 283;
            case KEY_PAUSE: return 284;
            case KEY_F1: return 290;
            case KEY_F2: return 291;
            case KEY_F3: return 292;
            case KEY_F4: return 293;
            case KEY_F5: return 294;
            case KEY_F6: return 295;
            case KEY_F7: return 296;
            case KEY_F8: return 297;
            case KEY_F9: return 298;
            case KEY_F10: return 299;
            case KEY_F11: return 300;
            case KEY_F12: return 301;
            case KEY_KP_0: return 320;
            case KEY_KP_1: return 321;
            case KEY_KP_2: return 322;
            case KEY_KP_3: return 323;
            case KEY_KP_4: return 324;
            case KEY_KP_5: return 325;
            case KEY_KP_6: return 326;
            case KEY_KP_7: return 327;
            case KEY_KP_8: return 328;
            case KEY_KP_9: return 329;
            case KEY_KP_DECIMAL: return 330;
            case KEY_KP_DIVIDE: return 331;
            case KEY_KP_MULTIPLY: return 332;
            case KEY_KP_SUBTRACT: return 333;
            case KEY_KP_ADD: return 334;
            case KEY_KP_ENTER: return 335;
            case KEY_LEFT_SHIFT: return 340;
            case KEY_LEFT_CONTROL: return 341;
            case KEY_LEFT_ALT: return 342;
            case KEY_LEFT_SUPER: return 343;
            case KEY_RIGHT_SHIFT: return 344;
            case KEY_RIGHT_CONTROL: return 345;
            case KEY_RIGHT_ALT: return 346;
            case KEY_RIGHT_SUPER: return 347;
            default: throw new IllegalArgumentException("Unsupported keyboard key: " + key);
        }
    }
}
