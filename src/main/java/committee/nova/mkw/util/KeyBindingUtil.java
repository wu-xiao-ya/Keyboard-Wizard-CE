package committee.nova.mkw.util;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.mixin.AccessorKeyMapping;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.extensions.IKeyMappingExtension;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = "key.categories.mkw.all";
    public static final String DYNAMIC_CATEGORY_CONFLICTS = "key.categories.mkw.conflicts";
    public static final String DYNAMIC_CATEGORY_UNBOUND = "key.categories.mkw.unbound";
    public static final String DYNAMIC_CATEGORY_CTRL = "key.categories.mkw.ctrl";
    public static final String DYNAMIC_CATEGORY_ALT = "key.categories.mkw.alt";
    public static final String DYNAMIC_CATEGORY_SHIFT = "key.categories.mkw.shift";
    public static final String DYNAMIC_CATEGORY_NONE = "key.categories.mkw.no_modifier";

    public static ArrayList<String> getCategories() {
        return AccessorKeyMapping.mkw$getCategorySortOrder().stream()
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
        return ((AccessorKeyMapping) keyMapping).mkw$getKey();
    }

    public static KeyModifier getModifier(KeyMapping keyMapping) {
        return ((IKeyMappingExtension) keyMapping).getKeyModifier();
    }

    public static void setModifierAndKey(KeyMapping keyMapping, KeyModifier modifier, InputConstants.Key key) {
        ((IKeyMappingExtension) keyMapping).setKeyModifierAndCode(modifier, key);
    }

    public static void resetToDefault(KeyMapping keyMapping) {
        ((IKeyMappingExtension) keyMapping).setToDefault();
    }
}
