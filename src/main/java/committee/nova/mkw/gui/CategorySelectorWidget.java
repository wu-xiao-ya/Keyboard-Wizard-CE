package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.input.AbstractInput;
import net.minecraft.text.Text;

public class CategorySelectorWidget extends PressableWidget implements TickableElement {

    public KeyWizardScreen keyWizardScreen;
    public boolean extended = false;

    public BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
        this.keyWizardScreen = keyWizardScreen;
        MinecraftClient client = MinecraftClient.getInstance();
        int listItemHeight = client.textRenderer.fontHeight + 7;
        int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
        int listBottom = this.getY() + this.getHeight() + listHeight;
        if (listBottom > this.keyWizardScreen.height) {
            listHeight = this.keyWizardScreen.height - this.getY() - this.getHeight() - 10;
        }
        this.categoryList = new BindingCategoryListWidget(client, this.getY() + this.getHeight(), this.getX(), this.getWidth(), listHeight, listItemHeight);
        this.categoryList.visible = false;
        this.setMessage(categoryText(this.getSelectedCategory()));
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubleClick) {
        boolean listClicked = this.categoryList.mouseClicked(click, doubleClick);
        boolean thisClicked = super.mouseClicked(click, doubleClick);
        if (!(listClicked || thisClicked)) {
            this.extended = false;
        }
        return listClicked || thisClicked;
    }

    @Override
    public void onPress(AbstractInput input) {
        this.playDownSound(MinecraftClient.getInstance().getSoundManager());
        this.extended = !this.extended;
    }

    @Override
    protected void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.renderWidget(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        this.setMessage(categoryText(this.getSelectedCategory()));
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

    private static Text categoryText(String category) {
        return category.startsWith("key.category.") ? Text.translatable(category) : Text.literal(category);
    }

    private static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {

        public BindingCategoryListWidget(MinecraftClient client, int top, int left, int width, int height, int itemHeight) {
            super(client, top, left, width, height, itemHeight);

            for (String category : KeyBindingUtil.getCategoriesWithDynamics()) {
                this.addEntry(new CategoryEntry(category));
            }
            this.setSelected(this.children().get(0));
        }

        public class CategoryEntry extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry>.Entry {
            private final String category;

            public CategoryEntry(String category) {
                this.category = category;
            }

            @Override
            public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                ctx.drawTextWithShadow(client.textRenderer, categoryText(this.category), x + 3, y + 2, 0xFFFFFFFF);
            }
        }

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}