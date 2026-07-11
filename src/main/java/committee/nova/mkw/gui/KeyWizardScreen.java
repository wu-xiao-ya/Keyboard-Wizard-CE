package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.core.layout.KeyboardScreenLayout;
import committee.nova.mkw.core.layout.KeyboardScreenLayout.Rect;
import committee.nova.mkw.core.layout.KeyboardScreenLayoutCalculator;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsOptionsScreen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends GameOptionsScreen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(ModernKeyWizard.MODID, "textures/gui/key_wizard_background.png");
    private static final List<Text> HELP_TOOLTIP = List.of(
            Text.translatable("gui.keyboard_wizard_ce.help.title"),
            Text.translatable("gui.keyboard_wizard_ce.help.select"),
            Text.translatable("gui.keyboard_wizard_ce.help.middle_click"),
            Text.translatable("gui.keyboard_wizard_ce.help.colors"),
            Text.translatable("gui.keyboard_wizard_ce.help.search")
    );
    public static final String KEY_FILTER_PREFIX = "#key#";
    private static final int[] MOUSE_CODES = {
            GLFW.GLFW_MOUSE_BUTTON_1,
            GLFW.GLFW_MOUSE_BUTTON_2,
            GLFW.GLFW_MOUSE_BUTTON_3,
            GLFW.GLFW_MOUSE_BUTTON_4,
            GLFW.GLFW_MOUSE_BUTTON_5,
            GLFW.GLFW_MOUSE_BUTTON_6,
            GLFW.GLFW_MOUSE_BUTTON_7,
            GLFW.GLFW_MOUSE_BUTTON_8
    };

    private KeyboardWidget keyboard;
    private KeyboardWidget mouseButton;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private ButtonWidget screenToggleButton;
    private ButtonWidget helpButton;
    private TextFieldWidget searchBar;
    private ButtonWidget resetBinding;
    private ButtonWidget resetAll;
    private ButtonWidget clearBinding;
    private ButtonWidget mainLayoutButton;
    private ButtonWidget numpadLayoutButton;
    private ButtonWidget auxiliaryLayoutButton;
    private ButtonWidget mousePlus;
    private ButtonWidget mouseMinus;
    private int mouseCodeIndex;
    private KeyboardLayout keyboardLayout = KeyboardLayout.MAIN;
    private float keyboardAnchorX;
    private float keyboardAnchorY;
    private float keyboardWidth;
    private float keyboardHeight = KeyboardScreenLayoutCalculator.KEYBOARD_MAX_HEIGHT;

    @SuppressWarnings("resource")
    public KeyWizardScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("screen.keyboard_wizard_ce.title"));
    }

    @Override
    protected void init() {
        if (this.client == null) {
            return;
        }

        int maxBindingNameWidth = 0;
        for (KeyBinding keyBinding : this.client.options.allKeys) {
            int width = this.textRenderer.getWidth(Text.translatable(keyBinding.getTranslationKey()));
            maxBindingNameWidth = Math.max(maxBindingNameWidth, width);
        }

        int maxCategoryWidth = 0;
        for (String category : KeyBindingUtil.getCategories()) {
            int width = this.textRenderer.getWidth(Text.translatable(category));
            maxCategoryWidth = Math.max(maxCategoryWidth, width);
        }

        KeyboardScreenLayout layout = KeyboardScreenLayoutCalculator.calculate(
                this.width,
                this.height,
                maxBindingNameWidth,
                maxCategoryWidth,
                this.textRenderer.getWidth(KeyboardLayout.MAIN.getDisplayName()),
                this.textRenderer.getWidth(KeyboardLayout.NUMPAD.getDisplayName()),
                this.textRenderer.getWidth(KeyboardLayout.AUXILIARY.getDisplayName())
        );
        Rect bindingListBounds = layout.bindingList();
        Rect categoryBounds = layout.categorySelector();
        Rect mouseBounds = layout.mouseKey();
        Rect keyboardBounds = layout.keyboard();

        this.bindingList = new KeyBindingListWidget(
                this,
                bindingListBounds.y(),
                bindingListBounds.x(),
                bindingListBounds.width(),
                bindingListBounds.height(),
                this.textRenderer.fontHeight * 3 + 10
        );

        this.keyboardAnchorX = keyboardBounds.x();
        this.keyboardAnchorY = keyboardBounds.y();
        this.keyboardWidth = keyboardBounds.width();
        this.keyboardHeight = keyboardBounds.height();
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);

        this.categorySelector = new CategorySelectorWidget(this, categoryBounds, this.height);
        this.mainLayoutButton = createLayoutButton(KeyboardLayout.MAIN, layout.mainLayoutButton());
        this.numpadLayoutButton = createLayoutButton(KeyboardLayout.NUMPAD, layout.numpadLayoutButton());
        this.auxiliaryLayoutButton = createLayoutButton(KeyboardLayout.AUXILIARY, layout.auxiliaryLayoutButton());
        updateLayoutButtons();

        this.screenToggleButton = createScreenToggleButton(layout.screenToggleButton().x(), layout.screenToggleButton().y(), button -> this.client.setScreen(new ControlsOptionsScreen(this.parent, this.gameOptions)));
        this.helpButton = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(layout.helpButton().x(), layout.helpButton().y(), layout.helpButton().width(), layout.helpButton().height()).build();
        this.searchBar = new TextFieldWidget(
                this.textRenderer,
                layout.searchBar().x(),
                layout.searchBar().y(),
                layout.searchBar().width(),
                layout.searchBar().height(),
                Text.empty()
        );
        this.searchBar.setChangedListener(this::setSearchText);

        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseBounds.x(), mouseBounds.y(), mouseBounds.width(), mouseBounds.height(), MOUSE_CODES[this.mouseCodeIndex], InputUtil.Type.MOUSE);
        this.mousePlus = ButtonWidget.builder(Text.literal("+"), button -> {
            this.mouseCodeIndex = (this.mouseCodeIndex + 1) % MOUSE_CODES.length;
            rebuildMouseButton(mouseBounds.x(), mouseBounds.y(), mouseBounds.width(), mouseBounds.height());
        }).dimensions(layout.mousePlusButton().x(), layout.mousePlusButton().y(), layout.mousePlusButton().width(), layout.mousePlusButton().height()).build();
        this.mouseMinus = ButtonWidget.builder(Text.literal("-"), button -> {
            this.mouseCodeIndex = (this.mouseCodeIndex - 1 + MOUSE_CODES.length) % MOUSE_CODES.length;
            rebuildMouseButton(mouseBounds.x(), mouseBounds.y(), mouseBounds.width(), mouseBounds.height());
        }).dimensions(layout.mouseMinusButton().x(), layout.mouseMinusButton().y(), layout.mouseMinusButton().width(), layout.mouseMinusButton().height()).build();

        this.resetBinding = ButtonWidget.builder(Text.translatable("controls.reset"), button -> {
            KeyBinding selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) {
                return;
            }
            KeyBindingUtil.resetToDefault(selectedBinding);
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        }).dimensions(layout.resetButton().x(), layout.resetButton().y(), layout.resetButton().width(), layout.resetButton().height()).build();
        this.clearBinding = ButtonWidget.builder(Text.translatable("gui.clear"), button -> {
            KeyBinding selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) {
                return;
            }
            KeyBindingUtil.setModifierAndKey(selectedBinding, KeyModifier.NONE, InputUtil.UNKNOWN_KEY);
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        }).dimensions(layout.clearButton().x(), layout.clearButton().y(), layout.clearButton().width(), layout.clearButton().height()).build();
        this.resetAll = ButtonWidget.builder(Text.translatable("controls.resetAll"), button -> {
            Screen current = this.client.currentScreen;
            this.client.setScreen(new ResetAllConfirmScreen(result -> {
                if (result) {
                    for (KeyBinding keyBinding : this.gameOptions.allKeys) {
                        KeyBindingUtil.resetToDefault(keyBinding);
                    }
                    KeyBindingUtil.refreshMappings();
                    refreshBindingList();
                }
                this.client.setScreen(current);
            }));
        }).dimensions(layout.resetAllButton().x(), layout.resetAllButton().y(), layout.resetAllButton().width(), layout.resetAllButton().height()).build();

        this.addDrawableChild(this.bindingList);
        this.addDrawableChild(this.keyboard);
        this.addDrawableChild(this.categorySelector);
        this.addDrawableChild(this.categorySelector.getCategoryList());
        this.addDrawableChild(this.mainLayoutButton);
        this.addDrawableChild(this.numpadLayoutButton);
        this.addDrawableChild(this.auxiliaryLayoutButton);
        this.addDrawableChild(this.screenToggleButton);
        this.addDrawableChild(this.helpButton);
        this.addDrawableChild(this.searchBar);
        this.addDrawableChild(this.mouseButton);
        this.addDrawableChild(this.mousePlus);
        this.addDrawableChild(this.mouseMinus);
        this.addDrawableChild(this.resetBinding);
        this.addDrawableChild(this.clearBinding);
        this.addDrawableChild(this.resetAll);
    }

    private void rebuildMouseButton(int mouseButtonX, int mouseButtonY, int mouseButtonWidth, int mouseButtonHeight) {
        this.remove(this.mouseButton);
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, MOUSE_CODES[this.mouseCodeIndex], InputUtil.Type.MOUSE);
        this.addDrawableChild(this.mouseButton);
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx, mouseX, mouseY, delta);
        ctx.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, 0, 0, 0, 0, this.width, this.height, 512, 512);
        ctx.fill(0, 0, this.width, this.height, 0x77000000);
        super.render(ctx, mouseX, mouseY, delta);
        if (this.helpButton != null && this.helpButton.isMouseOver(mouseX, mouseY)) {
            ctx.drawTooltip(this.textRenderer, HELP_TOOLTIP, mouseX, mouseY);
        }
    }

    @Override
    public void tick() {
        for (Element child : this.children()) {
            if (child instanceof TickableElement tickableElement) {
                tickableElement.tick();
            }
        }
    }

    private ButtonWidget createLayoutButton(KeyboardLayout layout, Rect bounds) {
        return ButtonWidget.builder(layout.getDisplayName(), button -> setKeyboardLayout(layout))
                .dimensions(bounds.x(), bounds.y(), bounds.width(), bounds.height())
                .build();
    }

    public static ButtonWidget createScreenToggleButton(int x, int y, ButtonWidget.PressAction onPress) {
        return new TextureButton(x, y, onPress);
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) {
            return;
        }
        this.keyboardLayout = layout;
        this.remove(this.keyboard);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);
        this.addDrawableChild(this.keyboard);
        updateLayoutButtons();
    }

    private void updateLayoutButtons() {
        if (this.mainLayoutButton != null) this.mainLayoutButton.active = this.keyboardLayout != KeyboardLayout.MAIN;
        if (this.numpadLayoutButton != null) this.numpadLayoutButton.active = this.keyboardLayout != KeyboardLayout.NUMPAD;
        if (this.auxiliaryLayoutButton != null) this.auxiliaryLayoutButton.active = this.keyboardLayout != KeyboardLayout.AUXILIARY;
    }

    public void refreshBindingList() {
        this.bindingList.refreshSelectedBinding();
    }

    @Nullable
    public KeyBinding getSelectedKeyMapping() {
        return this.bindingList.getSelectedKeyMapping();
    }

    public boolean getCategorySelectorExtended() {
        return this.categorySelector.extended;
    }

    public String getSelectedCategory() {
        return this.categorySelector.getSelectedCategory();
    }

    public String getFilterText() {
        return this.searchBar.getText();
    }

    public void setSearchText(String value) {
        if (!this.searchBar.getText().equals(value)) {
            this.searchBar.setText(value);
        }
    }

    public void setSearchTextForKey(InputUtil.Key key) {
        Text keyName = key.getLocalizedText();
        String searchKey;
        if (keyName.getContent() instanceof TranslatableTextContent contents) {
            searchKey = I18n.translate(contents.getKey());
        } else {
            searchKey = keyName.getString();
        }
        this.setSearchText(KEY_FILTER_PREFIX + "<" + searchKey + ">");
    }

    private static class TextureButton extends ButtonWidget {
        protected TextureButton(int x, int y, PressAction onPress) {
            super(x, y, 20, 20, net.minecraft.text.Text.empty(), onPress, DEFAULT_NARRATION_SUPPLIER);
        }

        @Override
        public void renderWidget(DrawContext ctx, int mouseX, int mouseY, float delta) {
            int textureY = this.isMouseOver(mouseX, mouseY) ? 20 : 0;
            ctx.drawTexture(RenderPipelines.GUI_TEXTURED, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, this.getX(), this.getY(), 0, textureY, 20, 20, 40, 40);
        }
    }
}
