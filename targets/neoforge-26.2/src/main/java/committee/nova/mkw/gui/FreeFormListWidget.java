package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry>
        extends AbstractSelectionList<FreeFormListWidget<E>.Entry> {
    public FreeFormListWidget(
            Minecraft minecraft, int top, int left, int width, int height, int itemHeight) {
        super(minecraft, width, height, top, itemHeight);
        this.setX(left);
    }

    @Override
    protected int scrollBarX() {
        return this.getX() + this.width - 5;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    protected int getListBottom() {
        return this.getY() + this.height;
    }

    @Override
    protected void extractListBackground(GuiGraphicsExtractor graphics) {
        graphics.fill(getX(), getY(), getRight(), getListBottom(), PixelKeyboardStyle.BORDER);
        graphics.fill(
                getX() + 2,
                getY() + 2,
                getRight() - 2,
                getListBottom() - 2,
                PixelKeyboardStyle.LIST_SURFACE);
    }

    @Override
    protected void extractListSeparators(GuiGraphicsExtractor graphics) {
        graphics.fill(getX(), getY(), getRight(), getY() + 2, PixelKeyboardStyle.BORDER);
        graphics.fill(
                getX(),
                getListBottom() - 2,
                getRight(),
                getListBottom(),
                PixelKeyboardStyle.BORDER);
    }

    @Override
    protected void extractSelection(
            GuiGraphicsExtractor graphics, FreeFormListWidget<E>.Entry entry, int border) {
        graphics.fill(
                getX() + 2,
                entry.getY(),
                getRight() - 2,
                entry.getY() + entry.getHeight(),
                PixelKeyboardStyle.LIST_SELECTED);
    }

    @Override
    public void extractWidgetRenderState(
            GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            super.extractWidgetRenderState(graphics, mouseX, mouseY, partialTick);
        }
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
        public abstract void extractContent(
                GuiGraphicsExtractor graphics,
                int mouseX,
                int mouseY,
                boolean hovered,
                float tickDelta);

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == 0) {
                FreeFormListWidget.this.setSelected(this);
                return true;
            }
            return false;
        }
    }
}
