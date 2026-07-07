package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.util.KeyBindingUtil;
import committee.nova.mkw.util.MinecraftCompat;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends OptionsSubScreen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(ModernKeyWizard.MODID, "textures/gui/key_wizard_background.png");
    private static final int KEYBOARD_HEIGHT = 180;
    private static final int KEYBOARD_MIN_HEIGHT = 132;
    private static final int CATEGORY_SELECTOR_HORIZONTAL_PADDING = 32;
    private static final int CATEGORY_SELECTOR_MIN_WIDTH = 110;
    private static final int CATEGORY_SELECTOR_MAX_WIDTH = 170;
    private static final int BINDING_LIST_MIN_WIDTH = 120;
    private static final float BINDING_LIST_MAX_WIDTH_RATIO = 0.24F;
    private static final int SCREEN_MARGIN = 10;
    private static final int TOP_CONTROL_Y = 5;
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
        int topRowMinWidth = categorySelectorWidth + 8 + LAYOUT_BUTTON_MIN_WIDTH * 3 + layoutButtonGap * 2 + SCREEN_MARGIN;
        int maxBindingListWidthForTopRow = this.width - 15 - topRowMinWidth;
        if (maxBindingListWidthForTopRow < bindingListWidth) {
            bindingListWidth = Math.max(BINDING_LIST_MIN_WIDTH, maxBindingListWidthForTopRow);
        }
        this.bindingList = new KeyBindingListWidget(this, SCREEN_MARGIN, SCREEN_MARGIN, bindingListWidth, this.height - 40, this.font.lineHeight * 3 + 10);

        int categorySelectorX = bindingListWidth + 15;
        int layoutButtonX = categorySelectorX + categorySelectorWidth + 8;
        int layoutButtonWidth = Math.min(layoutButtonPreferredWidth, Math.max(LAYOUT_BUTTON_MIN_WIDTH, (this.width - SCREEN_MARGIN - layoutButtonX - layoutButtonGap * 2) / 3));
        int layoutButtonsRight = layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 3 - layoutButtonGap;

        int mouseGroupWidth = MOUSE_SIDE_BUTTON_WIDTH * 2 + MOUSE_BUTTON_WIDTH + TOP_CONTROL_GAP * 2;
        int mouseGroupX = this.width - SCREEN_MARGIN - mouseGroupWidth;
        int mouseButtonY = categorySelectorY;
        if (mouseGroupX < layoutButtonsRight + 8) {
            mouseButtonY = categorySelectorY + TOP_CONTROL_HEIGHT + TOP_ROW_GAP;
        }
        int mouseMinusX = Math.max(categorySelectorX, mouseGroupX);
        int mouseButtonX = mouseMinusX + MOUSE_SIDE_BUTTON_WIDTH + TOP_CONTROL_GAP;
        int mousePlusX = mouseButtonX + MOUSE_BUTTON_WIDTH + TOP_CONTROL_GAP;
        final int finalMouseButtonX = mouseButtonX;
        final int finalMouseButtonY = mouseButtonY;

        int topControlsBottom = Math.max(categorySelectorY + TOP_CONTROL_HEIGHT, mouseButtonY + MOUSE_BUTTON_HEIGHT);
        int bottomControlsTop = this.height - 23;
        int keyboardTop = topControlsBottom + 8;
        int keyboardBottom = bottomControlsTop - 8;
        int availableKeyboardHeight = Math.max(KEYBOARD_MIN_HEIGHT, keyboardBottom - keyboardTop);

        this.keyboardAnchorX = bindingListWidth + 15;
        this.keyboardWidth = Math.max(120.0F, this.width - this.keyboardAnchorX - SCREEN_MARGIN);
        this.keyboardHeight = Math.max(KEYBOARD_MIN_HEIGHT, Math.min(KEYBOARD_HEIGHT, availableKeyboardHeight));
        this.keyboardAnchorY = keyboardTop + Math.max(0.0F, (availableKeyboardHeight - this.keyboardHeight) / 2.0F);
        this.keyboard = KeyboardWidgetBuilder.keyboard(this, this.keyboardLayout, this.keyboardAnchorX, this.keyboardAnchorY, this.keyboardWidth, this.keyboardHeight);

        this.categorySelector = new CategorySelectorWidget(this, categorySelectorX, categorySelectorY, categorySelectorWidth, 20);
        this.mainLayoutButton = createLayoutButton(KeyboardLayout.MAIN, layoutButtonX, categorySelectorY, layoutButtonWidth);
        this.numpadLayoutButton = createLayoutButton(KeyboardLayout.NUMPAD, layoutButtonX + layoutButtonWidth + layoutButtonGap, categorySelectorY, layoutButtonWidth);
        this.auxiliaryLayoutButton = createLayoutButton(KeyboardLayout.AUXILIARY, layoutButtonX + (layoutButtonWidth + layoutButtonGap) * 2, categorySelectorY, layoutButtonWidth);
        updateLayoutButtons();

        Button screenToggleButton = createScreenToggleButton(
                this.width - 22,
                this.height - 22,
                btn -> MinecraftCompat.setScreen(this.minecraft, new ControlsScreen(this.lastScreen, this.options))
        );
        Button helpButton = new HelpButton(this.width - 47, this.height - 22);
        this.searchBar = new EditBox(this.font, 10, this.height - 20, bindingListWidth, 14, Component.empty());
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);

        Button mousePlus = Button.builder(Component.literal("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) this.mouseCodeIndex = 0;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds(mousePlusX, mouseButtonY, MOUSE_SIDE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT).build();

        Button mouseMinus = Button.builder(Component.literal("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) this.mouseCodeIndex = this.mouseCodes.length - 1;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, finalMouseButtonX, finalMouseButtonY, MOUSE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds(mouseMinusX, mouseButtonY, MOUSE_SIDE_BUTTON_WIDTH, MOUSE_BUTTON_HEIGHT).build();

        Button resetBinding = Button.builder(Component.translatable("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.resetToDefault(selectedBinding);
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        }).bounds(bindingListWidth + 15, this.height - 23, 50, 20).build();

        Button clearBinding = Button.builder(Component.translatable("gui.clear"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.setModifierAndKey(selectedBinding, KeyModifier.NONE, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
            KeyBindingUtil.refreshMappings();
            refreshBindingList();
        }).bounds(bindingListWidth + 66, this.height - 23, 50, 20).build();

        Button resetAll = Button.builder(Component.translatable("controls.resetAll"), b -> {
            final Screen current = MinecraftCompat.getScreen(this.minecraft);
            MinecraftCompat.setScreen(this.minecraft, new ResetAllConfirmScreen(confirm -> {
                if (confirm) {
                    for (KeyMapping k : this.options.keyMappings) KeyBindingUtil.resetToDefault(k);
                    KeyBindingUtil.refreshMappings();
                    refreshBindingList();
                }
                MinecraftCompat.setScreen(this.minecraft, current);
            }));
        }).bounds(bindingListWidth + 117, this.height - 23, 70, 20).build();

        this.addRenderableWidget(this.bindingList);
        this.addRenderableWidget(this.keyboard);
        this.addRenderableWidget(this.categorySelector);
        this.addRenderableWidget(this.categorySelector.getCategoryList());
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
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BACKGROUND_TEXTURE, 0, 0, 0.0F, 0.0F, this.width, this.height, 512, 512);
        graphics.fill(0, 0, this.width, this.height, 0x77000000);
    }

    @Override
    protected void addOptions() {
    }

    public static Button createScreenToggleButton(int x, int y, Button.OnPress onPress) {
        return new TextureButton(x, y, onPress);
    }

    private Button createLayoutButton(KeyboardLayout layout, int x, int y, int width) {
        return Button.builder(layout.getDisplayName(), b -> setKeyboardLayout(layout))
                .bounds(x, y, width, TOP_CONTROL_HEIGHT)
                .build();
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
        this.searchBar.setValue(s);
    }

    public void setSearchTextForKey(InputConstants.Key key) {
        this.setSearchText(KEY_FILTER_PREFIX + "<" + key.getDisplayName().getString() + ">");
    }

    private static class TextureButton extends Button {
        private TextureButton(int x, int y, Button.OnPress onPress) {
            super(x, y, 20, 20, Component.empty(), onPress, DEFAULT_NARRATION);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int textureY = this.isHoveredOrFocused() ? 20 : 0;
            graphics.blit(RenderPipelines.GUI_TEXTURED, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, this.getX(), this.getY(), 0.0F, textureY, 20, 20, 40, 40);
        }
    }

    private static class HelpButton extends Button {
        private HelpButton(int x, int y) {
            super(x, y, 20, 20, Component.literal("?"), button -> {}, DEFAULT_NARRATION);
        }

        @Override
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            this.extractDefaultSprite(graphics);
            this.extractDefaultLabel(graphics.textRendererForWidget(this, GuiGraphicsExtractor.HoveredTextEffects.NONE));
            if (this.isHoveredOrFocused()) {
                graphics.setTooltipForNextFrame(
                        Minecraft.getInstance().font,
                        HELP_TOOLTIP.stream().map(Component::getVisualOrderText).toList(),
                        mouseX,
                        mouseY
                );
            }
        }
    }
}
