package committee.nova.mkw.core.binding;

import java.util.ArrayList;
import java.util.List;

public final class DynamicBindingCategories {
    public static final String ALL = "key.categories.keyboard_wizard_ce.all";
    public static final String CONFLICTS = "key.categories.keyboard_wizard_ce.conflicts";
    public static final String UNBOUND = "key.categories.keyboard_wizard_ce.unbound";
    public static final String CTRL = "key.categories.keyboard_wizard_ce.ctrl";
    public static final String ALT = "key.categories.keyboard_wizard_ce.alt";
    public static final String SHIFT = "key.categories.keyboard_wizard_ce.shift";
    public static final String NONE = "key.categories.keyboard_wizard_ce.no_modifier";

    private DynamicBindingCategories() {
    }

    public static ArrayList<String> withDynamicCategories(List<String> categories, boolean includeConflicts) {
        ArrayList<String> result = new ArrayList<>(categories);
        result.add(0, UNBOUND);
        if (includeConflicts) {
            result.add(0, CONFLICTS);
        }
        result.add(0, ALL);
        result.add(CTRL);
        result.add(ALT);
        result.add(SHIFT);
        result.add(NONE);
        return result;
    }
}
