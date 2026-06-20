package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class CategorySelectorWidget extends AbstractButton implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    public boolean extended = false;

    public BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
        super(x, y, width, height, Component.empty());
        this.keyWizardScreen = keyWizardScreen;
        Minecraft minecraft = Minecraft.getInstance();
        int listItemHeight = minecraft.font.lineHeight + 7;
        int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
        int listBottom = this.getY() + this.height + listHeight;
        if (listBottom > this.keyWizardScreen.height) {
            listHeight = this.keyWizardScreen.height - this.getY() - this.height - 10;
        }
        this.categoryList = new BindingCategoryListWidget(minecraft, this.getY() + this.height, this.getX(), this.width, listHeight, listItemHeight);
        this.categoryList.visible = this.extended;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean listClicked = this.categoryList.mouseClicked(event, doubleClick);
        boolean thisClicked = super.mouseClicked(event, doubleClick);
        if (!(listClicked || thisClicked)) {
            this.extended = false;
        }
        return listClicked || thisClicked;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
    }

    @Override
    public void onPress(InputWithModifiers input) {
        this.extended = !this.extended;
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        this.extractDefaultSprite(graphics);
        this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
        this.categoryList.extractRenderState(graphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void tick() {
        this.setMessage(Component.translatable(this.getSelectedCategory()));
        this.categoryList.visible = this.extended;
    }

    public String getSelectedCategory() {
        if (this.categoryList.getSelected() == null) {
            return KeyBindingUtil.DYNAMIC_CATEGORY_ALL;
        }
        return ((BindingCategoryListWidget.CategoryEntry) this.categoryList.getSelected()).category;
    }

    public BindingCategoryListWidget getCategoryList() {
        return this.categoryList;
    }

    public static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {
        public BindingCategoryListWidget(Minecraft minecraft, int top, int left, int width, int height, int itemHeight) {
            super(minecraft, top, left, width, height, itemHeight);

            for (String c : KeyBindingUtil.getCategoriesWithDynamics()) {
                this.addEntry(new CategoryEntry(c));
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
            public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                graphics.text(minecraft.font, Component.translatable(this.category), this.getContentX() + 1, this.getContentY(), 0xFFFFFFFF);
            }
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }
    }
}
