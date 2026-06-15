package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyWizard;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.controls.ControlsScreen;
import net.minecraft.client.gui.screens.OptionsSubScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.KeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.settings.KeyModifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class KeyWizardScreen extends OptionsSubScreen {

    private final int[] mouseCodes = {GLFW.GLFW_MOUSE_BUTTON_1, GLFW.GLFW_MOUSE_BUTTON_2, GLFW.GLFW_MOUSE_BUTTON_3, GLFW.GLFW_MOUSE_BUTTON_4, GLFW.GLFW_MOUSE_BUTTON_5, GLFW.GLFW_MOUSE_BUTTON_6, GLFW.GLFW_MOUSE_BUTTON_7, GLFW.GLFW_MOUSE_BUTTON_8};
    private int mouseCodeIndex = 0;

    private KeyboardWidget keyboard;
    private KeyboardWidget mouseButton;
    private Button mousePlus;
    private Button mouseMinus;
    private KeyBindingListWidget bindingList;
    private CategorySelectorWidget categorySelector;
    private ImageButton screenToggleButton;
    private EditBox searchBar;
    private Button resetBinding;
    private Button resetAll;
    private Button clearBinding;

    @SuppressWarnings("resource")
    public KeyWizardScreen(Screen parent) {
        super(parent, Minecraft.getInstance().options, Component.translatable("screen.mkw.title"));
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
            if (w > maxBindingNameWidth)
                maxBindingNameWidth = w;
        }

        int maxCategoryWidth = 0;
        for (String s : KeyBindingUtil.getCategories()) {
            int w = this.font.width(Component.translatable(s));
            if (w > maxCategoryWidth)
                maxCategoryWidth = w;
        }

        int bindingListWidth = (maxBindingNameWidth + 20);
        this.bindingList = new KeyBindingListWidget(this, 10, 10, bindingListWidth, this.height - 40, this.font.lineHeight * 3 + 10);
        this.keyboard = KeyboardWidgetBuilder.standardKeyboard(this, bindingListWidth + 15, this.height / 2.0F - 90.0F, this.width - (bindingListWidth + 15), 180);
        this.categorySelector = new CategorySelectorWidget(this, bindingListWidth + 15, 5, maxCategoryWidth + 20, 20);
        this.screenToggleButton = new ImageButton(this.width - 22, this.height - 22, 20, 20, 20, 0, 20, ModernKeyWizard.SCREEN_TOGGLE_WIDGETS, 40, 40, (btn) -> this.minecraft.setScreen(new ControlsScreen(this.lastScreen, this.options)));
        this.searchBar = new EditBox(this.font, 10, this.height - 20, bindingListWidth, 14, Component.literal(""));
        this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
        this.mousePlus = Button.builder(Component.literal("+"), b -> {
            this.mouseCodeIndex++;
            if (this.mouseCodeIndex >= this.mouseCodes.length) {
                this.mouseCodeIndex = 0;
            }
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds((int) this.mouseButton.getAnchorX() + 83, (int) this.mouseButton.getAnchorY(), 25, 20).build();
        this.mouseMinus = Button.builder(Component.literal("-"), b -> {
            this.mouseCodeIndex--;
            if (this.mouseCodeIndex < 0) {
                this.mouseCodeIndex = this.mouseCodes.length - 1;
            }
            this.removeWidget(this.mouseButton);
            this.mouseButton = KeyboardWidgetBuilder.singleKeyKeyboard(this, mouseButtonX, mouseButtonY, mouseButtonWidth, mouseButtonHeight, mouseCodes[mouseCodeIndex], InputConstants.Type.MOUSE);
            this.addRenderableWidget(this.mouseButton);
        }).bounds((int) this.mouseButton.getAnchorX() - 26, (int) this.mouseButton.getAnchorY(), 25, 20).build();
        this.resetBinding = Button.builder(Component.translatable("controls.reset"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            selectedBinding.setToDefault();
            KeyMapping.resetMapping();
        }).bounds(bindingListWidth + 15, this.height - 23, 50, 20).build();
        this.clearBinding = Button.builder(Component.translatable("gui.clear"), b -> {
            KeyMapping selectedBinding = this.getSelectedKeyMapping();
            if (selectedBinding == null) return;
            selectedBinding.setKeyModifierAndCode(KeyModifier.NONE, InputConstants.UNKNOWN);
            KeyMapping.resetMapping();
        }).bounds(bindingListWidth + 66, this.height - 23, 50, 20).build();
        this.resetAll = Button.builder(Component.translatable("controls.resetAll"), b -> {
            final Screen current = minecraft.screen;
            minecraft.setScreen(new ResetAllConfirmScreen(y -> {
                if (y) {
                    for (KeyMapping k : this.options.keyMappings) k.setToDefault();
                    KeyMapping.resetMapping();
                }
                minecraft.setScreen(current);
            }));
        }).bounds(bindingListWidth + 117, this.height - 23, 70, 20).build();
        this.addRenderableWidget(this.bindingList);
        this.addRenderableWidget(this.keyboard);
        this.addRenderableWidget(this.categorySelector);
        this.addRenderableWidget(this.categorySelector.getCategoryList());
        this.addRenderableWidget(this.screenToggleButton);
        this.addRenderableWidget(this.searchBar);
        this.addRenderableWidget(this.mouseButton);
        this.addRenderableWidget(this.mousePlus);
        this.addRenderableWidget(this.mouseMinus);
        this.addRenderableWidget(this.resetBinding);
        this.addRenderableWidget(this.clearBinding);
        this.addRenderableWidget(this.resetAll);
    }

    @Override
    public void render(GuiGraphics ctx, int mouseX, int mouseY, float delta) {
        this.renderBackground(ctx);
        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public void tick() {
        for (GuiEventListener e : this.children()) {
            if (e instanceof TickableElement) {
                ((TickableElement) e).tick();
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

}

