package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends OptionsSubScreen {
    private static final int KEYBOARD_HEIGHT = 180;
    private static final int SEARCH_MAX_LENGTH = 256;
    private static final List<Component> HELP_TOOLTIP = List.of(
            Component.translatable("gui.keyboard_wizard_ce.help.title"),
            Component.translatable("gui.keyboard_wizard_ce.help.select"),
            Component.translatable("gui.keyboard_wizard_ce.help.middle_click"),
            Component.translatable("gui.keyboard_wizard_ce.help.colors"),
            Component.translatable("gui.keyboard_wizard_ce.help.search")
    );

    private final int[] mouseCodes = {
            GLFW.GLFW_MOUSE_BUTTON_1,
            GLFW.GLFW_MOUSE_BUTTON_2,
            GLFW.GLFW_MOUSE_BUTTON_3,
            GLFW.GLFW_MOUSE_BUTTON_4,
            GLFW.GLFW_MOUSE_BUTTON_5,
            GLFW.GLFW_MOUSE_BUTTON_6,
            GLFW.GLFW_MOUSE_BUTTON_7,
            GLFW.GLFW_MOUSE_BUTTON_8
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
    private float keyboardHeight;
    private Button mainLayoutButton;
    private Button numpadLayoutButton;
    private Button auxiliaryLayoutButton;
    private Button helpButton;
    public static final String KEY_FILTER_PREFIX = "#key#";

    public KeyWizardScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("screen.keyboard_wizard_ce.title"));
    }

    public static Button createControlsEntryButton(ControlsScreen controlsScreen) {
        return new ControlsEntryButton(controlsScreen);
    }

    public static void layoutControlsEntryButton(ControlsScreen controlsScreen) {
        for (GuiEventListener child : controlsScreen.children()) {
            if (child instanceof ControlsEntryButton entry) {
                entry.updateBounds(controlsScreen);
                return;
            }
        }
    }

    private static final class ControlsEntryButton extends ScreenToggleButton {
        private static final int MARGIN = 3;

        private ControlsEntryButton(ControlsScreen controlsScreen) {
            super(0, 0, 0, Component.translatable("screen.keyboard_wizard_ce.title"),
                    button -> Minecraft.getInstance().setScreen(new KeyWizardScreen(controlsScreen)));
            this.setTooltip(Tooltip.create(this.getMessage()));
            this.updateBounds(controlsScreen);
        }

        private void updateBounds(ControlsScreen controlsScreen) {
            double scale = Minecraft.getInstance().getWindow().getGuiScale();
            int size = Math.clamp((int) Math.round(36 - 2 * scale), 24, 34);
            int x = controlsScreen.width - size - MARGIN;
            int y = controlsScreen.height - size - MARGIN;
            if (this.getWidth() != size || this.getHeight() != size || this.getX() != x || this.getY() != y) {
                this.setRectangle(size, size, x, y);
            }
        }
    }

    @Override
    protected void init() {
        int mouseButtonX = this.width - 114;
        int mouseButtonY = Math.max(28, this.height / 2 - 115);
        int mouseButtonWidth = 80;
        int mouseButtonHeight = 20;

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

        int bindingListWidth = Math.min(Math.max(90, maxBindingNameWidth + 20), this.width / 3);
        this.bindingList = new KeyBindingListWidget(this, 8, 8, bindingListWidth, this.height - 42, this.font.lineHeight * 3 + 10);
        int keyboardX = bindingListWidth + 15;
        this.keyboardAnchorX = keyboardX;
        this.keyboardAnchorY = Math.max(mouseButtonY + 24, this.height / 2.0F - KEYBOARD_HEIGHT / 2.0F);
        this.keyboardWidth = this.width - this.keyboardAnchorX - 8;
        this.keyboardHeight = Math.min(KEYBOARD_HEIGHT, this.height - this.keyboardAnchorY - 29);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);
        int categorySelectorX = keyboardX;
        int categorySelectorY = 7;
        int categorySelectorWidth = Math.min(maxCategoryWidth + 20, (int) this.keyboardWidth / 3);
        int layoutButtonGap = 4;
        int layoutButtonX = categorySelectorX + categorySelectorWidth + layoutButtonGap;
        int layoutButtonWidth = Math.min(74, (this.width - 8 - layoutButtonX - layoutButtonGap * 2) / 3);

        this.categorySelector = new CategorySelectorWidget(this, categorySelectorX, categorySelectorY, categorySelectorWidth, 20);
        this.mainLayoutButton = createLayoutButton(KeyboardLayout.MAIN, layoutButtonX, categorySelectorY, layoutButtonWidth);
        this.numpadLayoutButton = createLayoutButton(KeyboardLayout.NUMPAD, layoutButtonX + layoutButtonWidth + layoutButtonGap, categorySelectorY, layoutButtonWidth);
        this.auxiliaryLayoutButton = createLayoutButton(KeyboardLayout.AUXILIARY, layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2, categorySelectorY, layoutButtonWidth);
        updateLayoutButtons();

        Button screenToggleButton = createScreenToggleButton(
                this.width - 27,
                this.height - 27,
                btn -> this.minecraft.setScreen(new ControlsScreen(this.lastScreen, this.options))
        );
        int footerY = this.height - 27;
        int helpX = this.width - 50;
        int footerAvailable = helpX - keyboardX - 12;
        int resetWidth = Math.min(50, footerAvailable * 3 / 10);
        int resetAllWidth = Math.min(70, footerAvailable - 2 * resetWidth);
        this.helpButton = PixelWidgets.button(helpX, footerY, 20, 20, Component.literal("?"), b -> {});
        this.searchBar = PixelWidgets.searchBox(this.font, 8, footerY, bindingListWidth, Component.translatable("gui.keyboard_wizard_ce.search"));
        this.searchBar.setMaxLength(SEARCH_MAX_LENGTH);
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);

        Button mousePlus = PixelWidgets.button(mouseButtonX + 83, mouseButtonY, 25, 20, Component.literal("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) this.mouseCodeIndex = 0;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });

        Button mouseMinus = PixelWidgets.button(mouseButtonX - 26, mouseButtonY, 25, 20, Component.literal("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) this.mouseCodeIndex = this.mouseCodes.length - 1;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });

        Button resetBinding = PixelWidgets.button(keyboardX, footerY, resetWidth, 20, Component.translatable("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.resetToDefault(selectedBinding);
            KeyBindingUtil.refreshMappings();
        });

        Button clearBinding = PixelWidgets.button(keyboardX + resetWidth + 4, footerY, resetWidth, 20,
                Component.translatable("gui.clear"), b -> this.clearBinding(this.getSelectedKeyMapping()));

        Button resetAll = PixelWidgets.button(keyboardX + 2 * (resetWidth + 4), footerY, resetAllWidth, 20, Component.translatable("controls.resetAll"), b -> {
            final Screen current = this.minecraft.screen;
            this.minecraft.setScreen(new ResetAllConfirmScreen(confirm -> {
                if (confirm) {
                    for (KeyMapping k : this.options.keyMappings) KeyBindingUtil.resetToDefault(k);
                    KeyBindingUtil.refreshMappings();
                }
                this.minecraft.setScreen(current);
            }));
        });

        this.addRenderableWidget(this.bindingList);
        this.addRenderableWidget(this.keyboard);
        this.addRenderableWidget(this.categorySelector);
        this.addRenderableWidget(this.categorySelector.getCategoryList());
        this.addRenderableWidget(this.mainLayoutButton);
        this.addRenderableWidget(this.numpadLayoutButton);
        this.addRenderableWidget(this.auxiliaryLayoutButton);
        this.addRenderableWidget(this.helpButton);
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        PixelKeyboardStyle.drawPanel(graphics::fill, 0, 0, this.width, this.height);
        List<Component> buttonTooltip = List.of();
        for (Renderable renderable : this.renderables) {
            if (renderable == this.categorySelector.getCategoryList()) continue;
            renderable.render(graphics, mouseX, mouseY, partialTick);
            if (renderable instanceof Button button && button.isHovered()
                    && this.font.width(button.getMessage()) > PixelWidgets.labelWidth(button.getWidth())) {
                buttonTooltip = List.of(button.getMessage());
            }
        }
        this.categorySelector.getCategoryList().render(graphics, mouseX, mouseY, partialTick);
        List<Component> tooltip = this.categorySelector.tooltipLines();
        if (!this.getCategorySelectorExtended() && tooltip.isEmpty()) {
            if (this.helpButton.isHoveredOrFocused()) {
                tooltip = HELP_TOOLTIP;
            } else if (!buttonTooltip.isEmpty()) {
                tooltip = buttonTooltip;
            } else {
                tooltip = this.mouseButton.tooltipLines();
                if (tooltip.isEmpty()) tooltip = this.keyboard.tooltipLines();
                if (tooltip.isEmpty()) tooltip = this.bindingList.tooltipLines();
            }
        }
        PixelWidgets.renderTooltip(graphics, this.font, tooltip, mouseX, mouseY);
    }

    @Override
    protected void addOptions() {
    }

    public static Button createScreenToggleButton(int x, int y, Button.OnPress onPress) {
        return new ScreenToggleButton(x, y, 20, Component.translatable("options.controls"), onPress);
    }

    private Button createLayoutButton(KeyboardLayout layout, int x, int y, int width) {
        return PixelWidgets.button(x, y, width, 20, layout.getDisplayName(), b -> setKeyboardLayout(layout));
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) return;
        this.keyboardLayout = layout;
        this.removeWidget(this.keyboard);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);
        this.addRenderableWidget(this.keyboard);
        updateLayoutButtons();
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

    @Nullable
    public KeyMapping getSelectedKeyMapping() {
        return this.bindingList.getSelectedKeyMapping();
    }

    void clearBinding(@Nullable KeyMapping binding) {
        if (binding == null) return;
        KeyBindingUtil.setModifierAndKey(binding, KeyModifier.NONE, InputConstants.UNKNOWN);
        KeyBindingUtil.refreshMappings();
        this.bindingList.refreshAfterClear();
        this.keyboard.tick();
        this.mouseButton.tick();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT && this.getCategorySelectorExtended()) {
            return this.categorySelector.mouseClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
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
        private static final int KEYBOARD_WIDTH = 16;
        private static final int KEYBOARD_HEIGHT = 10;

        private ScreenToggleButton(int x, int y, int size, Component label, Button.OnPress onPress) {
            super(x, y, size, size, label, onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            PixelKeyboardStyle.drawKeycap(graphics::fill, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                    KeyState.UNASSIGNED, this.isHoveredOrFocused());
            renderKeyboardIcon(graphics);
        }

        private void renderKeyboardIcon(GuiGraphics graphics) {
            float scale = Math.min(1.0F, (this.getWidth() - 6.0F) / KEYBOARD_WIDTH);
            var pose = graphics.pose();
            pose.pushPose();
            pose.translate(this.getX() + (this.getWidth() - KEYBOARD_WIDTH * scale) / 2.0F,
                    this.getY() + (this.getHeight() - KEYBOARD_HEIGHT * scale) / 2.0F, 0.0F);
            pose.scale(scale, scale, 1.0F);
            graphics.fill(1, 0, KEYBOARD_WIDTH - 1, KEYBOARD_HEIGHT, PixelKeyboardStyle.BORDER);
            graphics.fill(0, 1, KEYBOARD_WIDTH, KEYBOARD_HEIGHT - 1, PixelKeyboardStyle.BORDER);
            graphics.fill(1, 1, KEYBOARD_WIDTH - 1, 2, PixelKeyboardStyle.PANEL_FOOT);

            for (int column = 0; column < 4; column++) {
                int keyX = 2 + 3 * column;
                graphics.fill(keyX, 2, keyX + 2, 4, PixelKeyboardStyle.PANEL_HIGHLIGHT);
                graphics.fill(keyX + 1, 5, keyX + 3, 6, PixelKeyboardStyle.PANEL);
            }

            graphics.fill(2, 7, 4, 8, KeyState.ASSIGNED.bevel);
            graphics.fill(5, 7, 11, 8, PixelKeyboardStyle.PANEL_HIGHLIGHT);
            graphics.fill(12, 7, 14, 8, KeyState.CONFLICT.bevel);
            graphics.fill(2, 8, 14, 9, PixelKeyboardStyle.PANEL_FOOT);
            pose.popPose();
        }
    }
}
