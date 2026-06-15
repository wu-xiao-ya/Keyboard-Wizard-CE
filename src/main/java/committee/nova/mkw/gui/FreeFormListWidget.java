package committee.nova.mkw.gui;

import committee.nova.mkw.util.DrawingUtil;
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
        return this.getX() + this.width - 5;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    protected int getListBottom() {
        return this.getY() + this.height;
    }

    protected void renderPanelBackground(GuiGraphics graphics) {
        graphics.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getListBottom(), -1072689136, -804253680);
    }

    @Override
    protected void renderItem(GuiGraphics graphics, int mouseX, int mouseY, float partialTick, int index, int x, int y, int entryWidth, int entryHeight) {
        if (this.isSelectedItem(index)) {
            DrawingUtil.drawNoFillRect(graphics, this.getRowLeft() - 2, y - 2, this.getRowRight() - 8, y + this.itemHeight - 4, 0xFFFFFFFF);
        }
        super.renderItem(graphics, mouseX, mouseY, partialTick, index, x, y, entryWidth, entryHeight);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        if (this.visible) {
            this.renderPanelBackground(graphics);
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
