package committee.nova.mkw.core.gui;

/** The palette and beveled geometry shared by clients using the pixel keyboard theme. */
public final class PixelKeyboardStyle {
    public static final int BORDER = 0xFF111111;
    public static final int HOVER_BORDER = 0xFF424242;
    public static final int PANEL = 0xFFC6C6C6;
    public static final int PANEL_HIGHLIGHT = 0xFFFFFFFF;
    public static final int PANEL_FOOT = 0xFF555555;
    public static final int INK = 0xFF202020;
    public static final int LIST_SURFACE = 0xFFEDEDED;
    public static final int LIST_SELECTED = 0xFFFFFFFF;

    private PixelKeyboardStyle() {
    }

    public enum KeyState {
        UNASSIGNED(PANEL, PANEL_HIGHLIGHT, PANEL_FOOT, 0xFF252525),
        ASSIGNED(0xFF17B86D, 0xFF32D850, 0xFF079B6B, 0xFF082A1B),
        CONFLICT(0xFFD73E42, 0xFFF07665, 0xFFAA212B, 0xFF1B0809);

        public final int face;
        public final int bevel;
        public final int foot;
        public final int ink;

        KeyState(int face, int bevel, int foot, int ink) {
            this.face = face;
            this.bevel = bevel;
            this.foot = foot;
            this.ink = ink;
        }
    }

    @FunctionalInterface
    public interface RectangleRenderer {
        void fill(int left, int top, int right, int bottom, int argb);
    }

    public static void drawKeycap(RectangleRenderer draw, int x, int y, int width, int height,
                                  KeyState state, boolean highlighted) {
        int edge = Math.min(width, height) >= 16 ? 2 : 1;
        int depth = height >= 16 ? 3 : 2;
        drawBevel(draw, x, y, width, height, edge, depth, highlighted ? HOVER_BORDER : BORDER,
                state.bevel, state.face, state.foot);
    }

    public static void drawPanel(RectangleRenderer draw, int x, int y, int width, int height) {
        drawBevel(draw, x, y, width, height, 2, 4, BORDER, PANEL_HIGHLIGHT, PANEL, PANEL_FOOT);
    }

    /** Draws the navigation glyph centered in the button, with a three-pixel margin. */
    public static void drawKeyboardIcon(RectangleRenderer draw, int x, int y, int width, int height) {
        float scale = Math.min(1.0F, Math.min((width - 6) / 16.0F, (height - 6) / 10.0F));
        if (scale <= 0.0F) {
            return;
        }
        int left = x + (width - Math.round(16 * scale)) / 2;
        int top = y + (height - Math.round(10 * scale)) / 2;
        RectangleRenderer icon = (x0, y0, x1, y1, color) -> {
            int scaledLeft = Math.round(x0 * scale);
            int scaledTop = Math.round(y0 * scale);
            int scaledRight = Math.round(x1 * scale);
            int scaledBottom = Math.round(y1 * scale);
            if (scaledLeft < scaledRight && scaledTop < scaledBottom) {
                draw.fill(left + scaledLeft, top + scaledTop, left + scaledRight, top + scaledBottom, color);
            }
        };

        icon.fill(1, 0, 15, 10, BORDER);
        icon.fill(0, 1, 16, 9, BORDER);
        icon.fill(1, 1, 15, 2, PANEL_FOOT);
        for (int column = 0; column < 4; column++) {
            int keyX = 2 + 3 * column;
            icon.fill(keyX, 2, keyX + 2, 4, PANEL_HIGHLIGHT);
            icon.fill(keyX + 1, 5, keyX + 3, 6, PANEL);
        }
        icon.fill(2, 7, 4, 8, KeyState.ASSIGNED.bevel);
        icon.fill(5, 7, 11, 8, PANEL_HIGHLIGHT);
        icon.fill(12, 7, 14, 8, KeyState.CONFLICT.bevel);
        icon.fill(2, 8, 14, 9, PANEL_FOOT);
    }

    private static void drawBevel(RectangleRenderer draw, int x, int y, int width, int height,
                                   int edge, int depth, int border, int bevel, int face, int foot) {
        if (width <= 0 || height <= 0) {
            return;
        }
        int right = x + width;
        int bottom = y + height;
        draw.fill(x, y, right, bottom, border);
        if (width <= 2 * edge || height <= 2 * edge) {
            return;
        }
        int footTop = Math.max(y + edge, bottom - edge - depth);
        draw.fill(x + edge, y + edge, right - edge, bottom - edge, bevel);
        draw.fill(x + edge, footTop, right - edge, bottom - edge, foot);
        if (width > 4 * edge && footTop > y + 3 * edge) {
            draw.fill(x + 2 * edge, y + 2 * edge, right - 2 * edge, footTop - edge, face);
        }
    }
}
