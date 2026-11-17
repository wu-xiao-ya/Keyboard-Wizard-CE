package committee.nova.mkw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
import committee.nova.mkw.core.gui.ScreenToggleSprite;
import committee.nova.mkw.util.KeyBindingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.SettingsScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyModifier;

import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.List;

public class KeyWizardScreen extends SettingsScreen {
    private static final List<ITextComponent> HELP_TOOLTIP =
            Arrays.asList(
                    new TranslationTextComponent("gui.keyboard_wizard_ce.help.title"),
                    new TranslationTextComponent("gui.keyboard_wizard_ce.help.select"),
                    new TranslationTextComponent("gui.keyboard_wizard_ce.help.middle_click"),
                    new TranslationTextComponent("gui.keyboard_wizard_ce.help.colors"),
                    new TranslationTextComponent("gui.keyboard_wizard_ce.help.search"));

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
    private Button mousePlus;
    private Button mouseMinus;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private ImageButton screenToggleButton;
    private Button helpButton;
    private TextFieldWidget searchBar;
    private Button resetBinding;
    private Button resetAll;
    private Button clearBinding;
    private Button mainLayoutButton;
    private Button numpadLayoutButton;
    private Button auxiliaryLayoutButton;
    private KeyboardLayout keyboardLayout = KeyboardLayout.MAIN;
    private float keyboardAnchorX;
    private float keyboardAnchorY;
    private float keyboardWidth;
    private static final int KEYBOARD_HEIGHT = 180;
    public static final String KEY_FILTER_PREFIX = "#key#";

    @SuppressWarnings("resource")
    public KeyWizardScreen(Screen parent) {
        super(
                parent,
                Minecraft.getInstance().options,
                new TranslationTextComponent("screen.keyboard_wizard_ce.title"));
    }

    @Override
    protected void init() {
        String previousSearch = this.searchBar == null ? "" : this.searchBar.getValue();
        int mouseButtonX = this.width - 105;
        int mouseButtonY = Math.max(30, this.height / 2 - 115);
        int mouseButtonWidth = 80;
        int mouseButtonHeight = 20;

        int maxBindingNameWidth = 0;
        if (this.minecraft == null) return;
        for (KeyBinding binding : KeyBindingUtil.getVisibleBindings()) {
            int w = this.font.width(new TranslationTextComponent(binding.getName()));
            if (w > maxBindingNameWidth) {
                maxBindingNameWidth = w;
            }
        }

        int maxCategoryWidth = 0;
        for (String category : KeyBindingUtil.getCategories()) {
            int w = this.font.width(new TranslationTextComponent(category));
            if (w > maxCategoryWidth) {
                maxCategoryWidth = w;
            }
        }

        int bindingListWidth = Math.min(Math.max(90, maxBindingNameWidth + 20), this.width / 3);
        this.bindingList =
                new KeyBindingListWidget(
                        this,
                        10,
                        10,
                        bindingListWidth,
                        this.height - 40,
                        this.font.lineHeight * 3 + 10);
        this.keyboardAnchorX = bindingListWidth + 15;
        this.keyboardAnchorY = Math.max(55, this.height / 2.0F - KEYBOARD_HEIGHT / 2.0F);
        this.keyboardWidth = this.width - this.keyboardAnchorX;
        this.keyboard =
                KeyboardWidgetBuilder.keyboard(
                        this,
                        this.keyboardLayout,
                        this.keyboardAnchorX,
                        this.keyboardAnchorY,
                        this.keyboardWidth,
                        Math.max(
                                50,
                                Math.min(
                                        KEYBOARD_HEIGHT, this.height - this.keyboardAnchorY - 29)));
        int categorySelectorX = bindingListWidth + 15;
        int categorySelectorY = 5;
        int categorySelectorWidth =
                Math.min(
                        maxCategoryWidth + 20,
                        Math.max(24, (this.width - categorySelectorX - 12) / 3));
        int layoutButtonWidth =
                Math.max(
                        16,
                        Math.min(
                                74,
                                (this.width - categorySelectorX - categorySelectorWidth - 24) / 3));
        int layoutButtonGap = 4;
        int layoutButtonX = categorySelectorX + categorySelectorWidth + 8;

        this.categorySelector =
                new CategorySelectorWidget(
                        this, categorySelectorX, categorySelectorY, categorySelectorWidth, 20);
        this.mainLayoutButton =
                createLayoutButton(KeyboardLayout.MAIN, layoutButtonX, categorySelectorY);
        this.numpadLayoutButton =
                createLayoutButton(
                        KeyboardLayout.NUMPAD,
                        layoutButtonX + layoutButtonWidth + layoutButtonGap,
                        categorySelectorY);
        this.auxiliaryLayoutButton =
                createLayoutButton(
                        KeyboardLayout.AUXILIARY,
                        layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2,
                        categorySelectorY);
        updateLayoutButtons();
        this.screenToggleButton =
                new ImageButton(
                        this.width - 22,
                        this.height - 22,
                        ScreenToggleSprite.WIDTH,
                        ScreenToggleSprite.HEIGHT,
                        ScreenToggleSprite.U,
                        ScreenToggleSprite.V,
                        ScreenToggleSprite.HOVER_V_OFFSET,
                        ModernKeyWizard.SCREEN_TOGGLE_WIDGETS,
                        ScreenToggleSprite.TEXTURE_WIDTH,
                        ScreenToggleSprite.TEXTURE_HEIGHT,
                        button ->
                                this.minecraft.setScreen(
                                        new ControlsScreen(this.lastScreen, this.options)));
        this.helpButton =
                new PixelWidgets.PixelButton(
                        this.width - 47,
                        this.height - 22,
                        20,
                        20,
                        new StringTextComponent("?"),
                        button -> {});
        this.searchBar =
                new PixelWidgets.PixelSearch(
                        this.font,
                        10,
                        this.height - 20,
                        bindingListWidth,
                        14,
                        StringTextComponent.EMPTY);
        this.searchBar.setMaxLength(256);
        this.searchBar.setValue(previousSearch);
        this.mouseButton =
                KeyboardWidgetBuilder.singleKeyKeyboard(
                        this,
                        mouseButtonX,
                        mouseButtonY,
                        mouseButtonWidth,
                        mouseButtonHeight,
                        mouseCodes[mouseCodeIndex],
                        InputMappings.Type.MOUSE);
        this.mousePlus =
                new PixelWidgets.PixelButton(
                        (int) this.mouseButton.getAnchorX() + 83,
                        (int) this.mouseButton.getAnchorY(),
                        25,
                        20,
                        new StringTextComponent("+"),
                        button -> {
                            this.mouseCodeIndex++;
                            if (this.mouseCodeIndex >= this.mouseCodes.length) {
                                this.mouseCodeIndex = 0;
                            }
                            removeChild(this.mouseButton);
                            this.mouseButton =
                                    KeyboardWidgetBuilder.singleKeyKeyboard(
                                            this,
                                            mouseButtonX,
                                            mouseButtonY,
                                            mouseButtonWidth,
                                            mouseButtonHeight,
                                            mouseCodes[mouseCodeIndex],
                                            InputMappings.Type.MOUSE);
                            addButton(this.mouseButton);
                        });
        this.mouseMinus =
                new PixelWidgets.PixelButton(
                        (int) this.mouseButton.getAnchorX() - 26,
                        (int) this.mouseButton.getAnchorY(),
                        25,
                        20,
                        new StringTextComponent("-"),
                        button -> {
                            this.mouseCodeIndex--;
                            if (this.mouseCodeIndex < 0) {
                                this.mouseCodeIndex = this.mouseCodes.length - 1;
                            }
                            removeChild(this.mouseButton);
                            this.mouseButton =
                                    KeyboardWidgetBuilder.singleKeyKeyboard(
                                            this,
                                            mouseButtonX,
                                            mouseButtonY,
                                            mouseButtonWidth,
                                            mouseButtonHeight,
                                            mouseCodes[mouseCodeIndex],
                                            InputMappings.Type.MOUSE);
                            addButton(this.mouseButton);
                        });
        this.resetBinding =
                new PixelWidgets.PixelButton(
                        bindingListWidth + 15,
                        this.height - 23,
                        50,
                        20,
                        new TranslationTextComponent("controls.reset"),
                        button -> {
                            KeyBinding selectedBinding = this.getSelectedKeyMapping();
                            if (selectedBinding == null) return;
                            KeyBindingUtil.resetToDefault(selectedBinding);
                            KeyBindingUtil.refreshMappings();
                        });
        this.clearBinding =
                new PixelWidgets.PixelButton(
                        bindingListWidth + 66,
                        this.height - 23,
                        50,
                        20,
                        new TranslationTextComponent("gui.clear"),
                        button -> {
                            KeyBinding selectedBinding = this.getSelectedKeyMapping();
                            if (selectedBinding == null) return;
                            KeyBindingUtil.setModifierAndKey(
                                    selectedBinding, KeyModifier.NONE, InputMappings.UNKNOWN);
                            KeyBindingUtil.refreshMappings();
                        });
        this.resetAll =
                new PixelWidgets.PixelButton(
                        bindingListWidth + 117,
                        this.height - 23,
                        70,
                        20,
                        new TranslationTextComponent("controls.resetAll"),
                        button -> {
                            final Screen current = this.minecraft.screen;
                            this.minecraft.setScreen(
                                    new ResetAllConfirmScreen(
                                            confirmed -> {
                                                if (confirmed) {
                                                    for (KeyBinding binding :
                                                            KeyBindingUtil.getVisibleBindings()) {
                                                        KeyBindingUtil.resetToDefault(binding);
                                                    }
                                                    KeyBindingUtil.refreshMappings();
                                                }
                                                this.minecraft.setScreen(current);
                                            }));
                        });

        this.addWidget(this.bindingList);
        this.addButton(this.keyboard);
        this.addButton(this.categorySelector);
        this.addWidget(this.categorySelector.getCategoryList());
        this.addButton(this.mainLayoutButton);
        this.addButton(this.numpadLayoutButton);
        this.addButton(this.auxiliaryLayoutButton);
        this.addButton(this.screenToggleButton);
        this.addButton(this.helpButton);
        this.addButton(this.searchBar);
        this.addButton(this.mouseButton);
        this.addButton(this.mousePlus);
        this.addButton(this.mouseMinus);
        this.addButton(this.resetBinding);
        this.addButton(this.clearBinding);
        this.addButton(this.resetAll);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        PixelKeyboardStyle.drawPanel(
                PixelWidgets.painter(matrixStack), 0, 0, this.width, this.height);
        if (this.bindingList != null) {
            this.bindingList.render(matrixStack, mouseX, mouseY, partialTicks);
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        if (this.helpButton != null && this.helpButton.isHovered()) {
            this.renderComponentTooltip(matrixStack, HELP_TOOLTIP, mouseX, mouseY);
        }
    }

    @Override
    public void tick() {
        for (IGuiEventListener listener : this.children()) {
            if (listener instanceof TickableElement) {
                ((TickableElement) listener).tick();
            }
        }
    }

    private Button createLayoutButton(KeyboardLayout layout, int x, int y) {
        return new PixelWidgets.PixelButton(
                x,
                y,
                Math.max(
                        16,
                        Math.min(
                                74,
                                (this.width
                                                - this.categorySelector.getWidth()
                                                - this.bindingList.getRowWidth()
                                                - 39)
                                        / 3)),
                20,
                layout.getDisplayName(),
                button -> setKeyboardLayout(layout));
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) return;
        this.keyboardLayout = layout;
        removeChild(this.keyboard);
        this.keyboard =
                KeyboardWidgetBuilder.keyboard(
                        this,
                        this.keyboardLayout,
                        this.keyboardAnchorX,
                        this.keyboardAnchorY,
                        this.keyboardWidth,
                        Math.max(
                                50,
                                Math.min(
                                        KEYBOARD_HEIGHT, this.height - this.keyboardAnchorY - 29)));
        this.addButton(this.keyboard);
        updateLayoutButtons();
    }

    private void updateLayoutButtons() {
        if (this.mainLayoutButton != null)
            this.mainLayoutButton.active = this.keyboardLayout != KeyboardLayout.MAIN;
        if (this.numpadLayoutButton != null)
            this.numpadLayoutButton.active = this.keyboardLayout != KeyboardLayout.NUMPAD;
        if (this.auxiliaryLayoutButton != null)
            this.auxiliaryLayoutButton.active = this.keyboardLayout != KeyboardLayout.AUXILIARY;
    }

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
        return this.searchBar.getValue();
    }

    public void setSearchText(String text) {
        this.searchBar.setMaxLength(256);
        this.searchBar.setValue(text);
    }

    public void setSearchTextForKey(InputMappings.Input key) {
        ITextComponent keyName = key.getDisplayName();
        String searchKey = I18n.get(keyName.getString());
        if (searchKey.equals(keyName.getString())) {
            searchKey = keyName.getString();
        }
        this.setSearchText(KEY_FILTER_PREFIX + "<" + searchKey + ">");
    }

    private void removeChild(Widget widget) {
        this.buttons.remove(widget);
        this.children.remove(widget);
    }
}
