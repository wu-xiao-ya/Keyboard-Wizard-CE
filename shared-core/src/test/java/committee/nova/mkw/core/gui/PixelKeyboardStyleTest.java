package committee.nova.mkw.core.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PixelKeyboardStyleTest {
    @Test
    void highlightPreservesTheColorOfEveryKeyState() {
        for (PixelKeyboardStyle.KeyState state : PixelKeyboardStyle.KeyState.values()) {
            int[][] pixels = new int[16][18];
            PixelKeyboardStyle.drawKeycap(
                    (left, top, right, bottom, color) -> fill(pixels, left, top, right, bottom, color),
                    0, 0, 18, 16, state, true);

            assertEquals(PixelKeyboardStyle.HOVER_BORDER, pixels[0][0]);
            assertEquals(state.bevel, pixels[2][2]);
            assertEquals(state.face, pixels[6][8]);
            assertEquals(state.foot, pixels[13][8]);
        }
    }

    @Test
    void smallCapsNeverDrawOutsideTheirBounds() {
        for (int width = 1; width <= 18; width++) {
            for (int height = 1; height <= 18; height++) {
                int[][] pixels = new int[height][width];
                PixelKeyboardStyle.drawKeycap(
                        (left, top, right, bottom, color) -> fill(pixels, left, top, right, bottom, color),
                        0, 0, width, height, PixelKeyboardStyle.KeyState.UNASSIGNED, false);
                assertEquals(PixelKeyboardStyle.BORDER, pixels[height - 1][width - 1]);
            }
        }
    }

    @Test
    void keyboardIconKeepsBothAccentsAtFooterAndEntrySizes() {
        for (int size : new int[] {20, 24, 34}) {
            int[][] pixels = new int[size][size];
            PixelKeyboardStyle.drawKeyboardIcon(
                    (left, top, right, bottom, color) -> {
                        assertTrue(left >= 3 && top >= 3 && right <= size - 3 && bottom <= size - 3);
                        fill(pixels, left, top, right, bottom, color);
                    }, 0, 0, size, size);
            assertTrue(contains(pixels, PixelKeyboardStyle.KeyState.ASSIGNED.bevel));
            assertTrue(contains(pixels, PixelKeyboardStyle.KeyState.CONFLICT.bevel));
        }
    }

    private static boolean contains(int[][] pixels, int color) {
        for (int[] row : pixels) {
            for (int pixel : row) {
                if (pixel == color) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void fill(int[][] pixels, int left, int top, int right, int bottom, int color) {
        assertTrue(left >= 0 && top >= 0 && right <= pixels[0].length && bottom <= pixels.length);
        assertTrue(left < right && top < bottom);
        for (int y = top; y < bottom; y++) {
            for (int x = left; x < right; x++) {
                pixels[y][x] = color;
            }
        }
    }
}
