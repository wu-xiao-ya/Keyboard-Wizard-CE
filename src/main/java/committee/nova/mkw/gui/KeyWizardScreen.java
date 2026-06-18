package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.util.KeyBindingUtil;
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
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class KeyWizardScreen extends OptionsSubScreen {
    private static final Identifier BACKGROUND_TEXTURE = Identifier.fromNamespaceAndPath(ModernKeyWizard.MODID, "textures/gui/key_wizard_background.png");
    private static final int KEYBOARD_HEIGHT = 180;
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
    private Button mainLayoutButton;
    private Button numpadLayoutButton;
    private Button auxiliaryLayoutButton;
    public static final String KEY_FILTER_PREFIX = "#key#";

    public KeyWizardScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("screen.keyboard_wizard_ce.title"));
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
            int w = this.font.width(Component.translatable(k.getName()));
            if (w > maxBindingNameWidth) maxBindingNameWidth = w;
        }

        int maxCategoryWidth = 0;
        for (String s : KeyBindingUtil.getCategories()) {
            int w = this.font.width(Component.translatable(s));
            if (w > maxCategoryWidth) maxCategoryWidth = w;
        }

        int bindingListWidth = maxBindingNameWidth + 20;
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

        Button screenToggleButton = createScreenToggleButton(
                this.width - 22,
                this.height - 22,
                btn -> this.minecraft.setScreen(new ControlsScreen(this.lastScreen, this.options))
        );
        Button helpButton = new HelpButton(this.width - 47, this.height - 22);
        this.searchBar = new EditBox(this.font, 10, this.height - 20, bindingListWidth, 14, Component.empty());
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);

        Button mousePlus = Button.builder(Component.literal("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) this.mouseCodeIndex = 0;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds((int) this.mouseButton.getAnchorX() + 83, (int) this.mouseButton.getAnchorY(), 25, 20).build();

        Button mouseMinus = Button.builder(Component.literal("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) this.mouseCodeIndex = this.mouseCodes.length - 1;
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds((int) this.mouseButton.getAnchorX() - 26, (int) this.mouseButton.getAnchorY(), 25, 20).build();

        Button resetBinding = Button.builder(Component.translatable("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.resetToDefault(selectedBinding);
            KeyMapping.resetMapping();
        }).bounds(bindingListWidth + 15, this.height - 23, 50, 20).build();

        Button clearBinding = Button.builder(Component.translatable("gui.clear"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            KeyBindingUtil.setModifierAndKey(selectedBinding, KeyModifier.NONE, InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_UNKNOWN));
            KeyMapping.resetMapping();
        }).bounds(bindingListWidth + 66, this.height - 23, 50, 20).build();

        Button resetAll = Button.builder(Component.translatable("controls.resetAll"), b -> {
            final Screen current = this.minecraft.screen;
            this.minecraft.setScreen(new ResetAllConfirmScreen(confirm -> {
                if (confirm) {
                    for (KeyMapping k : this.options.keyMappings) KeyBindingUtil.resetToDefault(k);
                    KeyMapping.resetMapping();
                }
                this.minecraft.setScreen(current);
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

    private Button createLayoutButton(KeyboardLayout layout, int x, int y) {
        return Button.builder(layout.getDisplayName(), b -> setKeyboardLayout(layout))
                .bounds(x, y, 74, 20)
                .build();
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
