package committee.nova.mkw.core.binding;

import org.junit.jupiter.api.Test;

import java.util.List;

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
        assertEquals(List.of(), query.textTerms());
    }

    @Test
    void parsesKeyFilterAndTextTerms() {
        BindingSearchQuery query = BindingSearchParser.parse("#key# <Left Shift> movement sprint", "#key#");

        assertTrue(query.keyFilter());
        assertEquals("Left Shift", query.keyNameFilter());
        assertEquals(List.of("movement", "sprint"), query.textTerms());
    }
}
