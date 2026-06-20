package committee.nova.mkw.util;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.keybinding.KeyModifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = "key.categories.keyboard_wizard_ce.all";
    public static final String DYNAMIC_CATEGORY_CONFLICTS = "key.categories.keyboard_wizard_ce.conflicts";
    public static final String DYNAMIC_CATEGORY_UNBOUND = "key.categories.keyboard_wizard_ce.unbound";
    public static final String DYNAMIC_CATEGORY_CTRL = "key.categories.keyboard_wizard_ce.ctrl";
    public static final String DYNAMIC_CATEGORY_ALT = "key.categories.keyboard_wizard_ce.alt";
    public static final String DYNAMIC_CATEGORY_SHIFT = "key.categories.keyboard_wizard_ce.shift";
    public static final String DYNAMIC_CATEGORY_NONE = "key.categories.keyboard_wizard_ce.no_modifier";

    public static ArrayList<String> getCategories() {
        return java.util.Arrays.stream(Minecraft.getInstance().options.keyMappings)
                .map(KeyMapping::getCategory)
                .distinct()
                .map(category -> category.label().getString())
                .sorted()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<String> getCategoriesWithDynamics() {
        ArrayList<String> categories = getCategories();
        categories.add(0, DYNAMIC_CATEGORY_UNBOUND);
        categories.add(0, DYNAMIC_CATEGORY_CONFLICTS);
        categories.add(0, DYNAMIC_CATEGORY_ALL);
        categories.add(DYNAMIC_CATEGORY_CTRL);
        categories.add(DYNAMIC_CATEGORY_ALT);
        categories.add(DYNAMIC_CATEGORY_SHIFT);
        categories.add(DYNAMIC_CATEGORY_NONE);
        return categories;
    }

    public static Map<InputConstants.Key, Integer> getBindingCountsByKey() {
        HashMap<InputConstants.Key, Integer> map = new HashMap<>();
        for (KeyMapping b : Minecraft.getInstance().options.keyMappings) {
            map.merge(getKey(b), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }

    public static InputConstants.Key getKey(KeyMapping keyMapping) {
        return keyMapping.getKey();
    }

    public static KeyModifier getModifier(KeyMapping keyMapping) {
        return KeyModifier.NONE;
    }

    public static void setModifierAndKey(KeyMapping keyMapping, KeyModifier modifier, InputConstants.Key key) {
        keyMapping.setKey(key);
    }

    public static void resetToDefault(KeyMapping keyMapping) {
        keyMapping.setToDefault();
    }
}
