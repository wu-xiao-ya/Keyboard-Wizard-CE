package committee.nova.mkw.gui;

import committee.nova.mkw.util.DrawingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;

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
        ctx.enableScissor(this.getX(), this.getY(), this.getX() + this.width, this.getListBottom());

        for (int i = 0; i < this.getEntryCount(); ++i) {
            Entry entry = this.children().get(i);
            int rowTop = this.getRowTop(i);
            entry.setX(this.getRowLeft());
            entry.setY(rowTop);
            entry.setWidth(this.getRowWidth());
            entry.setHeight(this.itemHeight);
            if (Objects.equals(this.getSelectedOrNull(), entry)) {
                DrawingUtil.drawNoFillRect(ctx, this.getRowLeft() - 2, rowTop - 2, this.getRowRight(), rowTop + this.itemHeight - 4, 0xFFFFFFFF);
            }
            entry.render(ctx, mouseX, mouseY, Objects.equals(this.getHoveredEntry(), entry), delta);
        }

        ctx.disableScissor();
    }

    @Override
    public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        if (this.visible) {
            this.renderPanelBackground(ctx);
            super.renderWidget(ctx, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClick) {
        return this.visible && super.mouseClicked(click, doubleClick);
    }

    @Override
    public boolean mouseReleased(Click click) {
        return this.visible && super.mouseReleased(click);
    }

    @Override
    public boolean mouseDragged(Click click, double deltaX, double deltaY) {
        return this.visible && super.mouseDragged(click, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return this.visible && super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        return this.visible && super.keyPressed(input);
    }

    @Override
    public boolean keyReleased(KeyInput input) {
        return this.visible && super.keyReleased(input);
    }

    @Override
    public boolean charTyped(CharInput input) {
        return this.visible && super.charTyped(input);
    }

    @Override
    public void setSelected(FreeFormListWidget<E>.Entry entry) {
        super.setSelected(entry);
    }

    public abstract class Entry extends EntryListWidget.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public final void render(DrawContext ctx, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            render(ctx, 0, this.getY(), this.getX(), this.getWidth(), this.getHeight(), mouseX, mouseY, hovered, tickDelta);
        }

        public abstract void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta);

        @Override
        public boolean mouseClicked(Click click, boolean doubleClick) {
            FreeFormListWidget.this.setSelected(this);
            return false;
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}