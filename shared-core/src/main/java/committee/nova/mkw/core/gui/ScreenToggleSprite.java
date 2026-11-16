package committee.nova.mkw.core.gui;

/** Sprite used on both screens to switch between Controls and Keyboard Wizard CE. */
public final class ScreenToggleSprite {
    public static final String TEXTURE_PATH = "textures/gui/screen_toggle_widgets.png";
    public static final int TEXTURE_WIDTH = 40;
    public static final int TEXTURE_HEIGHT = 40;
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static final int U = 0;
    public static final int V = 0;
    public static final int HOVER_V_OFFSET = HEIGHT;

    private ScreenToggleSprite() {
    }

    public static int textureV(boolean highlighted) {
        return V + (highlighted ? HOVER_V_OFFSET : 0);
    }
}
