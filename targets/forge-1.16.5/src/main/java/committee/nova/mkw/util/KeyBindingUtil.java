package committee.nova.mkw.util;

import committee.nova.mkw.bridge.binding.PlatformBindingBridge;
import committee.nova.mkw.core.binding.DynamicBindingCategories;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.Map;

public final class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = DynamicBindingCategories.ALL;
    public static final String DYNAMIC_CATEGORY_CONFLICTS = DynamicBindingCategories.CONFLICTS;
    public static final String DYNAMIC_CATEGORY_UNBOUND = DynamicBindingCategories.UNBOUND;
    public static final String DYNAMIC_CATEGORY_CTRL = DynamicBindingCategories.CTRL;
    public static final String DYNAMIC_CATEGORY_ALT = DynamicBindingCategories.ALT;
    public static final String DYNAMIC_CATEGORY_SHIFT = DynamicBindingCategories.SHIFT;
    public static final String DYNAMIC_CATEGORY_NONE = DynamicBindingCategories.NONE;
    private static final PlatformBindingBridge BRIDGE = new PlatformBindingBridge();

    private KeyBindingUtil() {
    }

    public static KeyBinding[] getVisibleBindings() {
        return BRIDGE.getAllBindings().toArray(new KeyBinding[0]);
    }

    public static boolean isVisible(KeyBinding keyBinding) {
        return PlatformBindingBridge.isVisible(keyBinding);
    }

    public static ArrayList<String> getCategories() {
        return new ArrayList<>(BRIDGE.getCategories());
    }

    public static ArrayList<String> getCategoriesWithDynamics() {
        return DynamicBindingCategories.withDynamicCategories(getCategories(), true);
    }

    public static Map<InputMappings.Input, Integer> getBindingCountsByKey() {
        return BRIDGE.getBindingCountsByKey();
    }

    public static InputMappings.Input getKey(KeyBinding keyBinding) {
        return BRIDGE.getKey(keyBinding);
    }

    public static boolean isModifierActive(KeyModifier modifier, KeyBinding binding) {
        return BRIDGE.getModifier(binding) == modifier;
    }

    public static boolean isUnbound(KeyBinding binding) {
        return BRIDGE.isUnbound(binding);
    }

    public static void setModifierAndKey(KeyBinding binding, KeyModifier modifier, InputMappings.Input key) {
        BRIDGE.setModifierAndKey(binding, modifier, key);
    }

    public static void resetToDefault(KeyBinding binding) {
        BRIDGE.resetToDefault(binding);
    }

    public static void refreshMappings() {
        BRIDGE.refreshMappings();
    }
}
