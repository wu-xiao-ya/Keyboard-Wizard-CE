package committee.nova.mkw.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.stream.Collectors;

public final class KeyBindingUtil {
    public static final String DYNAMIC_CATEGORY_ALL = "key.categories.keyboard_wizard_ce.all";
    public static final String DYNAMIC_CATEGORY_CONFLICTS = "key.categories.keyboard_wizard_ce.conflicts";
    public static final String DYNAMIC_CATEGORY_UNBOUND = "key.categories.keyboard_wizard_ce.unbound";
    public static final String DYNAMIC_CATEGORY_CTRL = "key.categories.keyboard_wizard_ce.ctrl";
    public static final String DYNAMIC_CATEGORY_ALT = "key.categories.keyboard_wizard_ce.alt";
    public static final String DYNAMIC_CATEGORY_SHIFT = "key.categories.keyboard_wizard_ce.shift";
    public static final String DYNAMIC_CATEGORY_NONE = "key.categories.keyboard_wizard_ce.no_modifier";

    private KeyBindingUtil() {
    }

    @SuppressWarnings("resource")
    public static ArrayList<String> getCategories() {
        LinkedHashSet<String> categories = new LinkedHashSet<>();
        for (KeyBinding keyBinding : Minecraft.getInstance().options.keyMappings) {
            categories.add(keyBinding.getCategory());
        }
        return categories.stream().sorted().collect(Collectors.toCollection(ArrayList::new));
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
    public static Map<InputMappings.Input, Integer> getBindingCountsByKey() {
        HashMap<InputMappings.Input, Integer> map = new HashMap<>();
        for (KeyBinding binding : Minecraft.getInstance().options.keyMappings) {
            map.merge(getKey(binding), 1, Integer::sum);
        }
        return Collections.unmodifiableMap(map);
    }

    public static InputMappings.Input getKey(KeyBinding keyBinding) {
        return keyBinding.getKey();
    }

    public static boolean isModifierActive(KeyModifier modifier, KeyBinding binding) {
        return binding.getKeyModifier() == modifier;
    }
}
