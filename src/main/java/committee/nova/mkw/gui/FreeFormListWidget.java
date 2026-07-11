package committee.nova.mkw.gui;

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

    private final int panelLeft;
    private final int panelTop;
    private final int panelBottom;

    public boolean visible = true;

    public FreeFormListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
        super(client, width, height, top, itemHeight);
        this.panelLeft = left;
        this.panelTop = top;
        this.panelBottom = top + height;
    }

    protected int getListBottom() {
        return this.panelBottom;
    }

    @Override
    public int getRowWidth() {
        return Math.max(0, this.getRowRight() - this.getRowLeft());
    }

    @Override
    public int getRowLeft() {
        return this.panelLeft + CONTENT_LEFT_PADDING;
    }

    @Override
    public int getRowRight() {
        return this.getScrollbarPositionX() - CONTENT_SCROLLBAR_GAP;
    }

    @Override
    protected int getScrollbarPositionX() {
        return this.panelLeft + this.width - SCROLLBAR_WIDTH - SCROLLBAR_RIGHT_PADDING;
    }

    protected void renderPanelBackground(DrawContext ctx) {
        ctx.fillGradient(this.panelLeft, this.panelTop, this.panelLeft + this.width, this.panelBottom, -1072689136, -804253680);
    }

    @Override
    protected void renderList(DrawContext ctx, int mouseX, int mouseY, float delta) {
        ctx.enableScissor(this.panelLeft, this.panelTop, this.panelLeft + this.width, this.panelBottom);

        for (int i = 0; i < this.getEntryCount(); ++i) {
            if (this.isSelectedEntry(i)) {
                DrawingUtil.drawNoFillRect(ctx, this.getRowLeft() - 2, this.getRowTop(i) - 2, this.getRowRight(), this.getRowTop(i) + this.itemHeight - 4, 0xFFFFFFFF);
            }

            Entry entry = getEntry(i);
            entry.render(ctx, i, getRowTop(i), getRowLeft(), getRowWidth(), this.itemHeight, mouseX, mouseY, Objects.equals(this.getHoveredEntry(), entry), delta);
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
    public void setSelected(FreeFormListWidget<E>.Entry entry) {
        super.setSelected(entry);
    }

    public abstract class Entry extends EntryListWidget.Entry<FreeFormListWidget<E>.Entry> {
        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button != 0) {
                return false;
            }
            FreeFormListWidget.this.setSelected(this);
            return true;
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
