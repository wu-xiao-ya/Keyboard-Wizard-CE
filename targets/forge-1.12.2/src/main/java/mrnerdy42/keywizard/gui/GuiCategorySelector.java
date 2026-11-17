package mrnerdy42.keywizard.gui;

import java.io.IOException;
import java.util.ArrayList;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import org.lwjgl.input.Mouse;

/** Bounded silver dropdown: input and wheel events remain inside its popup. */
public class GuiCategorySelector extends GuiButton {
    private final GuiKeyWizard parent;
    private final ArrayList<String> categories;
    private boolean extended;
    private int selectedCategoryIdx;
    private int firstRow;
    private final int rowHeight = 16;
    public GuiCategorySelector(GuiKeyWizard parent, int x, int y, int width, ArrayList<String> categories) {
        super(0, x, y, width, 20, I18n.format(categories.get(0)));
        this.parent = parent;
        this.categories = categories;
        int previous = categories.indexOf(parent.getSelectedCategory());
        if (previous >= 0) {
            selectedCategoryIdx = previous;
        }
        displayString = I18n.format(getSelctedCategory());
    }
    private int rows() {
        return Math.max(1, Math.min(categories.size(), (parent.height - y - height - 10) / rowHeight));
    }
    @Override
    public void drawButton(Minecraft mc, int mx, int my, float ticks) {
        if (!visible) {
            return;
        }
        hovered = mx >= x && mx < x + width && my >= y && my < y + height;
        PixelWidgets.key(mc.fontRenderer, displayString + (extended ? " ^" : " v"), x, y, width, height,
                PixelKeyboardStyle.KeyState.UNASSIGNED, hovered, extended);
        if (!extended) {
            return;
        }
        int top = y + height;
        PixelWidgets.panel(x, top, width, rows() * rowHeight + 6);
        FontRenderer font = mc.fontRenderer;
        for (int row = 0; row < rows(); row++) {
            int index = firstRow + row;
            int rowY = top + 3 + row * rowHeight;
            boolean over = mx >= x + 3 && mx < x + width - 3 && my >= rowY && my < rowY + rowHeight;
            Gui.drawRect(x + 3, rowY, x + width - 3, rowY + rowHeight,
                    index == selectedCategoryIdx || over ? PixelKeyboardStyle.LIST_SELECTED : PixelKeyboardStyle.LIST_SURFACE);
            font.drawString(font.trimStringToWidth(I18n.format(categories.get(index)), width - 12), x + 6, rowY + 4, PixelKeyboardStyle.INK);
        }
        if (categories.size() > rows()) {
            int trackHeight = rows() * rowHeight;
            int thumb = Math.max(8, trackHeight * rows() / categories.size());
            int thumbY = top + 3 + firstRow * (trackHeight - thumb) / (categories.size() - rows());
            Gui.drawRect(x + width - 4, thumbY, x + width - 2, thumbY + thumb, PixelKeyboardStyle.PANEL_FOOT);
        }
    }
    public void handleMouseInput(int mx, int my) throws IOException {
        handleMouseInput(mx, my, Mouse.getEventDWheel());
    }
    public void handleMouseInput(int mx, int my, int scroll) throws IOException {
        if (extended && scroll != 0 && mx >= x && mx < x + width && my >= y + height && my < y + height + rows() * rowHeight + 6) {
            firstRow = Math.max(0, Math.min(categories.size() - rows(), firstRow + (scroll > 0 ? -3 : 3)));
        }
    }
    public boolean getExtended() {
        return extended;
    }
    public String getSelctedCategory() {
        return categories.get(selectedCategoryIdx);
    }
    public void mouseClicked(Minecraft mc, int mx, int my, int button) {
        if (button != 0) {
            return;
        }
        if (mx >= x && mx < x + width && my >= y && my < y + height) {
            playPressSound(mc.getSoundHandler());
            setState(!extended);
        } else if (extended) {
            int row = (my - y - height - 3) / rowHeight;
            if (mx >= x + 3 && mx < x + width - 3 && my >= y + height + 3 && row >= 0 && row < rows()) {
                selectedCategoryIdx = firstRow + row;
                displayString = I18n.format(getSelctedCategory());
                playPressSound(mc.getSoundHandler());
            }
            setState(false);
        }
    }
    public void setState(boolean value) {
        extended = value;
    }
}
