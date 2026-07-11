package committee.nova.mkw.gui;

import committee.nova.mkw.bridge.binding.CategoryDisplayResolver;
import committee.nova.mkw.core.layout.KeyboardScreenLayout;
import committee.nova.mkw.core.layout.KeyboardScreenLayoutCalculator;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.text.Text;

public class CategorySelectorWidget extends PressableWidget implements TickableElement {

    public KeyWizardScreen keyWizardScreen;
    public boolean extended = false;

    public BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, KeyboardScreenLayout.Rect layoutRect, int screenHeight) {
        super(layoutRect.x(), layoutRect.y(), layoutRect.width(), layoutRect.height(), Text.empty());
        this.keyWizardScreen = keyWizardScreen;
        MinecraftClient client = MinecraftClient.getInstance();
        int listItemHeight = client.textRenderer.fontHeight + 7;
        int listHeight = KeyboardScreenLayoutCalculator.calculateCategoryListHeight(
                KeyBindingUtil.getCategoriesWithDynamics().size(),
                listItemHeight,
                screenHeight,
                layoutRect.bottom()
        );
        this.categoryList = new BindingCategoryListWidget(client, layoutRect.bottom(), layoutRect.x(), layoutRect.width(), listHeight, listItemHeight);
        this.categoryList.visible = false;
        this.setMessage(CategoryDisplayResolver.resolve(this.getSelectedCategory()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean listClicked = this.categoryList.mouseClicked(mouseX, mouseY, button);
        boolean thisClicked = super.mouseClicked(mouseX, mouseY, button);
        if (listClicked && button == 0) {
            this.extended = false;
        } else if (!(listClicked || thisClicked)) {
            this.extended = false;
        }
        return listClicked || thisClicked;
    }

    @Override
    public void onPress() {
        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
        this.extended = !this.extended;
    }

    @Override
    protected void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderWidget(ctx, mouseX, mouseY, delta);
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Text label = trimToWidth(this.getMessage(), this.getWidth() - 8);
        int textX = this.getX() + (this.getWidth() - textRenderer.getWidth(label)) / 2;
        int textY = this.getY() + (this.getHeight() - textRenderer.fontHeight) / 2;
        ctx.drawTextWithShadow(textRenderer, label, textX, textY, this.active ? 0xFFFFFFFF : 0xFFA0A0A0);
    }

    @Override
    public void tick() {
        this.setMessage(CategoryDisplayResolver.resolve(this.getSelectedCategory()));
        this.categoryList.visible = this.extended;
    }

    public String getSelectedCategory() {
        if (this.categoryList.getSelectedOrNull() == null) {
            return KeyBindingUtil.DYNAMIC_CATEGORY_ALL;
        }
        return ((BindingCategoryListWidget.CategoryEntry) this.categoryList.getSelectedOrNull()).category;
    }

    public BindingCategoryListWidget getCategoryList() {
        return this.categoryList;
    }

    private static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {

        public BindingCategoryListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
            super(client, top, left, width, height, itemHeight);

            for (String category : KeyBindingUtil.getCategoriesWithDynamics()) {
                this.addEntry(new CategoryEntry(category));
            }
            if (!this.children().isEmpty()) {
                this.setSelected(this.children().get(0));
            }
        }

        public class CategoryEntry extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry>.Entry {
            private final String category;

            public CategoryEntry(String category) {
                this.category = category;
            }

            @Override
            public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                ctx.drawTextWithShadow(client.textRenderer, trimToWidth(CategoryDisplayResolver.resolve(this.category), entryWidth - 6), x + 3, y + 2, 0xFFFFFFFF);
            }
        }

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }

    private static Text trimToWidth(Text text, int width) {
        if (width <= 0) {
            return Text.empty();
        }
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        return Text.literal(textRenderer.trimToWidth(text.getString(), width));
    }
}
