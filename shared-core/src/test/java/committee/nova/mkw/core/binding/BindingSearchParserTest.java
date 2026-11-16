package committee.nova.mkw.core.binding;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BindingSearchParserTest {
    @Test
    void parsesEmptyInput() {
        BindingSearchQuery query = BindingSearchParser.parse(null, "#key#");

        assertFalse(query.keyFilter());
        assertNull(query.keyNameFilter());
        assertEquals(Collections.emptyList(), query.textTerms());
    }

    @Test
    void parsesKeyFilterAndTextTerms() {
        BindingSearchQuery query = BindingSearchParser.parse("#key# <Left Shift> movement sprint", "#key#");

        assertTrue(query.keyFilter());
        assertEquals("Left Shift", query.keyNameFilter());
        assertEquals(Arrays.asList("movement", "sprint"), query.textTerms());
    }

    @Test
    void matchesEveryWordCaseInsensitivelyAndKeepsKeyNamesIntact() {
        BindingSearchQuery query = BindingSearchParser.parse("<Keypad Enter> MOVE  forward", "#key#");

        assertFalse(query.keyFilter());
        assertEquals("Keypad Enter", query.keyNameFilter());
        assertTrue(query.matchesName("Move Forward"));
        assertFalse(query.matchesName("Move Backward"));
    }

    @Test
    void preservesLegacyKeyQueryAndCategoryScope() {
        BindingSearchQuery typed = BindingSearchParser.parseLegacy("@W  walk forward", "#key#");
        BindingSearchQuery clicked = BindingSearchParser.parseLegacy("#key#<NUMPADENTER>", "#key#");

        assertFalse(typed.keyFilter());
        assertEquals("W", typed.keyNameFilter());
        assertTrue(typed.matchesName("Walk Forward"));
        assertTrue(clicked.keyFilter());
        assertEquals("NUMPADENTER", clicked.keyNameFilter());
        assertFalse(clicked.hasTextTerms());
        assertEquals("Key<A>", BindingSearchParser.parseLegacy("#key#<Key<A>>", "#key#").keyNameFilter());
    }
}
