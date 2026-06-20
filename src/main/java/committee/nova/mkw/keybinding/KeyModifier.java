package committee.nova.mkw.keybinding;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public enum KeyModifier {
    CONTROL(GLFW.GLFW_KEY_LEFT_CONTROL, GLFW.GLFW_KEY_RIGHT_CONTROL),
    SHIFT(GLFW.GLFW_KEY_LEFT_SHIFT, GLFW.GLFW_KEY_RIGHT_SHIFT),
    ALT(GLFW.GLFW_KEY_LEFT_ALT, GLFW.GLFW_KEY_RIGHT_ALT),
    NONE(-1, -1);

    private final int leftKey;
    private final int rightKey;

    KeyModifier(int leftKey, int rightKey) {
        this.leftKey = leftKey;
        this.rightKey = rightKey;
    }

    public static KeyModifier getActiveModifier() {
        if (Screen.hasControlDown()) {
            return CONTROL;
        }
        if (Screen.hasShiftDown()) {
            return SHIFT;
        }
        if (Screen.hasAltDown()) {
            return ALT;
        }
        return NONE;
    }

    public static boolean isKeyCodeModifier(InputUtil.Key key) {
        return valueFromKey(key) != NONE;
    }

    public static KeyModifier valueFromKey(InputUtil.Key key) {
        int code = key.getCode();
        for (KeyModifier modifier : values()) {
            if (modifier.matchesCode(code)) {
                return modifier;
            }
        }
        return NONE;
    }

    public static KeyModifier valueFromString(String value) {
        for (KeyModifier modifier : values()) {
            if (modifier.name().equalsIgnoreCase(value)) {
                return modifier;
            }
        }
        return NONE;
    }

    public boolean matches(InputUtil.Key key) {
        return matchesCode(key.getCode());
    }

    public boolean isActive() {
        switch (this) {
            case CONTROL:
                return Screen.hasControlDown();
            case SHIFT:
                return Screen.hasShiftDown();
            case ALT:
                return Screen.hasAltDown();
            default:
                return true;
        }
    }

    public String getCombinedName(InputUtil.Key key) {
        if (this == NONE || matches(key)) {
            return key.getLocalizedText().getString();
        }
        return name() + " + " + key.getLocalizedText().getString();
    }

    private boolean matchesCode(int code) {
        return this != NONE && (code == leftKey || code == rightKey);
    }
}
