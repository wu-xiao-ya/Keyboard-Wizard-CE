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
