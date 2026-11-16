package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class KeyboardWidget extends AbstractContainerEventHandler implements Renderable, TickableElement, NarratableEntry {
    public KeyWizardScreen keyWizardScreen;

    private final HashMap<Integer, KeyboardKeyWidget> keys = new HashMap<>();
    private final float anchorX;
    private final float anchorY;

    protected KeyboardWidget(KeyWizardScreen keyWizardScreen, float anchorX, float anchorY) {
        this.keyWizardScreen = keyWizardScreen;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width, height, InputConstants.Type.KEYSYM));
        return relativeX + width + keySpacing;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode, InputConstants.Type keyType) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width, height, keyType));
        return relativeX + width + keySpacing;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        List<? extends KeyboardKeyWidget> keys = this.children();
        for (KeyboardKeyWidget k : keys) {
            k.render(graphics, mouseX, mouseY, partialTick);
        }

    }

    List<Component> getTooltip() {
        if (keyWizardScreen.getCategorySelectorExtended()) return List.of();
        for (KeyboardKeyWidget k : this.children()) {
            if (k.active && k.isHovered()) {
                return k.getTooltip();
            }
        }
        return List.of();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : this.children()) {
                if (k.mouseClicked(mouseX, mouseY, button)) {
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
    public void tick() {
        for (KeyboardKeyWidget k : this.children()) {
            k.tick();
        }
    }

    public float getAnchorX() {
        return this.anchorX;
    }

    public float getAnchorY() {
        return this.anchorY;
    }

    public class KeyboardKeyWidget extends AbstractButton implements TickableElement {
        private final InputConstants.Key key;
        private List<Component> bindingNames = List.of();

        protected KeyboardKeyWidget(int keyCode, float x, float y, float width, float height, InputConstants.Type keyType) {
            super((int) x, (int) y, (int) width, (int) height, Component.empty());
            this.key = keyType.getOrCreate(keyCode);
            this.tick();
        }

        @Override
        public Component getMessage() {
            return this.key.getDisplayName();
        }

        private KeyState getState() {
            if (!this.active || this.bindingNames.isEmpty()) return KeyState.UNASSIGNED;
            return this.bindingNames.size() == 1 ? KeyState.ASSIGNED : KeyState.CONFLICT;
        }

        private List<Component> getTooltip() {
            Component status = switch (this.getState()) {
                case UNASSIGNED -> Component.translatable("gui.keyboard_wizard_ce.state.unassigned");
                case ASSIGNED -> Component.translatable("gui.keyboard_wizard_ce.state.assigned");
                case CONFLICT -> Component.translatable("gui.keyboard_wizard_ce.state.conflict");
            };
            List<Component> tooltip = new ArrayList<>(this.bindingNames.size() + 2);
            tooltip.add(this.getMessage());
            tooltip.add(status);
            this.bindingNames.stream().sorted(Comparator.comparing(Component::getString)).forEach(tooltip::add);
            return tooltip;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            KeyMapping selected = keyWizardScreen.getSelectedKeyMapping();
            boolean selectedKey = selected != null && this.key.equals(KeyBindingUtil.getKey(selected));
            boolean highlighted = this.active && this.isHoveredOrFocused() && !keyWizardScreen.getCategorySelectorExtended();
            PixelWidgets.drawButton(graphics, this.getMessage(), this.getX(), this.getY(), this.getWidth(),
                    this.getHeight(), this.getState(), highlighted, selectedKey);
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
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, this.getMessage());
        }

        @Override
        public void onPress() {
            if (Screen.hasAltDown() && Screen.hasControlDown()) {
                keyWizardScreen.setSearchTextForKey(this.key);
                return;
            }

            KeyMapping selectedKeyMapping = keyWizardScreen.getSelectedKeyMapping();
            if (selectedKeyMapping != null) {
                KeyBindingUtil.setModifierAndKey(selectedKeyMapping, KeyModifier.getActiveModifier(), this.key);
                KeyBindingUtil.refreshMappings();
            }
        }

        @Override
        public void tick() {
            List<Component> names = new ArrayList<>();
            for (KeyMapping b : Minecraft.getInstance().options.keyMappings) {
                if (KeyBindingUtil.getKey(b).equals(this.key)) {
                    names.add(Component.translatable(b.getName()));
                }
            }
            this.bindingNames = names;
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }
}
