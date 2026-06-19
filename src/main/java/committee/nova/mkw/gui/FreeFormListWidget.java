package committee.nova.mkw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.mkw.util.DrawingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.list.AbstractSelectionList;

import java.util.Objects;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry> extends AbstractSelectionList<FreeFormListWidget<E>.Entry> {
    public boolean visible = true;

    public FreeFormListWidget(Minecraft client, int top, int left, int width, int height, int itemHeight) {
        super(client, width, height, top, top + height, itemHeight);
        this.setLeftPos(left);
        this.setRenderBackground(false);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.x0 + this.width - 5;
    }

    @Override
    public int getRowWidth() {
        return this.width;
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
    }

    @Override
    protected void renderList(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.enableScissor(this.x0, this.y0, this.width, this.height);
        for (int i = 0; i < this.getItemCount(); ++i) {
            if (this.isSelectedItem(i)) {
                DrawingUtil.drawNoFillRect(matrices, this.getRowLeft() - 2, this.getRowTop(i) - 2, this.getRowRight() - 8, this.getRowTop(i) + this.itemHeight - 4, 0xFFFFFFFF);
            }
            Entry entry = getEntry(i);
            entry.render(matrices, i, this.getRowTop(i), this.getRowLeft(), this.getRowWidth(), this.itemHeight - 4, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), entry), delta);
        }
        RenderSystem.disableScissor();
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            super.render(matrices, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) { return this.visible && super.mouseClicked(mouseX, mouseY, button); }
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) { return this.visible && super.mouseReleased(mouseX, mouseY, button); }
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) { return this.visible && super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY); }
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) { return this.visible && super.mouseScrolled(mouseX, mouseY, amount); }
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) { return this.visible && super.keyPressed(keyCode, scanCode, modifiers); }
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) { return this.visible && super.keyReleased(keyCode, scanCode, modifiers); }
    @Override
    public boolean charTyped(char chr, int modifiers) { return this.visible && super.charTyped(chr, modifiers); }
    @Override
    public boolean isMouseOver(double mouseX, double mouseY) { return this.visible && super.isMouseOver(mouseX, mouseY); }

    public abstract class Entry extends AbstractSelectionList.Entry<FreeFormListWidget<E>.Entry> {
        public abstract void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);
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
