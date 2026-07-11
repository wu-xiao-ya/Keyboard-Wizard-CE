package committee.nova.mkw.core.binding;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DynamicBindingCategoriesTest {
    @Test
    void addsDynamicCategoriesInStableOrder() {
        List<String> categories = DynamicBindingCategories.withDynamicCategories(
                List.of("minecraft:movement", "minecraft:misc"),
                true
        );

        assertEquals(DynamicBindingCategories.ALL, categories.get(0));
        assertEquals(DynamicBindingCategories.CONFLICTS, categories.get(1));
        assertEquals(DynamicBindingCategories.UNBOUND, categories.get(2));
        assertEquals(DynamicBindingCategories.CTRL, categories.get(categories.size() - 4));
        assertEquals(DynamicBindingCategories.NONE, categories.get(categories.size() - 1));
    }

    @Test
    void omitsConflictCategoryWhenDisabled() {
        List<String> categories = DynamicBindingCategories.withDynamicCategories(List.of("movement"), false);

        assertFalse(categories.contains(DynamicBindingCategories.CONFLICTS));
    }
}
