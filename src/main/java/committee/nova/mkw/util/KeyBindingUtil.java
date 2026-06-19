package committee.nova.mkw.util;

import committee.nova.mkb.ModernKeyBinding;
import committee.nova.mkw.mixin.AccessorKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.InputUtil.Key;

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

    /**
     * Get a list of all binding categories
     */
    public static ArrayList<String> getCategories() {
        return AccessorKeyBinding.getKeyCategories().stream().sorted().collect(Collectors.toCollection(ArrayList<String>::new));
    }

    public static ArrayList<String> getCategoriesWithDynamics() {
        ArrayList<String> categories = getCategories();
        categories.add(0, DYNAMIC_CATEGORY_UNBOUND);
        if (!ModernKeyBinding.nonConflictKeys()) categories.add(0, DYNAMIC_CATEGORY_CONFLICTS);
        categories.add(0, DYNAMIC_CATEGORY_ALL);
        categories.add(DYNAMIC_CATEGORY_CTRL);
        categories.add(DYNAMIC_CATEGORY_ALT);
        categories.add(DYNAMIC_CATEGORY_SHIFT);
        categories.add(DYNAMIC_CATEGORY_NONE);
        return categories;
    }

    @SuppressWarnings("resource")
    public static Map<Key, Integer> getBindingCountsByKey() {
        HashMap<InputUtil.Key, Integer> map = new HashMap<>();
        for (KeyBinding b : MinecraftClient.getInstance().options.allKeys) {
            map.merge(((AccessorKeyBinding) b).getBoundKey(), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }
}
