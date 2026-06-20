package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.api.IKeyBinding;
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
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends GameOptionsScreen {
    private static final Identifier BACKGROUND_TEXTURE = new Identifier(ModernKeyWizard.MODID, "textures/gui/key_wizard_background.png");
    private static final List<Text> HELP_TOOLTIP = List.of(
            Text.translatable("gui.keyboard_wizard_ce.help.title"),
            Text.translatable("gui.keyboard_wizard_ce.help.select"),
            Text.translatable("gui.keyboard_wizard_ce.help.middle_click"),
            Text.translatable("gui.keyboard_wizard_ce.help.colors"),
            Text.translatable("gui.keyboard_wizard_ce.help.search")
    );
    private static final Text SEARCH_HINT = Text.translatable("gui.keyboard_wizard_ce.search.hint");
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
    private TexturedButtonWidget screenToggleButton;
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
    private static final int KEYBOARD_HEIGHT = 180;

    @SuppressWarnings("resource")
    public KeyWizardScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.translatable("screen.keyboard_wizard_ce.title"));
    }

    @Override
    protected void init() {
        if (this.client == null) {
            return;
        }

        int mouseButtonX = this.width - 105;
        int mouseButtonY = this.height / 2 - 115;
        int mouseButtonWidth = 80;
        int mouseButtonHeight = 20;

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

        int bindingListWidth = maxBindingNameWidth + 20;
        this.bindingList = new KeyBindingListWidget(this, 10, 10, bindingListWidth, this.height - 40, this.textRenderer.fontHeight * 3 + 10);
        this.keyboardAnchorX = bindingListWidth + 15;
        this.keyboardAnchorY = this.height / 2.0F - KEYBOARD_HEIGHT / 2.0F;
        this.keyboardWidth = this.width - this.keyboardAnchorX;
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, KEYBOARD_HEIGHT);

        int categorySelectorX = bindingListWidth + 15;
        int categorySelectorY = 5;
        int categorySelectorWidth = maxCategoryWidth + 20;
        int layoutButtonWidth = 74;
        int layoutButtonGap = 4;
        int layoutButtonX = categorySelectorX + categorySelectorWidth + 8;

        this.categorySelector = new CategorySelectorWidget(this, categorySelectorX, categorySelectorY, categorySelectorWidth, 20);
        this.mainLayoutButton = createLayoutButton(KeyboardLayout.MAIN, layoutButtonX, categorySelectorY);
        this.numpadLayoutButton = createLayoutButton(KeyboardLayout.NUMPAD, layoutButtonX + layoutButtonWidth + layoutButtonGap, categorySelectorY);
        this.auxiliaryLayoutButton = createLayoutButton(KeyboardLayout.AUXILIARY, layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2, categorySelectorY);
        updateLayoutButtons();

        this.screenToggleButton = new TexturedButtonWidget(this.width - 22, this.height - 22, 20, 20, 20, 0, 20, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, button -> this.client.setScreen(new ControlsOptionsScreen(this.parent, this.gameOptions)));
        this.helpButton = ButtonWidget.builder(Text.literal("?"), button -> {
        }).dimensions(this.width - 47, this.height - 22, 20, 20).build();
        this.helpButton.active = false;

        this.searchBar = new TextFieldWidget(this.textRenderer, 10, this.height - 20, bindingListWidth, 14, Text.empty());
        this.searchBar.setPlaceholder(SEARCH_HINT);
        this.searchBar.setChangedListener(this::setSearchText);

        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, MOUSE_CODES[this.mouseCodeIndex], InputUtil.Type.MOUSE);
        this.mousePlus = ButtonWidget.builder(Text.literal("+"), button -> {
            this.mouseCodeIndex = (this.mouseCodeIndex + 1) % MOUSE_CODES.length;
            rebuildMouseButton(mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight);
        }).dimensions((int) this.mouseButton.getAnchorX() + 83, (int) this.mouseButton.getAnchorY(), 25, 20).build();
        this.mouseMinus = ButtonWidget.builder(Text.literal("-"), button -> {
            this.mouseCodeIndex = (this.mouseCodeIndex - 1 + MOUSE_CODES.length) % MOUSE_CODES.length;
            rebuildMouseButton(mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight);
        }).dimensions((int) this.mouseButton.getAnchorX() - 26, (int) this.mouseButton.getAnchorY(), 25, 20).build();

        this.resetBinding = ButtonWidget.builder(Text.translatable("controls.reset"), button -> {
            KeyBinding selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) {
                return;
            }
            ((IKeyBinding) selectedBinding).setToDefault();
            KeyBinding.updateKeysByCode();
        }).dimensions(bindingListWidth + 15, this.height - 23, 50, 20).build();
        this.clearBinding = ButtonWidget.builder(Text.translatable("gui.clear"), button -> {
            KeyBinding selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) {
                return;
            }
            ((IKeyBinding) selectedBinding).setKeyModifierAndCode(KeyModifier.NONE, InputUtil.UNKNOWN_KEY);
            KeyBinding.updateKeysByCode();
        }).dimensions(bindingListWidth + 66, this.height - 23, 50, 20).build();
        this.resetAll = ButtonWidget.builder(Text.translatable("controls.resetAll"), button -> {
            Screen current = this.client.currentScreen;
            this.client.setScreen(new ResetAllConfirmScreen(result -> {
                if (result) {
                    for (KeyBinding keyBinding : this.gameOptions.allKeys) {
                        ((IKeyBinding) keyBinding).setToDefault();
                    }
                    KeyBinding.updateKeysByCode();
                }
                this.client.setScreen(current);
            }));
        }).dimensions(bindingListWidth + 117, this.height - 23, 70, 20).build();

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
        this.renderBackground(ctx);
        ctx.drawTexture(BACKGROUND_TEXTURE, 0, 0, 0, 0, this.width, this.height, 512, 512);
        ctx.fill(0, 0, this.width, this.height, 0x77000000);
        super.render(ctx, mouseX, mouseY, delta);
        if (this.helpButton != null && this.helpButton.isHovered()) {
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

    private ButtonWidget createLayoutButton(KeyboardLayout layout, int x, int y) {
        return ButtonWidget.builder(layout.getDisplayName(), button -> setKeyboardLayout(layout)).dimensions(x, y, 74, 20).build();
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) {
            return;
        }
        this.keyboardLayout = layout;
        this.remove(this.keyboard);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, KEYBOARD_HEIGHT);
        this.addDrawableChild(this.keyboard);
        updateLayoutButtons();
    }

    private void updateLayoutButtons() {
        if (this.mainLayoutButton != null) this.mainLayoutButton.active = this.keyboardLayout != KeyboardLayout.MAIN;
        if (this.numpadLayoutButton != null) this.numpadLayoutButton.active = this.keyboardLayout != KeyboardLayout.NUMPAD;
        if (this.auxiliaryLayoutButton != null) this.auxiliaryLayoutButton.active = this.keyboardLayout != KeyboardLayout.AUXILIARY;
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
}
