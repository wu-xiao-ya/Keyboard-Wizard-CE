package mrnerdy42.keywizard.util;

import java.util.List;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;
import mrnerdy42.keywizard.gui.GuiKeyWizard;
import mrnerdy42.keywizard.gui.GuiKeyboard;

/** Builds the original Forge 1.12.2 layouts from shared geometry. */
public final class KeyboardFactory {
    private KeyboardFactory() {
    }

    public static GuiKeyboard makeKeyboard(KeyboardLayout layout, GuiKeyWizard parent, double x, double y, double width, double height) {
        GuiKeyboard keyboard = new GuiKeyboard(parent, x, y);
        List<KeyPlacement> placements;
        switch (layout) {
            case QWERTY:
                placements = KeyboardLayoutGeometry.standard((float) width, (float) height);
                break;
            case NUMPAD:
                placements = KeyboardLayoutGeometry.numpad((float) width, (float) height);
                break;
            case AUXILIARY:
                placements = KeyboardLayoutGeometry.auxiliary((float) width, (float) height);
                break;
            default:
                return null;
        }
        float maxX = 1;
        float maxY = 1;
        for (KeyPlacement placement : placements) {
            maxX = Math.max(maxX, placement.x() + placement.width());
            maxY = Math.max(maxY, placement.y() + placement.height());
        }
        float scale = Math.min(1F, Math.min((float) width / maxX, (float) height / maxY));
        float offsetX = ((float) width - maxX * scale) / 2F;
        for (KeyPlacement placement : placements) {
            keyboard.addKey(offsetX + placement.x() * scale, placement.y() * scale,
                    placement.width() * scale, placement.height() * scale, Lwjgl2Keys.code(placement.key()));
        }
        return keyboard;
    }
}
