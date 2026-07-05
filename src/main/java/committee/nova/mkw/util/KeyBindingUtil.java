package committee.nova.mkw.util;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.bridge.binding.PlatformBindingBridge;
import committee.nova.mkw.core.binding.DynamicBindingCategories;
import net.minecraft.client.KeyMapping;
import net.neoforged.neoforge.client.settings.KeyModifier;

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
        return DynamicBindingCategories.withDynamicCategories(getCategories(), true);
    }

    public static Map<InputConstants.Key, Integer> getBindingCountsByKey() {
        return BRIDGE.getBindingCountsByKey();
    }

    public static InputConstants.Key getKey(KeyMapping keyMapping) {
        return BRIDGE.getKey(keyMapping);
    }

    public static KeyModifier getModifier(KeyMapping keyMapping) {
        return BRIDGE.getModifier(keyMapping);
    }

    public static boolean isUnbound(KeyMapping keyMapping) {
        return BRIDGE.isUnbound(keyMapping);
    }

    public static void setModifierAndKey(KeyMapping keyMapping, KeyModifier modifier, InputConstants.Key key) {
        BRIDGE.setModifierAndKey(keyMapping, modifier, key);
    }

    public static void resetToDefault(KeyMapping keyMapping) {
        BRIDGE.resetToDefault(keyMapping);
    }

    public static void refreshMappings() {
        BRIDGE.refreshMappings();
    }
}
