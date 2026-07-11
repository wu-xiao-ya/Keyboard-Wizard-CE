package committee.nova.mkw.core.layout;

import committee.nova.mkw.core.layout.KeyboardScreenLayout.Rect;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyboardScreenLayoutCalculatorTest {
    @Test
    void laysOutWideAndNarrowScreensWithoutOverlap() {
        assertLayout(1024, 576);
        assertLayout(640, 360);
        assertLayout(480, 320);
    }

    @Test
    void categoryListUsesNaturalAndAvailableHeight() {
        assertTrue(KeyboardScreenLayoutCalculator.calculateCategoryListHeight(4, 16, 360, 25) < 320);
        assertTrue(KeyboardScreenLayoutCalculator.calculateCategoryListHeight(100, 16, 360, 25) <= 320);
    }

    private static void assertLayout(int width, int height) {
        KeyboardScreenLayout layout = KeyboardScreenLayoutCalculator.calculate(
                width,
                height,
                260,
                180,
                48,
                56,
                56
        );

        List<Rect> topControls = List.of(
                layout.categorySelector(),
                layout.mainLayoutButton(),
                layout.numpadLayoutButton(),
                layout.auxiliaryLayoutButton(),
                layout.mouseMinusButton(),
                layout.mouseKey(),
                layout.mousePlusButton()
        );

        for (Rect control : topControls) {
            assertInside(control, width, height);
            assertFalse(control.overlaps(layout.bindingList()));
        }
        for (int i = 0; i < topControls.size(); i++) {
            for (int j = i + 1; j < topControls.size(); j++) {
                assertFalse(topControls.get(i).overlaps(topControls.get(j)));
            }
        }

        assertInside(layout.keyboard(), width, height);
        assertFalse(layout.keyboard().overlaps(layout.bindingList()));
        assertFalse(layout.keyboard().overlaps(layout.resetButton()));
        assertFalse(layout.helpButton().overlaps(layout.screenToggleButton()));
    }

    private static void assertInside(Rect rect, int width, int height) {
        assertTrue(rect.x() >= 0);
        assertTrue(rect.y() >= 0);
        assertTrue(rect.right() <= width);
        assertTrue(rect.bottom() <= height);
    }
}
