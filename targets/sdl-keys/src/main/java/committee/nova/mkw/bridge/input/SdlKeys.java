package committee.nova.mkw.bridge.input;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.layout.PhysicalKey;

/** Physical keyboard scancodes as exposed by Minecraft 26.3. */
public final class SdlKeys {
    private SdlKeys() {
    }

    public static int code(PhysicalKey key) {
        return switch (key) {
            case KEY_SPACE -> InputConstants.KEY_SPACE;
            case KEY_APOSTROPHE -> InputConstants.KEY_APOSTROPHE;
            case KEY_COMMA -> InputConstants.KEY_COMMA;
            case KEY_MINUS -> InputConstants.KEY_MINUS;
            case KEY_PERIOD -> InputConstants.KEY_PERIOD;
            case KEY_SLASH -> InputConstants.KEY_SLASH;
            case KEY_0 -> InputConstants.KEY_0;
            case KEY_1 -> InputConstants.KEY_1;
            case KEY_2 -> InputConstants.KEY_2;
            case KEY_3 -> InputConstants.KEY_3;
            case KEY_4 -> InputConstants.KEY_4;
            case KEY_5 -> InputConstants.KEY_5;
            case KEY_6 -> InputConstants.KEY_6;
            case KEY_7 -> InputConstants.KEY_7;
            case KEY_8 -> InputConstants.KEY_8;
            case KEY_9 -> InputConstants.KEY_9;
            case KEY_SEMICOLON -> InputConstants.KEY_SEMICOLON;
            case KEY_EQUAL -> InputConstants.KEY_EQUALS;
            case KEY_A -> InputConstants.KEY_A;
            case KEY_B -> InputConstants.KEY_B;
            case KEY_C -> InputConstants.KEY_C;
            case KEY_D -> InputConstants.KEY_D;
            case KEY_E -> InputConstants.KEY_E;
            case KEY_F -> InputConstants.KEY_F;
            case KEY_G -> InputConstants.KEY_G;
            case KEY_H -> InputConstants.KEY_H;
            case KEY_I -> InputConstants.KEY_I;
            case KEY_J -> InputConstants.KEY_J;
            case KEY_K -> InputConstants.KEY_K;
            case KEY_L -> InputConstants.KEY_L;
            case KEY_M -> InputConstants.KEY_M;
            case KEY_N -> InputConstants.KEY_N;
            case KEY_O -> InputConstants.KEY_O;
            case KEY_P -> InputConstants.KEY_P;
            case KEY_Q -> InputConstants.KEY_Q;
            case KEY_R -> InputConstants.KEY_R;
            case KEY_S -> InputConstants.KEY_S;
            case KEY_T -> InputConstants.KEY_T;
            case KEY_U -> InputConstants.KEY_U;
            case KEY_V -> InputConstants.KEY_V;
            case KEY_W -> InputConstants.KEY_W;
            case KEY_X -> InputConstants.KEY_X;
            case KEY_Y -> InputConstants.KEY_Y;
            case KEY_Z -> InputConstants.KEY_Z;
            case KEY_LEFT_BRACKET -> InputConstants.KEY_LBRACKET;
            case KEY_BACKSLASH -> InputConstants.KEY_BACKSLASH;
            case KEY_RIGHT_BRACKET -> InputConstants.KEY_RBRACKET;
            case KEY_GRAVE_ACCENT -> InputConstants.KEY_GRAVE;
            case KEY_ENTER -> InputConstants.KEY_RETURN;
            case KEY_TAB -> InputConstants.KEY_TAB;
            case KEY_BACKSPACE -> InputConstants.KEY_BACKSPACE;
            case KEY_INSERT -> InputConstants.KEY_INSERT;
            case KEY_DELETE -> InputConstants.KEY_DELETE;
            case KEY_RIGHT -> InputConstants.KEY_RIGHT;
            case KEY_LEFT -> InputConstants.KEY_LEFT;
            case KEY_DOWN -> InputConstants.KEY_DOWN;
            case KEY_UP -> InputConstants.KEY_UP;
            case KEY_PAGE_UP -> InputConstants.KEY_PAGEUP;
            case KEY_PAGE_DOWN -> InputConstants.KEY_PAGEDOWN;
            case KEY_HOME -> InputConstants.KEY_HOME;
            case KEY_END -> InputConstants.KEY_END;
            case KEY_CAPS_LOCK -> InputConstants.KEY_CAPSLOCK;
            case KEY_SCROLL_LOCK -> InputConstants.KEY_SCROLLLOCK;
            case KEY_NUM_LOCK -> InputConstants.KEY_NUMLOCK;
            case KEY_PRINT_SCREEN -> InputConstants.KEY_PRINTSCREEN;
            case KEY_PAUSE -> InputConstants.KEY_PAUSE;
            case KEY_F1 -> InputConstants.KEY_F1;
            case KEY_F2 -> InputConstants.KEY_F2;
            case KEY_F3 -> InputConstants.KEY_F3;
            case KEY_F4 -> InputConstants.KEY_F4;
            case KEY_F5 -> InputConstants.KEY_F5;
            case KEY_F6 -> InputConstants.KEY_F6;
            case KEY_F7 -> InputConstants.KEY_F7;
            case KEY_F8 -> InputConstants.KEY_F8;
            case KEY_F9 -> InputConstants.KEY_F9;
            case KEY_F10 -> InputConstants.KEY_F10;
            case KEY_F11 -> InputConstants.KEY_F11;
            case KEY_F12 -> InputConstants.KEY_F12;
            case KEY_KP_0 -> InputConstants.KEY_NUMPAD0;
            case KEY_KP_1 -> InputConstants.KEY_NUMPAD1;
            case KEY_KP_2 -> InputConstants.KEY_NUMPAD2;
            case KEY_KP_3 -> InputConstants.KEY_NUMPAD3;
            case KEY_KP_4 -> InputConstants.KEY_NUMPAD4;
            case KEY_KP_5 -> InputConstants.KEY_NUMPAD5;
            case KEY_KP_6 -> InputConstants.KEY_NUMPAD6;
            case KEY_KP_7 -> InputConstants.KEY_NUMPAD7;
            case KEY_KP_8 -> InputConstants.KEY_NUMPAD8;
            case KEY_KP_9 -> InputConstants.KEY_NUMPAD9;
            case KEY_KP_DECIMAL -> InputConstants.getKey("key.keyboard.keypad.period").getValue();
            case KEY_KP_DIVIDE -> InputConstants.getKey("key.keyboard.keypad.divide").getValue();
            case KEY_KP_MULTIPLY -> InputConstants.KEY_MULTIPLY;
            case KEY_KP_SUBTRACT -> InputConstants.getKey("key.keyboard.keypad.subtract").getValue();
            case KEY_KP_ADD -> InputConstants.KEY_ADD;
            case KEY_KP_ENTER -> InputConstants.KEY_NUMPADENTER;
            case KEY_LEFT_SHIFT -> InputConstants.KEY_LSHIFT;
            case KEY_LEFT_CONTROL -> InputConstants.KEY_LCONTROL;
            case KEY_LEFT_ALT -> InputConstants.KEY_LALT;
            case KEY_LEFT_SUPER -> InputConstants.KEY_LGUI;
            case KEY_RIGHT_SHIFT -> InputConstants.KEY_RSHIFT;
            case KEY_RIGHT_CONTROL -> InputConstants.KEY_RCONTROL;
            case KEY_RIGHT_ALT -> InputConstants.KEY_RALT;
            case KEY_RIGHT_SUPER -> InputConstants.KEY_RGUI;
        };
    }
}
