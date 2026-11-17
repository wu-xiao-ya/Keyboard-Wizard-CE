package committee.nova.mkw.keybinding;

import net.minecraft.client.MinecraftClient;
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
        if (CONTROL.isActive()) {
            return CONTROL;
        }
        if (SHIFT.isActive()) {
            return SHIFT;
        }
        if (ALT.isActive()) {
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
        return this == NONE || isKeyPressed(this.leftKey) || isKeyPressed(this.rightKey);
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

    private static boolean isKeyPressed(int code) {
        MinecraftClient client = MinecraftClient.getInstance();
        return code >= 0 && client != null && client.getWindow() != null && InputUtil.isKeyPressed(client.getWindow(), code);
    }
}