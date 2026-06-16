package committee.nova.mkw.util;

import committee.nova.mkw.mixin.AccessorKeyBinding;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
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

    /**
     * Get a list of all binding categories
     */
    @SuppressWarnings("resource")
    public static ArrayList<String> getCategories() {
        LinkedHashSet<String> categories = new LinkedHashSet<>();
        for (KeyMapping keyMapping : Minecraft.getInstance().options.keyMappings) {
            categories.add(keyMapping.getCategory());
        }
        return categories.stream().sorted().collect(Collectors.toCollection(ArrayList<String>::new));
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

    @SuppressWarnings("resource")
    public static Map<Key, Integer> getBindingCountsByKey() {
        HashMap<InputConstants.Key, Integer> map = new HashMap<>();
        for (KeyMapping b : Minecraft.getInstance().options.keyMappings) {
            map.merge(((AccessorKeyBinding) b).getBoundKey(), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }
}

