package committee.nova.mkw.core.layout;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardLayoutGeometryTest {
    @Test
    void standardKeyboardStaysInsideBounds() {
        assertPlacementsInside(KeyboardLayoutGeometry.standard(900, 180), 900, 180);
        assertEquals(72, KeyboardLayoutGeometry.standard(900, 180).size());
    }

    @Test
    void alternateLayoutsStayInsideBounds() {
        assertPlacementsInside(KeyboardLayoutGeometry.numpad(900, 180), 900, 180);
        assertPlacementsInside(KeyboardLayoutGeometry.auxiliary(900, 180), 900, 180);
        assertEquals(17, KeyboardLayoutGeometry.numpad(900, 180).size());
        assertEquals(13, KeyboardLayoutGeometry.auxiliary(900, 180).size());
    }

    private static void assertPlacementsInside(List<KeyPlacement> placements, float width, float height) {
        for (KeyPlacement placement : placements) {
            assertTrue(placement.width() > 0);
            assertTrue(placement.height() > 0);
            assertTrue(placement.x() >= 0);
            assertTrue(placement.y() >= 0);
            assertTrue(placement.x() + placement.width() <= width + 0.01F);
            assertTrue(placement.y() + placement.height() <= height + 0.01F);
        }
    }
}
