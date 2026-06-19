package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.util.KeyBindingUtil;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends OptionsSubScreen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ModernKeyWizard.MODID, "textures/gui/key_wizard_background.png");
    private static final List<Component> HELP_TOOLTIP = List.of(
            new TranslatableComponent("gui.keyboard_wizard_ce.help.title"),
            new TranslatableComponent("gui.keyboard_wizard_ce.help.select"),
            new TranslatableComponent("gui.keyboard_wizard_ce.help.middle_click"),
            new TranslatableComponent("gui.keyboard_wizard_ce.help.colors"),
            new TranslatableComponent("gui.keyboard_wizard_ce.help.search")
    );

    private final int[] mouseCodes = {GLFW.GLFW_MOUSE_BUTTON_1, GLFW.GLFW_MOUSE_BUTTON_2, GLFW.GLFW_MOUSE_BUTTON_3, GLFW.GLFW_MOUSE_BUTTON_4, GLFW.GLFW_MOUSE_BUTTON_5, GLFW.GLFW_MOUSE_BUTTON_6, GLFW.GLFW_MOUSE_BUTTON_7, GLFW.GLFW_MOUSE_BUTTON_8};
    private int mouseCodeIndex = 0;

    private KeyboardWidget keyboard;
    private KeyboardWidget mouseButton;
    private Button mousePlus;
    private Button mouseMinus;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private ImageButton screenToggleButton;
    private Button helpButton;
    private EditBox searchBar;
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
        super(parent, Minecraft.getInstance().options, new TranslatableComponent("screen.keyboard_wizard_ce.title"));
    }

    @Override
    protected void init() {
        int mouseButtonX = this.width - 105;
        int mouseButtonY = this.height / 2 - 115;
        int mouseButtonWidth = 80;
        int mouseButtonHeight = 20;

        int maxBindingNameWidth = 0;
        if (this.minecraft == null) return;
        for (KeyMapping k : this.minecraft.options.keyMappings) {
            int w = this.font.width(new TranslatableComponent(k.getName()));
            if (w > maxBindingNameWidth)
                maxBindingNameWidth = w;
        }

        int maxCategoryWidth = 0;
        for (String s : KeyBindingUtil.getCategories()) {
            int w = this.font.width(new TranslatableComponent(s));
            if (w > maxCategoryWidth)
                maxCategoryWidth = w;
        }

        int bindingListWidth = (maxBindingNameWidth + 20);
        this.bindingList = new KeyBindingListWidget(this, 10, 10, bindingListWidth, this.height - 40, this.font.lineHeight * 3 + 10);
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
        this.screenToggleButton = new ImageButton(this.width - 22, this.height - 22, 20, 20, 20, 0, 20, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, (btn) -> this.minecraft.setScreen(new ControlsScreen(this.lastScreen, this.options)));
        this.helpButton = new Button(this.width - 47, this.height - 22, 20, 20, new TextComponent("?"), b -> {});
        this.searchBar = new EditBox(this.font, 10, this.height - 20, bindingListWidth, 14, TextComponent.EMPTY);
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
        this.mousePlus = new Button((int) this.mouseButton.getAnchorX() + 83, (int) this.mouseButton.getAnchorY(), 25, 20, new TextComponent("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) {
                this.mouseCodeIndex = 0;
            }
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });
        this.mouseMinus = new Button((int) this.mouseButton.getAnchorX() - 26, (int) this.mouseButton.getAnchorY(), 25, 20, new TextComponent("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) {
                this.mouseCodeIndex = this.mouseCodes.length - 1;
            }
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        });
        this.resetBinding = new Button(bindingListWidth + 15, this.height - 23, 50, 20, new TranslatableComponent("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            selectedBinding.setToDefault();
            KeyMapping.resetMapping();
        });
        this.clearBinding = new Button(bindingListWidth + 66, this.height - 23, 50, 20, new TranslatableComponent("gui.clear"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            selectedBinding.setKeyModifierAndCode(KeyModifier.NONE, InputConstants.UNKNOWN);
            KeyMapping.resetMapping();
        });
        this.resetAll = new Button(bindingListWidth + 117, this.height - 23, 70, 20, new TranslatableComponent("controls.resetAll"), b -> {
            final Screen current = minecraft.screen;
            minecraft.setScreen(new ResetAllConfirmScreen(y -> {
                if (y) {
                    for (KeyMapping k : this.options.keyMappings) k.setToDefault();
                    KeyMapping.resetMapping();
                }
                minecraft.setScreen(current);
            }));
        });
        this.addRenderableWidget(this.bindingList);
        this.addRenderableWidget(this.keyboard);
        this.addRenderableWidget(this.categorySelector);
        this.addRenderableWidget(this.categorySelector.getCategoryList());
        this.addRenderableWidget(this.mainLayoutButton);
        this.addRenderableWidget(this.numpadLayoutButton);
        this.addRenderableWidget(this.auxiliaryLayoutButton);
        this.addRenderableWidget(this.screenToggleButton);
        this.addRenderableWidget(this.helpButton);
        this.addRenderableWidget(this.searchBar);
        this.addRenderableWidget(this.mouseButton);
        this.addRenderableWidget(this.mousePlus);
        this.addRenderableWidget(this.mouseMinus);
        this.addRenderableWidget(this.resetBinding);
        this.addRenderableWidget(this.clearBinding);
        this.addRenderableWidget(this.resetAll);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        this.renderBackground(poseStack);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        blit(poseStack, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 512, 512);
        fill(poseStack, 0, 0, this.width, this.height, 0x77000000);
        super.render(poseStack, mouseX, mouseY, delta);
        if (this.helpButton != null && this.helpButton.isHoveredOrFocused()) {
            this.renderComponentTooltip(poseStack, HELP_TOOLTIP, mouseX, mouseY);
        }
    }

    @Override
    public void tick() {
        for (GuiEventListener e : this.children()) {
            if (e instanceof TickableElement) {
                ((TickableElement) e).tick();
            }
        }
    }

    private Button createLayoutButton(KeyboardLayout layout, int x, int y) {
        return new Button(x, y, 74, 20, layout.getDisplayName(), b -> setKeyboardLayout(layout));
    }

    private void setKeyboardLayout(KeyboardLayout layout) {
        if (this.keyboardLayout == layout) return;
        this.keyboardLayout = layout;
        this.removeWidget(this.keyboard);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, KEYBOARD_HEIGHT);
        this.addRenderableWidget(this.keyboard);
        updateLayoutButtons();
    }

    private void updateLayoutButtons() {
        if (this.mainLayoutButton != null) this.mainLayoutButton.active = this.keyboardLayout != KeyboardLayout.MAIN;
        if (this.numpadLayoutButton != null) this.numpadLayoutButton.active = this.keyboardLayout != KeyboardLayout.NUMPAD;
        if (this.auxiliaryLayoutButton != null) this.auxiliaryLayoutButton.active = this.keyboardLayout != KeyboardLayout.AUXILIARY;
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
        this.searchBar.setValue(s);
    }

    public void setSearchTextForKey(InputConstants.Key key) {
        Component keyName = key.getDisplayName();
        String searchKey;
        if (keyName instanceof TranslatableComponent translatable) {
            searchKey = I18n.get(translatable.getKey());
        } else {
            searchKey = keyName.getString();
        }
        this.setSearchText(KEY_FILTER_PREFIX + "<" + searchKey + ">");
    }

}
