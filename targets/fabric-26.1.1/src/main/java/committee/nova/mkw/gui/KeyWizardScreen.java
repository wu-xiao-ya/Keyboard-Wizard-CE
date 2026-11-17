package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.util.KeyBindingUtil;
import committee.nova.mkw.util.MinecraftCompat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;

import java.util.List;
import org.lwjgl.glfw.GLFW;

public class KeyWizardScreen extends OptionsSubScreen {
    private static final int KEYBOARD_HEIGHT = 180;
    private static final int SEARCH_MAX_LENGTH = 256;
    private static final int TOGGLE_MARGIN = 3;
    private static final int CATEGORY_SELECTOR_HORIZONTAL_PADDING = 32;
    private static final int CATEGORY_SELECTOR_MIN_WIDTH = 110;
    private static final int CATEGORY_SELECTOR_MAX_WIDTH = 170;
    private static final int BINDING_LIST_MIN_WIDTH = 105;
    private static final float BINDING_LIST_MAX_WIDTH_RATIO = 0.20F;
    private static final int SCREEN_MARGIN = 8;
    private static final int TOP_CONTROL_Y = 7;
    private static final int TOP_CONTROL_HEIGHT = 20;
    private static final int TOP_CONTROL_GAP = 4;
    private static final int TOP_ROW_GAP = 6;
    private static final int LAYOUT_BUTTON_MIN_WIDTH = 74;
    private static final int MOUSE_BUTTON_WIDTH = 80;
    private static final int MOUSE_BUTTON_HEIGHT = 20;
    private static final int MOUSE_SIDE_BUTTON_WIDTH = 25;
    private static final List<Component> HELP_TOOLTIP = List.of(
            Component.translatable("gui.keyboard_wizard_ce.help.title"),
            Component.translatable("gui.keyboard_wizard_ce.help.select"),
            Component.translatable("gui.keyboard_wizard_ce.help.middle_click"),
            Component.translatable("gui.keyboard_wizard_ce.help.colors"),
            Component.translatable("gui.keyboard_wizard_ce.help.search")
    );

    private final int[] mouseCodes = {
            GLFW.GLFW_MOUSE_BUTTON_1, GLFW.GLFW_MOUSE_BUTTON_2, GLFW.GLFW_MOUSE_BUTTON_3,
            GLFW.GLFW_MOUSE_BUTTON_4, GLFW.GLFW_MOUSE_BUTTON_5, GLFW.GLFW_MOUSE_BUTTON_6,
            GLFW.GLFW_MOUSE_BUTTON_7, GLFW.GLFW_MOUSE_BUTTON_8
    };
    private int mouseCodeIndex = 0;

    private KeyboardWidget keyboard;
    private KeyboardWidget mouseButton;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private EditBox searchBar;
    private KeyboardLayout keyboardLayout = KeyboardLayout.MAIN;
    private float keyboardAnchorX;
    private float keyboardAnchorY;
    private float keyboardWidth;
    private float keyboardHeight = KEYBOARD_HEIGHT;
    private Button mainLayoutButton;
    private Button numpadLayoutButton;
    private Button auxiliaryLayoutButton;
    public static final String KEY_FILTER_PREFIX = "#key#";

    public KeyWizardScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("screen.keyboard_wizard_ce.title"));
    }

    @Override
    protected void init() {
        int maxBindingNameWidth = 0;
        if (this.minecraft == null) return;
        for (KeyMapping k : this.minecraft.options.keyMappings) {
            int w = this.font.width(Component.translatable(k.getName()));
            if (w > maxBindingNameWidth) maxBindingNameWidth = w;
        }

        int maxCategoryWidth = 0;
        for (String s : KeyBindingUtil.getCategories()) {
            int w = this.font.width(Component.translatable(s));
            if (w > maxCategoryWidth) maxCategoryWidth = w;
        }

        int categorySelectorY = TOP_CONTROL_Y;
        int categorySelectorWidth = clamp(maxCategoryWidth + CATEGORY_SELECTOR_HORIZONTAL_PADDING, CATEGORY_SELECTOR_MIN_WIDTH, Math.max(CATEGORY_SELECTOR_MIN_WIDTH, Math.min(CATEGORY_SELECTOR_MAX_WIDTH, this.width / 4)));
        int layoutButtonPreferredWidth = Math.max(LAYOUT_BUTTON_MIN_WIDTH, Math.max(
                Math.max(this.font.width(KeyboardLayout.MAIN.getDisplayName()), this.font.width(KeyboardLayout.NUMPAD.getDisplayName())),
                this.font.width(KeyboardLayout.AUXILIARY.getDisplayName())
        ) + 24);
        int layoutButtonGap = TOP_CONTROL_GAP;

        int bindingListMaxWidth = Math.max(BINDING_LIST_MIN_WIDTH, (int) (this.width * BINDING_LIST_MAX_WIDTH_RATIO));
        int bindingListWidth = clamp(maxBindingNameWidth + 20, BINDING_LIST_MIN_WIDTH, bindingListMaxWidth);
        int topRowMinWidth = categorySelectorWidth + 8 + LAYOUT_BUTTON_MIN_WIDTH * 2 + layoutButtonGap + SCREEN_MARGIN;
        int maxBindingListWidthForTopRow = this.width - 15 - topRowMinWidth;
        if (maxBindingListWidthForTopRow < bindingListWidth) {
            bindingListWidth = Math.max(BINDING_LIST_MIN_WIDTH, maxBindingListWidthForTopRow);
        }
        this.bindingList = new KeyBindingListWidget(this, SCREEN_MARGIN, SCREEN_MARGIN, bindingListWidth, this.height - 42, this.font.lineHeight * 3 + 10);

        int categorySelectorX = bindingListWidth + 15;
        int layoutButtonX = categorySelectorX + categorySelectorWidth + 8;
        int topLayoutAvailableWidth = Math.max(0, this.width - SCREEN_MARGIN - layoutButtonX);
        boolean wrapAuxiliaryLayoutButton = topLayoutAvailableWidth < layoutButtonPreferredWidth * 3 + layoutButtonGap * 2;
        int layoutButtonWidth;
        int auxiliaryLayoutButtonX;
        int auxiliaryLayoutButtonY;
        int layoutButtonsRight;
        if (wrapAuxiliaryLayoutButton) {
            layoutButtonWidth = Math.min(layoutButtonPreferredWidth, Math.max(1, (topLayoutAvailableWidth - layoutButtonGap) / 2));
            auxiliaryLayoutButtonX = layoutButtonX;
            auxiliaryLayoutButtonY = categorySelectorY + TOP_CONTROL_HEIGHT + TOP_ROW_GAP;
            layoutButtonsRight = layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2 - layoutButtonGap;
        } else {
            layoutButtonWidth = layoutButtonPreferredWidth;
            auxiliaryLayoutButtonX = layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2;
            auxiliaryLayoutButtonY = categorySelectorY;
            layoutButtonsRight = auxiliaryLayoutButtonX + layoutButtonWidth;
        }

        int mouseGroupWidth = MOUSE_SIDE_BUTTON_WIDTH * 2 + MOUSE_BUTTON_WIDTH + TOP_CONTROL_GAP * 2;
        int mouseGroupX = this.width - SCREEN_MARGIN - mouseGroupWidth;
        int mouseButtonY = categorySelectorY;
        int wrappedLayoutButtonsRight = wrapAuxiliaryLayoutButton ? auxiliaryLayoutButtonX + layoutButtonWidth : layoutButtonsRight;
        if (wrapAuxiliaryLayoutButton || mouseGroupX < layoutButtonsRight + 8) {
            mouseButtonY = categorySelectorY + TOP_CONTROL_HEIGHT + TOP_ROW_GAP;
            if (mouseGroupX < wrappedLayoutButtonsRight + 8) {
                mouseButtonY += TOP_CONTROL_HEIGHT + TOP_ROW_GAP;
            }
        }
        int mouseMinusX = Math.max(categorySelectorX, mouseGroupX);
        int mouseButtonX = mouseMinusX + MOUSE_SIDE_BUTTON_WIDTH + TOP_CONTROL_GAP;
        int mousePlusX = mouseButtonX + MOUSE_BUTTON_WIDTH + TOP_CONTROL_GAP;
        final int finalMouseButtonX = mouseButtonX;
        final int finalMouseButtonY = mouseButtonY;

        int topControlsBottom = Math.max(Math.max(categorySelectorY + TOP_CONTROL_HEIGHT, auxiliaryLayoutButtonY + TOP_CONTROL_HEIGHT), mouseButtonY + MOUSE_BUTTON_HEIGHT);
        int footerY = this.height - 27;
        int keyboardTop = topControlsBottom + 8;
        int keyboardBottom = footerY - 8;
        int availableKeyboardHeight = Math.max(1, keyboardBottom - keyboardTop);

        this.keyboardAnchorX = bindingListWidth + 15;
        this.keyboardWidth = Math.max(120.0F, this.width - this.keyboardAnchorX - SCREEN_MARGIN);
        this.keyboardHeight = Math.min(KEYBOARD_HEIGHT, availableKeyboardHeight);
        this.keyboardAnchorY = keyboardTop + Math.max(0.0F, (availableKeyboardHeight - this.keyboardHeight) / 2.0F);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);

        this.categorySelector = new CategorySelectorWidget(this, categorySelectorX, categorySelectorY, categorySelectorWidth, 20);
        this.mainLayoutButton = createLayoutButton(KeyboardLayout.MAIN, layoutButtonX, categorySelectorY, layoutButtonWidth);
        this.numpadLayoutButton = createLayoutButton(KeyboardLayout.NUMPAD, layoutButtonX + layoutButtonWidth + layoutButtonGap, categorySelectorY, layoutButtonWidth);
        this.auxiliaryLayoutButton = createLayoutButton(KeyboardLayout.AUXILIARY, auxiliaryLayoutButtonX, auxiliaryLayoutButtonY, layoutButtonWidth);
        updateLayoutButtons();

        int helpX = this.width - 50;
        int footerAvailable = Math.max(3, helpX - categorySelectorX - 12);
        int resetWidth = Math.max(1, Math.min(50, footerAvailable * 3 / 10));
        int resetAllWidth = Math.min(70, footerAvailable - 2 * resetWidth);
        Button screenToggleButton = new ScreenToggleButton(this.width - 27, footerY, 20,
                Component.translatable("options.controls"),
                btn -> MinecraftCompat.setScreen(this.minecraft, new ControlsScreen(this.lastScreen, this.options)));
        Button helpButton = new HelpButton(helpX, footerY);
        this.searchBar = PixelWidgets.searchBox(this.font, SCREEN_MARGIN, footerY, bindingListWidth,
                Component.translatable("gui.keyboard_wizard_ce.search"));
        this.searchBar.setMaxLength(SEARCH_MAX_LENGTH);
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);

        Button mousePlus = PixelWidgets.button(mousePlusX, mouseButtonY, MOUSE_SIDE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, Component.literal("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) this.mouseCodeIndex = 0;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });

        Button mouseMinus = PixelWidgets.button(mouseMinusX, mouseButtonY, MOUSE_SIDE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, Component.literal("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) this.mouseCodeIndex = this.mouseCodes.length - 1;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });

        Button resetBinding = PixelWidgets.button(categorySelectorX, footerY, resetWidth, 20, Component.translatable("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.resetToDefault(selectedBinding);
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        });

        Button clearBinding = PixelWidgets.button(categorySelectorX + resetWidth + 4, footerY, resetWidth, 20, Component.translatable("gui.clear"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.setModifierAndKey(selectedBinding, KeyModifier.NONE, InputConstants.UNKNOWN);
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        });

        Button resetAll = PixelWidgets.button(categorySelectorX + 2 * (resetWidth + 4), footerY, resetAllWidth, 20, Component.translatable("controls.resetAll"), b -> {
            final Screen current = MinecraftCompat.getScreen(this.minecraft);
            MinecraftCompat.setScreen(this.minecraft, new ResetAllConfirmScreen(confirm -> {
                if (confirm) {
                    for (KeyMapping k : this.options.keyMappings) KeyBindingUtil.resetToDefault(k);
                    KeyBindingUtil.refreshMappings();
                    refreshBindingList();
                }
                MinecraftCompat.setScreen(this.minecraft, current);
            }));
        });

        this.addRenderableWidget(this.bindingList);
        this.addRenderableWidget(this.keyboard);
        this.addRenderableWidget(this.categorySelector);
        this.addWidget(this.categorySelector.getCategoryList());
        this.addRenderableWidget(this.mainLayoutButton);
        this.addRenderableWidget(this.numpadLayoutButton);
        this.addRenderableWidget(this.auxiliaryLayoutButton);
        this.addRenderableWidget(helpButton);
        this.addRenderableWidget(screenToggleButton);
        this.addRenderableWidget(this.searchBar);
        this.addRenderableWidget(this.mouseButton);
        this.addRenderableWidget(mousePlus);
        this.addRenderableWidget(mouseMinus);
        this.addRenderableWidget(resetBinding);
        this.addRenderableWidget(clearBinding);
        this.addRenderableWidget(resetAll);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        PixelKeyboardStyle.drawPanel(graphics::fill, 0, 0, this.width, this.height);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);
        // The dropdown overlays the keyboard even after the keyboard widget is replaced.
        if (this.getCategorySelectorExtended()) {
            graphics.nextStratum();
            this.categorySelector.getCategoryList().extractRenderState(graphics, mouseX, mouseY, partialTick);
        }
        PixelWidgets.showTooltip(graphics, this.font, this.categorySelector.tooltipLines(), mouseX, mouseY);
    }

    @Override
    protected void addOptions() {
    }

    @Override
    protected void repositionElements() {
        String search = this.searchBar == null ? "" : this.searchBar.getValue();
        String category = this.categorySelector == null ? null : this.categorySelector.getSelectedCategory();
        this.rebuildWidgets();
        if (category != null) this.categorySelector.selectCategory(category);
        this.setSearchText(search);
        this.bindingList.tick();
    }

    public static Button createControlsEntryButton(ControlsScreen controlsScreen, Button.OnPress onPress) {
        Button button = new ControlsEntryButton(onPress);
        updateControlsEntryBounds(button, controlsScreen);
        return button;
    }

    public static void layoutControlsEntryButton(ControlsScreen controlsScreen) {
        for (GuiEventListener child : controlsScreen.children()) {
            if (child instanceof ControlsEntryButton button) {
                updateControlsEntryBounds(button, controlsScreen);
                return;
            }
        }
    }

    private static void updateControlsEntryBounds(Button button, ControlsScreen screen) {
        double scale = Minecraft.getInstance().getWindow().getGuiScale();
        int size = Math.clamp((int) Math.round(36 - 2 * scale), 24, 34);
        button.setRectangle(size, size, screen.width - size - TOGGLE_MARGIN, screen.height - size - TOGGLE_MARGIN);
    }

    private Button createLayoutButton(KeyboardLayout layout, int x, int y, int width) {
        return PixelWidgets.button(x, y, width, TOP_CONTROL_HEIGHT, layout.getDisplayName(), b -> setKeyboardLayout(layout));
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) return;
        this.keyboardLayout = layout;
        this.removeWidget(this.keyboard);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);
        this.addRenderableWidget(this.keyboard);
        updateLayoutButtons();
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(value, max));
    }

    private void updateLayoutButtons() {
        if (this.mainLayoutButton != null) this.mainLayoutButton.active = this.keyboardLayout != KeyboardLayout.MAIN;
        if (this.numpadLayoutButton != null) this.numpadLayoutButton.active = this.keyboardLayout != KeyboardLayout.NUMPAD;
        if (this.auxiliaryLayoutButton != null) this.auxiliaryLayoutButton.active = this.keyboardLayout != KeyboardLayout.AUXILIARY;
    }

    @Override
    public void tick() {
        for (GuiEventListener e : this.children()) {
            if (e instanceof TickableElement tickable) {
                tickable.tick();
            }
        }
    }

    public void refreshBindingList() {
        this.bindingList.refreshSelectedBinding();
    }

    @Nullable
    public KeyMapping getSelectedKeyMapping() {
        return this.bindingList.getSelectedKeyMapping();
    }

    public boolean getCategorySelectorExtended() {
        return this.categorySelector.extended;
    }

    public String getSelectedCategory() {
        return this.categorySelector.getSelectedCategory();
    }

    public String getFilterText() {
        return this.searchBar.getValue();
    }

    public void setSearchText(String s) {
        this.searchBar.setMaxLength(Math.max(SEARCH_MAX_LENGTH, s.length()));
        this.searchBar.setValue(s);
    }

    public void setSearchTextForKey(InputConstants.Key key) {
        this.setSearchText(KEY_FILTER_PREFIX + "<" + key.getDisplayName().getString() + ">");
    }

    private static class ScreenToggleButton extends Button {
        private ScreenToggleButton(int x, int y, int size, Component label, Button.OnPress onPress) {
            super(x, y, size, size, label, onPress, DEFAULT_NARRATION);
            this.setTooltip(Tooltip.create(label));
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            PixelKeyboardStyle.drawKeycap(graphics::fill, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                    KeyState.UNASSIGNED, this.isHoveredOrFocused());
            PixelKeyboardStyle.drawKeyboardIcon(graphics::fill, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        }
    }

    private static final class ControlsEntryButton extends ScreenToggleButton {
        private ControlsEntryButton(Button.OnPress onPress) {
            super(0, 0, 0, Component.translatable("screen.keyboard_wizard_ce.title"), onPress);
        }
    }

    private static class HelpButton extends Button {
        private HelpButton(int x, int y) {
            super(x, y, 20, 20, Component.literal("?"), button -> {}, DEFAULT_NARRATION);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            PixelWidgets.drawButton(graphics, this.getMessage(), this.getX(), this.getY(), this.getWidth(),
                    this.getHeight(), KeyState.UNASSIGNED, this.isHoveredOrFocused(), false);
            if (this.isHoveredOrFocused()) {
                PixelWidgets.showTooltip(graphics, Minecraft.getInstance().font, HELP_TOOLTIP, mouseX, mouseY);
            }
        }
    }
}
