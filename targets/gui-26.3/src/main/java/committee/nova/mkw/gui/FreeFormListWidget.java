package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry> extends AbstractSelectionList<FreeFormListWidget<E>.Entry> {
    public FreeFormListWidget(Minecraft minecraft, int top, int left, int width, int height, int itemHeight) {
        super(minecraft, width, height, top, itemHeight);
        this.setX(left);
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - 8;
    }

    @Override
    public int getRowWidth() {
        return this.width - 16;
    }

    protected int getListBottom() {
        return this.getY() + this.height;
    }

    @Override
    protected void extractListBackground(GuiGraphicsExtractor graphics) {
        graphics.fill(this.getX(), this.getY(), this.getRight(), this.getListBottom(), PixelKeyboardStyle.BORDER);
        graphics.fill(this.getX() + 2, this.getY() + 2, this.getRight() - 2, this.getListBottom() - 2,
                PixelKeyboardStyle.LIST_SURFACE);
    }

    @Override
    protected void extractListSeparators(GuiGraphicsExtractor graphics) {
        graphics.fill(this.getX(), this.getY(), this.getRight(), this.getY() + 2, PixelKeyboardStyle.BORDER);
        graphics.fill(this.getX(), this.getListBottom() - 2, this.getRight(), this.getListBottom(), PixelKeyboardStyle.BORDER);
    }

    @Override
    protected void extractSelection(GuiGraphicsExtractor graphics, FreeFormListWidget<E>.Entry entry, int border) {
        graphics.fill(this.getX() + 2, entry.getY(), this.getRight() - 2,
                entry.getY() + entry.getHeight(), PixelKeyboardStyle.LIST_SELECTED);
    }

    @Override
    protected void extractItem(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, FreeFormListWidget<E>.Entry entry) {
        super.extractItem(graphics, mouseX, mouseY, partialTick, entry);
        int bottom = entry.getY() + entry.getHeight();
        graphics.fill(this.getX() + 4, bottom - 1, this.getRight() - 8, bottom, PixelKeyboardStyle.PANEL_FOOT);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        return this.visible && super.mouseClicked(event, doubleClick);
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        return this.visible && super.mouseReleased(event);
    }

    @Override
    public boolean mouseDragged(MouseButtonEvent event, double deltaX, double deltaY) {
        return this.visible && super.mouseDragged(event, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return this.visible && super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        return this.visible && super.keyPressed(event);
    }

    @Override
    public boolean keyReleased(KeyEvent event) {
        return this.visible && super.keyReleased(event);
    }

    @Override
    public boolean charTyped(CharacterEvent event) {
        return this.visible && super.charTyped(event);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && super.isMouseOver(mouseX, mouseY);
    }

    public abstract class Entry extends AbstractSelectionList.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public abstract void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta);

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
                FreeFormListWidget.this.setSelected(this);
                return true;
            }
            return false;
        }
    }
}
