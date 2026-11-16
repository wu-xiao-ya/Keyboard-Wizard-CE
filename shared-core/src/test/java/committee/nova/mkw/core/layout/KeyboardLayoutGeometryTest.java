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

    @Test
    void keepsTheEnterAndModifierKeysDistinct() {
        List<KeyPlacement> main = KeyboardLayoutGeometry.standard(900, 180);
        List<KeyPlacement> numpad = KeyboardLayoutGeometry.numpad(900, 180);

        assertTrue(main.stream().anyMatch(placement -> placement.key() == PhysicalKey.KEY_ENTER));
        assertTrue(main.stream().anyMatch(placement -> placement.key() == PhysicalKey.KEY_LEFT_SHIFT));
        assertTrue(main.stream().anyMatch(placement -> placement.key() == PhysicalKey.KEY_RIGHT_SHIFT));
        assertTrue(numpad.stream().anyMatch(placement -> placement.key() == PhysicalKey.KEY_KP_ENTER));
        assertTrue(numpad.stream().noneMatch(placement -> placement.key() == PhysicalKey.KEY_ENTER));
    }

    @Test
    void preservesLegacyKeyboardRowsAndNumpadDimensions() {
        List<KeyPlacement> main = KeyboardLayoutGeometry.legacyStandard(900, 180);
        List<KeyPlacement> numpad = KeyboardLayoutGeometry.legacyNumpad(900, 180);

        assertEquals(72, main.size());
        assertEquals(16, numpad.size());
        assertEquals(13, KeyboardLayoutGeometry.legacyAuxiliary(900, 180).size());
        assertEquals(120.0F, placement(main, PhysicalKey.KEY_Q).x(), 0.01F);
        assertEquals(12.0F, placement(main, PhysicalKey.KEY_ENTER).height(), 0.01F);
        assertEquals(220.0F / 3.0F, placement(numpad, PhysicalKey.KEY_KP_ENTER).width(), 0.01F);
        assertPlacementsInside(main, 900, 180);
        assertPlacementsInside(numpad, 900, 180);
    }

    private static KeyPlacement placement(List<KeyPlacement> placements, PhysicalKey key) {
        return placements.stream().filter(placement -> placement.key() == key).findFirst().get();
    }
}
