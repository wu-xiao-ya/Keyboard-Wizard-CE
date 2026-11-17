package committee.nova.mkw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import committee.nova.mkw.util.KeyBindingUtil;

import net.minecraft.client.gui.INestedGuiEventHandler;
import net.minecraft.client.gui.IRenderable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardWidget extends Widget
        implements IRenderable, INestedGuiEventHandler, TickableElement {
    private final KeyWizardScreen keyWizardScreen;
    private final HashMap<Integer, KeyboardKeyWidget> keys = new HashMap<>();
    private final float anchorX;
    private final float anchorY;

    protected KeyboardWidget(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY) {
        super((int) anchorX, (int) anchorY, 0, 0, StringTextComponent.EMPTY);
        this.keyWizardScreen = keyWizardScreen;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public float addKey(
            float relativeX,
            float relativeY,
            float width,
            float height,
            float keySpacing,
            int keyCode) {
        return addKey(
                relativeX,
                relativeY,
                width,
                height,
                keySpacing,
                keyCode,
                InputMappings.Type.KEYSYM);
    }

    public float addKey(
            float relativeX,
            float relativeY,
            float width,
            float height,
            float keySpacing,
            int keyCode,
            InputMappings.Type keyType) {
        this.keys.put(
                keyCode,
                new KeyboardKeyWidget(
                        keyCode,
                        this.anchorX + relativeX,
                        this.anchorY + relativeY,
                        width,
                        height,
                        keyType));
        return relativeX + width + keySpacing;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for (KeyboardKeyWidget key : this.children()) {
            key.render(matrixStack, mouseX, mouseY, partialTicks);
        }

        if (!this.keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget key : this.children()) {
                if (key.active && key.isHovered()) {
                    this.keyWizardScreen.renderComponentTooltip(
                            matrixStack, key.tooltipText, mouseX, mouseY);
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget key : this.children()) {
                if (key.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<? extends KeyboardKeyWidget> children() {
        return new ArrayList<>(this.keys.values());
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {}

    @Override
    public net.minecraft.client.gui.IGuiEventListener getFocused() {
        return null;
    }

    @Override
    public void setFocused(net.minecraft.client.gui.IGuiEventListener listener) {}

    @Override
    public void tick() {
        for (KeyboardKeyWidget key : this.children()) {
            key.tick();
        }
    }

    public float getAnchorX() {
        return this.anchorX;
    }

    public float getAnchorY() {
        return this.anchorY;
    }

    public class KeyboardKeyWidget extends Button implements TickableElement {
        public float x;
        public float y;
        protected float width;
        protected float height;

        private final InputMappings.Input key;
        private List<ITextComponent> tooltipText = new ArrayList<>();

        protected KeyboardKeyWidget(
                int keyCode,
                float x,
                float y,
                float width,
                float height,
                InputMappings.Type keyType) {
            super(
                    (int) x,
                    (int) y,
                    (int) width,
                    (int) height,
                    StringTextComponent.EMPTY,
                    button -> {});
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.key = keyType.getOrCreate(keyCode);
            this.setMessage(this.key.getDisplayName());
        }

        @Override
        public void renderButton(
                MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            PixelWidgets.key(
                    matrixStack,
                    getMessage().getString(),
                    (int) this.x,
                    (int) this.y,
                    (int) this.width,
                    (int) this.height,
                    this.tooltipText.size(),
                    this.active
                            && this.isHovered()
                            && !keyWizardScreen.getCategorySelectorExtended());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (!this.active || !this.visible || !this.isHovered()) {
                return false;
            }

            if (button == 2 && !keyWizardScreen.getCategorySelectorExtended()) {
                keyWizardScreen.setSearchTextForKey(this.key);
                return true;
            }

            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public void onPress() {
            if (Screen.hasAltDown() && Screen.hasControlDown()) {
                keyWizardScreen.setSearchText("<" + this.getMessage().getString() + ">");
                return;
            }

            KeyBinding selectedKeyBinding = keyWizardScreen.getSelectedKeyMapping();
            if (selectedKeyBinding != null) {
                KeyBindingUtil.setModifierAndKey(selectedKeyBinding, getActiveModifier(), this.key);
                KeyBindingUtil.refreshMappings();
            }
        }

        @Override
        public void tick() {
            updateTooltip();
        }

        @SuppressWarnings("resource")
        private void updateTooltip() {
            ArrayList<String> tooltip = new ArrayList<>();
            for (KeyBinding binding : KeyBindingUtil.getVisibleBindings()) {
                if (KeyBindingUtil.getKey(binding).equals(this.key)) {
                    tooltip.add(I18n.get(binding.getName()));
                }
            }
            this.tooltipText =
                    tooltip.stream()
                            .sorted()
                            .map(StringTextComponent::new)
                            .collect(Collectors.toCollection(ArrayList::new));
        }

        @SuppressWarnings("removal")
        private KeyModifier getActiveModifier() {
            return KeyModifier.getActiveModifier();
        }
    }
}
