package mrnerdy42.keywizard.util;

import java.util.List;
import committee.nova.mkw.core.layout.KeyPlacement;
import committee.nova.mkw.core.layout.KeyboardLayoutGeometry;
import committee.nova.mkw.core.layout.PhysicalKey;
import mrnerdy42.keywizard.gui.GuiKeyWizard;
import mrnerdy42.keywizard.gui.GuiKeyboard;

/** Builds the original Forge 1.12.2 layouts from shared geometry. */
public final class KeyboardFactory {
    private KeyboardFactory() { }
    public static GuiKeyboard makeKeyboard(KeyboardLayout layout, GuiKeyWizard parent, double x, double y, double width, double height) {
        GuiKeyboard keyboard = new GuiKeyboard(parent, x, y);
        List<KeyPlacement> placements;
        switch (layout) {
            case QWERTY: placements = KeyboardLayoutGeometry.legacyStandard((float) width, (float) height); break;
            case NUMPAD: placements = KeyboardLayoutGeometry.legacyNumpad((float) width, (float) height); break;
            case AUXILIARY: placements = KeyboardLayoutGeometry.legacyAuxiliary((float) width, (float) height); break;
            default: return null;
        }
        for (KeyPlacement placement : placements) keyboard.addKey(placement.x(), placement.y(), placement.width(), placement.height(), Lwjgl2Keys.code(placement.key()));
        return keyboard;
    }
}
