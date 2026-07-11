package committee.nova.mkw.core.layout;

import committee.nova.mkw.core.layout.KeyboardScreenLayout.Rect;

public final class KeyboardScreenLayoutCalculator {
    public static final int SCREEN_MARGIN = 10;
    public static final int BINDING_LIST_MIN_WIDTH = 92;
    public static final int BINDING_LIST_MAX_WIDTH = 160;
    public static final float BINDING_LIST_MAX_WIDTH_RATIO = 0.16F;
    public static final int CATEGORY_MIN_WIDTH = 88;
    public static final int CATEGORY_MAX_WIDTH = 130;
    public static final int LAYOUT_BUTTON_MIN_WIDTH = 74;
    public static final int LAYOUT_BUTTON_MAX_WIDTH = 96;
    public static final int KEYBOARD_MIN_HEIGHT = 132;
    public static final int KEYBOARD_MAX_HEIGHT = 180;
    public static final int CATEGORY_LIST_MAX_HEIGHT = 320;

    private static final int CONTENT_GAP = 15;
    private static final int TOP_Y = 5;
    private static final int CONTROL_HEIGHT = 20;
    private static final int CONTROL_GAP = 4;
    private static final int ROW_GAP = 6;
    private static final int MOUSE_SIDE_WIDTH = 25;
    private static final int MOUSE_KEY_WIDTH = 80;
    private static final int BOTTOM_Y_OFFSET = 23;

    private KeyboardScreenLayoutCalculator() {
    }

    public static KeyboardScreenLayout calculate(
            int screenWidth,
            int screenHeight,
            int maxBindingTextWidth,
            int maxCategoryTextWidth,
            int mainLabelWidth,
            int numpadLabelWidth,
            int auxiliaryLabelWidth
    ) {
        int contentReserve = 120;
        int bindingCap = Math.min(
                BINDING_LIST_MAX_WIDTH,
                Math.min(
                        (int) (screenWidth * BINDING_LIST_MAX_WIDTH_RATIO),
                        screenWidth - SCREEN_MARGIN * 2 - CONTENT_GAP - contentReserve
                )
        );
        bindingCap = Math.max(BINDING_LIST_MIN_WIDTH, bindingCap);
        int bindingWidth = clamp(maxBindingTextWidth + 20, BINDING_LIST_MIN_WIDTH, bindingCap);

        Rect bindingList = new Rect(
                SCREEN_MARGIN,
                SCREEN_MARGIN,
                bindingWidth,
                Math.max(0, screenHeight - 40)
        );
        Rect searchBar = new Rect(SCREEN_MARGIN, screenHeight - 20, bindingWidth, 14);

        int contentLeft = bindingWidth + CONTENT_GAP;
        int contentRight = screenWidth - SCREEN_MARGIN;
        FlowCursor flow = new FlowCursor(contentLeft, contentRight, TOP_Y);

        int categoryWidth = clamp(
                maxCategoryTextWidth + 20,
                CATEGORY_MIN_WIDTH,
                Math.max(CATEGORY_MIN_WIDTH, Math.min(CATEGORY_MAX_WIDTH, screenWidth / 4))
        );
        int layoutButtonWidth = clamp(
                Math.max(mainLabelWidth, Math.max(numpadLabelWidth, auxiliaryLabelWidth)) + 18,
                LAYOUT_BUTTON_MIN_WIDTH,
                LAYOUT_BUTTON_MAX_WIDTH
        );

        Rect category = flow.place(categoryWidth, CONTROL_HEIGHT);
        Rect main = flow.place(layoutButtonWidth, CONTROL_HEIGHT);
        Rect numpad = flow.place(layoutButtonWidth, CONTROL_HEIGHT);
        Rect auxiliary = flow.place(layoutButtonWidth, CONTROL_HEIGHT);

        int mouseGroupWidth = MOUSE_SIDE_WIDTH * 2 + MOUSE_KEY_WIDTH + CONTROL_GAP * 2;
        Rect mouseGroup = flow.place(mouseGroupWidth, CONTROL_HEIGHT);
        Rect mouseMinus = new Rect(mouseGroup.x(), mouseGroup.y(), MOUSE_SIDE_WIDTH, CONTROL_HEIGHT);
        Rect mouseKey = new Rect(mouseMinus.right() + CONTROL_GAP, mouseGroup.y(), MOUSE_KEY_WIDTH, CONTROL_HEIGHT);
        Rect mousePlus = new Rect(mouseKey.right() + CONTROL_GAP, mouseGroup.y(), MOUSE_SIDE_WIDTH, CONTROL_HEIGHT);

        int keyboardTop = flow.bottom() + 8;
        int keyboardBottom = screenHeight - BOTTOM_Y_OFFSET - 8;
        int availableKeyboardHeight = Math.max(0, keyboardBottom - keyboardTop);
        int keyboardHeight = Math.min(KEYBOARD_MAX_HEIGHT, availableKeyboardHeight);
        if (availableKeyboardHeight >= KEYBOARD_MIN_HEIGHT) {
            keyboardHeight = Math.max(KEYBOARD_MIN_HEIGHT, keyboardHeight);
        }
        int keyboardY = keyboardTop + Math.max(0, (availableKeyboardHeight - keyboardHeight) / 2);
        Rect keyboard = new Rect(
                contentLeft,
                keyboardY,
                Math.max(0, contentRight - contentLeft),
                keyboardHeight
        );

        int bottomY = screenHeight - BOTTOM_Y_OFFSET;
        Rect reset = new Rect(contentLeft, bottomY, 50, CONTROL_HEIGHT);
        Rect clear = new Rect(reset.right() + 1, bottomY, 50, CONTROL_HEIGHT);
        Rect resetAll = new Rect(clear.right() + 1, bottomY, 70, CONTROL_HEIGHT);
        Rect help = new Rect(screenWidth - 47, screenHeight - 22, 20, 20);
        Rect screenToggle = new Rect(screenWidth - 22, screenHeight - 22, 20, 20);

        return new KeyboardScreenLayout(
                bindingList,
                searchBar,
                category,
                main,
                numpad,
                auxiliary,
                mouseMinus,
                mouseKey,
                mousePlus,
                keyboard,
                reset,
                clear,
                resetAll,
                help,
                screenToggle
        );
    }

    public static int calculateCategoryListHeight(
            int itemCount,
            int itemHeight,
            int screenHeight,
            int categoryBottom
    ) {
        int naturalHeight = itemCount * itemHeight + 10;
        int availableHeight = Math.max(itemHeight + 4, screenHeight - categoryBottom - 34);
        return Math.max(
                itemHeight + 4,
                Math.min(CATEGORY_LIST_MAX_HEIGHT, Math.min(naturalHeight, availableHeight))
        );
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, Math.max(min, max)));
    }

    private static final class FlowCursor {
        private final int left;
        private final int right;
        private int x;
        private int y;
        private int bottom;

        private FlowCursor(int left, int right, int y) {
            this.left = left;
            this.right = Math.max(left, right);
            this.x = left;
            this.y = y;
            this.bottom = y;
        }

        private Rect place(int requestedWidth, int height) {
            int availableWidth = Math.max(0, this.right - this.left);
            int width = Math.min(requestedWidth, availableWidth);
            if (this.x > this.left && this.x + width > this.right) {
                this.x = this.left;
                this.y = this.bottom + ROW_GAP;
            }

            Rect result = new Rect(this.x, this.y, width, height);
            this.x = result.right() + CONTROL_GAP;
            this.bottom = Math.max(this.bottom, result.bottom());
            return result;
        }

        private int bottom() {
            return this.bottom;
        }
    }
}
