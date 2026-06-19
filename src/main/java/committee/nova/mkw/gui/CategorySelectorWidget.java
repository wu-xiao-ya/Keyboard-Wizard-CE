package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class CategorySelectorWidget extends Button implements TickableElement {
    public boolean extended = false;

    private final KeyWizardScreen keyWizardScreen;
    private final BindingCategoryListWidget categoryList;

    public CategorySelectorWidget(KeyWizardScreen keyWizardScreen, int x, int y, int width, int height) {
        super(x, y, width, height, StringTextComponent.EMPTY, button -> { });
        this.keyWizardScreen = keyWizardScreen;
        Minecraft client = Minecraft.getInstance();
        int listItemHeight = client.font.lineHeight + 7;
        int listHeight = KeyBindingUtil.getCategoriesWithDynamics().size() * listItemHeight + 10;
        int listBottom = this.y + this.getHeight() + listHeight;
        if (listBottom > this.keyWizardScreen.height) {
            listHeight = this.keyWizardScreen.height - this.y - this.getHeight() - 10;
        }
        this.categoryList = new BindingCategoryListWidget(client, this.y + this.getHeight(), this.x, this.width, listHeight, listItemHeight);
        this.categoryList.visible = false;
        this.setMessage(new TranslationTextComponent(this.getSelectedCategory()));
    }

    @Override
    public void onPress() {
        this.extended = !this.extended;
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.categoryList.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void tick() {
        this.setMessage(new TranslationTextComponent(this.getSelectedCategory()));
        this.categoryList.visible = this.extended;
    }

    public String getSelectedCategory() {
        if (this.categoryList.getSelected() == null) {
            return KeyBindingUtil.DYNAMIC_CATEGORY_ALL;
        }
        return this.categoryList.getSelected().category;
    }

    public BindingCategoryListWidget getCategoryList() {
        return this.categoryList;
    }

    public static class BindingCategoryListWidget extends FreeFormListWidget<BindingCategoryListWidget.CategoryEntry> {
        public BindingCategoryListWidget(Minecraft client, int top, int left, int width, int height, int itemHeight) {
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
            public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
                minecraft.font.draw(matrixStack, new TranslationTextComponent(this.category), x + 3, y + 2, 0xFFFFFFFF);
            }
        }
    }
}
