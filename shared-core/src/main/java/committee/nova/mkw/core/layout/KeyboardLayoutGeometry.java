package committee.nova.mkw.core.layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static committee.nova.mkw.core.layout.PhysicalKey.*;

public final class KeyboardLayoutGeometry {
    private KeyboardLayoutGeometry() {
    }

    private static final float KEY_SPACING = 5.0F;
    private static final float AUXILIARY_CLUSTER_GAP_UNITS = 4.0F;

    public static List<KeyPlacement> standard(float width, float height) {
        return standardWithKeyHeight(width, height / 6.0F - KEY_SPACING);
    }

    public static List<KeyPlacement> legacyStandard(float width, float height) {
        return standardWithKeyHeight(width, height / 15.0F);
    }

    private static List<KeyPlacement> standardWithKeyHeight(float width, float keyHeight) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float currentY = 0.0F;
        float keySpacing = KEY_SPACING;
        float keyWidth = width / 12.0F - keySpacing;

        addHorizontalRow(placements, new PhysicalKey[]{KEY_F1, KEY_F2, KEY_F3, KEY_F4, KEY_F5, KEY_F6, KEY_F7, KEY_F8, KEY_F9, KEY_F10, KEY_F11, KEY_F12}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 15.0F - keySpacing;
        float currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_GRAVE_ACCENT, KEY_1, KEY_2, KEY_3, KEY_4, KEY_5, KEY_6, KEY_7, KEY_8, KEY_9, KEY_0, KEY_MINUS, KEY_EQUAL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        currentX = addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_BACKSPACE);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_TAB);
        currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_Q, KEY_W, KEY_E, KEY_R, KEY_T, KEY_Y, KEY_U, KEY_I, KEY_O, KEY_P, KEY_LEFT_BRACKET, KEY_RIGHT_BRACKET}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, KEY_BACKSLASH);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_CAPS_LOCK);
        currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_A, KEY_S, KEY_D, KEY_F, KEY_G, KEY_H, KEY_J, KEY_K, KEY_L, KEY_SEMICOLON, KEY_APOSTROPHE}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_ENTER);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_LEFT_SHIFT);
        currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_Z, KEY_X, KEY_C, KEY_V, KEY_B, KEY_N, KEY_M, KEY_COMMA, KEY_PERIOD, KEY_SLASH}, currentX, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth * 3.0F + keySpacing * 2.0F, keyHeight, keySpacing, KEY_RIGHT_SHIFT);

        currentY += keyHeight + keySpacing;
        keyWidth = width / 7.0F - keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_LEFT_CONTROL, KEY_LEFT_SUPER, KEY_LEFT_ALT, KEY_SPACE, KEY_RIGHT_ALT, KEY_RIGHT_SUPER, KEY_RIGHT_CONTROL}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        return placements;
    }

    public static List<KeyPlacement> numpad(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float keySpacing = KEY_SPACING;
        float keyWidth = Math.max(34.0F, Math.min((width - keySpacing * 3.0F) / 4.0F, 75.0F));
        float keyHeight = Math.max(24.0F, (height - keySpacing * 4.0F) / 5.0F);
        float layoutWidth = keyWidth * 4.0F + keySpacing * 3.0F;
        float xOffset = Math.max(0.0F, (width - layoutWidth) / 2.0F);
        float currentY = 0.0F;

        addHorizontalRow(placements, new PhysicalKey[]{KEY_NUM_LOCK, KEY_KP_DIVIDE, KEY_KP_MULTIPLY, KEY_KP_SUBTRACT}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        float currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_7, KEY_KP_8, KEY_KP_9}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, KEY_KP_ADD);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_4, KEY_KP_5, KEY_KP_6}, xOffset, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_1, KEY_KP_2, KEY_KP_3}, xOffset, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth, keyHeight * 2.0F + keySpacing, keySpacing, KEY_KP_ENTER);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, xOffset, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_KP_0);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, KEY_KP_DECIMAL);
        return placements;
    }

    public static List<KeyPlacement> legacyNumpad(float width, float height) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float keySpacing = KEY_SPACING;
        float keyWidth = width / 4.0F - keySpacing;
        float keyHeight = height / 14.0F;
        float currentY = 0.0F;

        float currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_DIVIDE, KEY_KP_MULTIPLY, KEY_KP_SUBTRACT}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth / 3.0F, keyHeight * 2.0F + keySpacing, keySpacing, KEY_KP_ADD);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_7, KEY_KP_8, KEY_KP_9}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_4, KEY_KP_5, KEY_KP_6}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, currentX, currentY, keyWidth / 3.0F, keyHeight * 3.0F + keySpacing * 2.0F, keySpacing, KEY_KP_ENTER);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_KP_1, KEY_KP_2, KEY_KP_3}, 0.0F, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        currentX = addKey(placements, 0.0F, currentY, keyWidth * 2.0F + keySpacing, keyHeight, keySpacing, KEY_KP_0);
        addKey(placements, currentX, currentY, keyWidth, keyHeight, keySpacing, KEY_KP_DECIMAL);
        return placements;
    }

    public static List<KeyPlacement> auxiliary(float width, float height) {
        if (width <= 0.0F || height <= 0.0F) {
            return Collections.emptyList();
        }
        float horizontalGapUnits = 4.0F + AUXILIARY_CLUSTER_GAP_UNITS;
        float spacing = Math.min(KEY_SPACING, Math.min(width / (2.0F * horizontalGapUnits), height / 6.0F));
        float keyWidth = Math.min((width - spacing * horizontalGapUnits) / 6.0F, 95.0F);
        float keyHeight = (height - spacing * 3.0F) / 4.0F;
        return auxiliaryWithKeySize(width, keyWidth, keyHeight, spacing);
    }

    public static List<KeyPlacement> legacyAuxiliary(float width, float height) {
        return auxiliaryWithKeySize(width, width / 7.0F - KEY_SPACING, height / 14.0F, KEY_SPACING);
    }

    private static List<KeyPlacement> auxiliaryWithKeySize(float width, float keyWidth, float keyHeight, float keySpacing) {
        ArrayList<KeyPlacement> placements = new ArrayList<>();
        float clusterWidth = keyWidth * 3.0F + keySpacing * 2.0F;
        float clusterGap = keySpacing * AUXILIARY_CLUSTER_GAP_UNITS;
        float layoutWidth = clusterWidth * 2.0F + clusterGap;
        float xOffset = Math.max(0.0F, (width - layoutWidth) / 2.0F);
        float leftStart = xOffset;
        float arrowStart = xOffset + clusterWidth + clusterGap;
        float currentY = 0.0F;

        addHorizontalRow(placements, new PhysicalKey[]{KEY_PRINT_SCREEN, KEY_SCROLL_LOCK, KEY_PAUSE}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_INSERT, KEY_HOME, KEY_PAGE_UP}, leftStart, currentY, keyWidth, keyHeight, keySpacing);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_DELETE, KEY_END, KEY_PAGE_DOWN}, leftStart, currentY, keyWidth, keyHeight, keySpacing);
        addKey(placements, arrowStart + keyWidth + keySpacing, currentY, keyWidth, keyHeight, keySpacing, KEY_UP);

        currentY += keyHeight + keySpacing;
        addHorizontalRow(placements, new PhysicalKey[]{KEY_LEFT, KEY_DOWN, KEY_RIGHT}, arrowStart, currentY, keyWidth, keyHeight, keySpacing);
        return placements;
    }

    private static float addHorizontalRow(List<KeyPlacement> placements, PhysicalKey[] keys, float startX, float y, float width, float height, float spacing) {
        float currentX = startX;
        for (PhysicalKey key : keys) {
            currentX = addKey(placements, currentX, y, width, height, spacing, key);
        }
        return currentX;
    }

    private static float addKey(List<KeyPlacement> placements, float x, float y, float width, float height, float spacing, PhysicalKey key) {
        placements.add(new KeyPlacement(x, y, width, height, key));
        return x + width + spacing;
    }
}
