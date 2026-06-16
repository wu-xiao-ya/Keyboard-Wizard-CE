package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;

public class CategorySelectorWidget extends AbstractButton implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    public boolean extended = false;

    public BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
        super(x, y, width, height, Component.literal(""));
        this.keyWizardScreen = keyWizardScreen;
        Minecraft c = Minecraft.getInstance();
        int listItemHeight = c.font.lineHeight + 7;
        int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
        int listBottom = this.getY() + this.height + listHeight;
        if (listBottom > this.keyWizardScreen.height) {
            listHeight = this.keyWizardScreen.height - this.getY() - this.height - 10;
        }
        this.categoryList = new BindingCategoryListWidget(c, this.getY() + this.height, this.getX(), this.width, listHeight, listItemHeight);
        this.categoryList.visible = false;
        this.setMessage(Component.translatable(this.getSelectedCategory()));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean listClicked = this.categoryList.mouseClicked(mouseX, mouseY, button);
        boolean thisClicked = super.mouseClicked(mouseX, mouseY, button);
        if (!(listClicked || thisClicked)) {
            this.extended = false;
        }
        return listClicked || thisClicked;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput builder) {

    }

    @Override
    public void onPress() {
        this.playDownSound(Minecraft.getInstance().getSoundManager());
        this.extended = !this.extended;
    }

    @Override
    public void render(GuiGraphics ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        this.categoryList.render(ctx, mouseX, mouseY, delta);
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

    private static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {

        public BindingCategoryListWidget(Minecraft client, int top, int left, int width, int height, int itemHeight) {
            super(client, top, left, width, height, itemHeight);

            for (String c : KeyBindingUtil.getCategoriesWithDynamics()) {
                this.addEntry(new CategoryEntry(c));
            }
            this.setSelected(this.children().get(0));
        }

        public class CategoryEntry extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry>.Entry {
            private final String category;

            public CategoryEntry(String category) {
                this.category = category;
            }

            @Override
            public void render(GuiGraphics ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                ctx.drawString(minecraft.font, Component.translatable(this.category), x + 3, y + 2, 0xFFFFFFFF);
            }

        }
    }

}

