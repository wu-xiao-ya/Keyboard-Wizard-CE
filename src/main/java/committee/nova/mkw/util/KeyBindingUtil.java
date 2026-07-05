package committee.nova.mkw.util;

import committee.nova.mkw.ModernKeyBinding;
import committee.nova.mkw.api.IKeyBinding;
import committee.nova.mkw.bridge.binding.PlatformBindingBridge;
import committee.nova.mkw.core.binding.DynamicBindingCategories;
import committee.nova.mkw.keybinding.KeyModifier;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

import java.util.ArrayList;
import java.util.Map;

public class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = DynamicBindingCategories.ALL;
    public static final String DYNAMIC_CATEGORY_CONFLICTS = DynamicBindingCategories.CONFLICTS;
    public static final String DYNAMIC_CATEGORY_UNBOUND = DynamicBindingCategories.UNBOUND;
    public static final String DYNAMIC_CATEGORY_CTRL = DynamicBindingCategories.CTRL;
    public static final String DYNAMIC_CATEGORY_ALT = DynamicBindingCategories.ALT;
    public static final String DYNAMIC_CATEGORY_SHIFT = DynamicBindingCategories.SHIFT;
    public static final String DYNAMIC_CATEGORY_NONE = DynamicBindingCategories.NONE;
    private static final PlatformBindingBridge BRIDGE = new PlatformBindingBridge();

    public static ArrayList<String> getCategories() {
        return new ArrayList<>(BRIDGE.getCategories());
    }

    public static ArrayList<String> getCategoriesWithDynamics() {
        return DynamicBindingCategories.withDynamicCategories(getCategories(), !ModernKeyBinding.nonConflictKeys());
    }

    public static Map<InputUtil.Key, Integer> getBindingCountsByKey() {
        return BRIDGE.getBindingCountsByKey();
    }

    public static InputUtil.Key getKey(KeyBinding keyBinding) {
        return BRIDGE.getKey(keyBinding);
    }

    public static KeyModifier getModifier(KeyBinding keyBinding) {
        return BRIDGE.getModifier(keyBinding);
    }

    public static boolean isUnbound(KeyBinding keyBinding) {
        return BRIDGE.isUnbound(keyBinding);
    }

    public static void setModifierAndKey(KeyBinding keyBinding, KeyModifier modifier, InputUtil.Key key) {
        BRIDGE.setModifierAndKey(keyBinding, modifier, key);
    }

    public static void resetToDefault(KeyBinding keyBinding) {
        BRIDGE.resetToDefault(keyBinding);
    }

    public static void refreshMappings() {
        BRIDGE.refreshMappings();
    }
}

