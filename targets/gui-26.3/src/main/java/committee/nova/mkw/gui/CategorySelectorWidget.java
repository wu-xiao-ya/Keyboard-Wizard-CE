package committee.nova.mkw.gui;

import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.List;

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
        this.tick();
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        boolean listClicked = this.categoryList.mouseClicked(event, doubleClick);
        boolean thisClicked = super.mouseClicked(event, doubleClick);
        if (!(listClicked || thisClicked)) {
            setExtended(false);
        }
        return listClicked || thisClicked;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
        output.add(NarratedElementType.TITLE, this.getMessage());
    }

    @Override
    public void onPress(InputWithModifiers input) {
        setExtended(!this.extended);
    }

    @Override
    protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        PixelWidgets.drawButton(graphics, this.getMessage(), this.getX(), this.getY(), this.getWidth(),
                this.getHeight(), KeyState.UNASSIGNED, this.isHoveredOrFocused(), this.extended);
    }

    private void setExtended(boolean extended) {
        this.extended = extended;
        this.categoryList.visible = extended;
    }

    List<Component> tooltipLines() {
        if (this.extended) return this.categoryList.tooltipLines();
        return this.isHovered() && Minecraft.getInstance().font.width(this.getMessage()) > PixelWidgets.labelWidth(this.getWidth())
                ? List.of(this.getMessage()) : List.of();
    }

    void selectCategory(String category) {
        for (var entry : this.categoryList.children()) {
            if (entry instanceof BindingCategoryListWidget.CategoryEntry candidate && candidate.category.equals(category)) {
                this.categoryList.setSelected(candidate);
                this.tick();
                return;
            }
        }
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
                graphics.text(minecraft.font, PixelWidgets.fit(minecraft.font, this.label(), this.getContentWidth() - 1),
                        this.getContentX() + 1, this.getContentY(), PixelKeyboardStyle.INK, false);
            }

            private Component label() {
                return Component.translatable(this.category);
            }
        }

        private List<Component> tooltipLines() {
            if (this.getHovered() instanceof CategoryEntry entry && this.minecraft.font.width(entry.label()) > entry.getContentWidth() - 1) {
                return List.of(entry.label());
            }
            return List.of();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
        }
    }
}
