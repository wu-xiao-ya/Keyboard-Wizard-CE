package committee.nova.mkw.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import committee.nova.mkw.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;

import java.util.Objects;

public abstract class FreeFormListWidget<E extends FreeFormListWidget<E>.Entry> extends EntryListWidget<FreeFormListWidget<E>.Entry> {
    private static final int CONTENT_LEFT_PADDING = 4;
    private static final int SCROLLBAR_WIDTH = 6;
    private static final int SCROLLBAR_RIGHT_PADDING = 4;
    private static final int CONTENT_SCROLLBAR_GAP = 4;

    public boolean visible = true;

    public FreeFormListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.setX(left);
    }

    protected int getListBottom() {
        return this.getY() + this.height;
    }

    @Override
    public int getRowWidth() {
        return Math.max(0, this.getRowRight() - this.getRowLeft());
    }

    @Override
    public int getRowLeft() {
        return this.getX() + CONTENT_LEFT_PADDING;
    }

    @Override
    public int getRowRight() {
        return this.getScrollbarX() - CONTENT_SCROLLBAR_GAP;
    }

    @Override
    protected int getScrollbarX() {
        return this.getX() + this.width - SCROLLBAR_WIDTH - SCROLLBAR_RIGHT_PADDING;
    }

    protected void renderPanelBackground(DrawContext ctx) {
        ctx.fillGradient(this.getX(), this.getY(), this.getX() + this.width, this.getListBottom(), -1072689136, -804253680);
    }

    @Override
    protected void renderList(DrawContext ctx, int mouseX, int mouseY, float delta) {
        double scaleH = this.client.getWindow().getHeight() / (double) this.client.getWindow().getScaledHeight();
        double scaleW = this.client.getWindow().getWidth() / (double) this.client.getWindow().getScaledWidth();
        RenderSystem.enableScissor((int) (this.getX() * scaleW), (int) (this.client.getWindow().getHeight() - (this.getListBottom() * scaleH)), (int) (this.width * scaleW), (int) (this.height * scaleH));

        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (this.isSelectedEntry(i)) {
                DrawingUtil.drawNoFillRect(ctx, this.getRowLeft() - 2, this.getRowTop(i) - 2, this.getRowRight(), this.getRowTop(i) + this.itemHeight - 4, 0xFFFFFFFF);
            }

            Entry entry = getEntry(i);
            entry.render(ctx, i, this.getRowTop(i), this.getRowLeft(), this.getRowWidth(), this.itemHeight - 4, mouseX, mouseY, this.isMouseOver(mouseX, mouseY) && Objects.equals(this.getEntryAtPosition(mouseX, mouseY), entry), delta);
        }
        RenderSystem.disableScissor();
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            this.renderPanelBackground(ctx);
            super.renderWidget(ctx, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean isFocused() {
        return true;
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
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return this.visible && super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
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

    public abstract class Entry extends EntryListWidget.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public abstract void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                FreeFormListWidget.this.setSelected(this);
                return true;
            }

            return false;
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
