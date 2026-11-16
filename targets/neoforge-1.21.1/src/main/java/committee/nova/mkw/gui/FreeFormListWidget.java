package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry> extends AbstractSelectionList<FreeFormListWidget<E>.Entry> {
    public boolean visible = true;

    public FreeFormListWidget(Minecraft minecraft, int top, int left, int width, int height, int itemHeight) {
        super(minecraft, width, height, top, itemHeight);
        this.setX(left);
    }

    @Override
    protected int getScrollbarPosition() {
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
    protected void renderListBackground(GuiGraphics graphics) {
        graphics.fill(this.getX(), this.getY(), this.getRight(), this.getListBottom(), PixelKeyboardStyle.BORDER);
        graphics.fill(this.getX() + 2, this.getY() + 2, this.getRight() - 2, this.getListBottom() - 2, PixelKeyboardStyle.LIST_SURFACE);
    }

    @Override
    protected void renderListSeparators(GuiGraphics graphics) {
        graphics.fill(this.getX(), this.getY(), this.getRight(), this.getY() + 2, PixelKeyboardStyle.BORDER);
        graphics.fill(this.getX(), this.getListBottom() - 2, this.getRight(), this.getListBottom(), PixelKeyboardStyle.BORDER);
    }

    @Override
    protected void renderSelection(GuiGraphics graphics, int y, int entryWidth, int entryHeight, int border, int fill) {
        graphics.fill(this.getX() + 2, y - 2, this.getRight() - 2, y + entryHeight + 2, PixelKeyboardStyle.LIST_SELECTED);
    }

    @Override
    protected void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int index, int x, int y, int entryWidth, int entryHeight) {
        super.renderItem(graphics, mouseX, mouseY, partialTick, index, x, y, entryWidth, entryHeight);
        graphics.fill(this.getX() + 4, y + entryHeight + 1, this.getRight() - 8, y + entryHeight + 2, PixelKeyboardStyle.PANEL_FOOT);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            super.renderWidget(graphics, mouseX, mouseY, partialTick);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.visible && super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.visible && super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.visible && super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        return this.visible && super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.visible && super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.visible && super.keyReleased(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.visible && super.charTyped(chr, modifiers);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.visible && super.isMouseOver(mouseX, mouseY);
    }

    public abstract class Entry extends AbstractSelectionList.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public abstract void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                FreeFormListWidget.this.setSelected(this);
                return true;
            }
            return false;
        }
    }
}
