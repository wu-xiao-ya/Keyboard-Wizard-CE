package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.gui.PixelKeyboardStyle.KeyState;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
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
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width, height, InputConstants.Type.KEYBOARD));
        return relativeX + width + keySpacing;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode, InputConstants.Type keyType) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width, height, keyType));
        return relativeX + width + keySpacing;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        List<? extends KeyboardKeyWidget> keys = this.children();
        for (KeyboardKeyWidget k : keys) {
            k.extractRenderState(graphics, mouseX, mouseY, partialTick);
        }

        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : keys) {
                if (k.active && k.isHovered()) {
                    PixelWidgets.showTooltip(graphics, Minecraft.getInstance().font, k.tooltipLines(), mouseX, mouseY);
                    break;
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : this.children()) {
                if (k.mouseClicked(event, doubleClick)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        if (keyWizardScreen.getCategorySelectorExtended()) {
            return false;
        }

        for (KeyboardKeyWidget k : this.children()) {
            if (k.isMouseOver(mouseX, mouseY)) {
                return true;
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
        private final Component functionKeyLabel;
        private final Component compactModifierLabel;
        private List<Component> bindingNames = List.of();

        protected KeyboardKeyWidget(int keyCode, float x, float y, float width, float height, InputConstants.Type keyType) {
            super((int) x, (int) y, (int) width, (int) height, Component.empty());
            this.key = keyType.getOrCreate(keyCode);
            // SDL's F1–F12 scancodes are contiguous; these are the function keys in the layout.
            this.functionKeyLabel = keyType == InputConstants.Type.KEYBOARD && keyCode >= InputConstants.KEY_F1
                    && keyCode <= InputConstants.KEY_F12
                    ? Component.literal("F" + (keyCode - InputConstants.KEY_F1 + 1)) : null;
            this.compactModifierLabel = keyType == InputConstants.Type.KEYBOARD ? compactModifierLabel(keyCode) : null;
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

        private List<Component> tooltipLines() {
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
        protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            KeyMapping selected = keyWizardScreen.getSelectedKeyMapping();
            boolean selectedKey = selected != null && this.key.equals(KeyBindingUtil.getKey(selected));
            boolean highlighted = this.active && this.isHoveredOrFocused() && !keyWizardScreen.getCategorySelectorExtended();
            PixelWidgets.drawKeyboardKey(graphics, this.keycapLabel(Minecraft.getInstance().font),
                    this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                    this.getState(), highlighted, selectedKey, this.functionKeyLabel != null);
        }

        private Component keycapLabel(Font font) {
            Component label = this.getMessage();
            Component compactLabel = this.functionKeyLabel != null ? this.functionKeyLabel : this.compactModifierLabel;
            return compactLabel != null && font.width(label) > PixelWidgets.labelWidth(this.getWidth()) ? compactLabel : label;
        }

        private static Component compactModifierLabel(int keyCode) {
            String compactLabel = switch (keyCode) {
                case InputConstants.KEY_LSHIFT -> "left_shift";
                case InputConstants.KEY_RSHIFT -> "right_shift";
                case InputConstants.KEY_LCONTROL -> "left_control";
                case InputConstants.KEY_RCONTROL -> "right_control";
                case InputConstants.KEY_LALT -> "left_alt";
                case InputConstants.KEY_RALT -> "right_alt";
                case InputConstants.KEY_LGUI -> "left_meta";
                case InputConstants.KEY_RGUI -> "right_meta";
                default -> null;
            };
            return compactLabel == null ? null : Component.translatable("gui.keyboard_wizard_ce.keycap." + compactLabel);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, this.getMessage());
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (!this.active || !this.visible || !this.isMouseOver(event.x(), event.y())) {
                return false;
            }

            if (event.button() == InputConstants.MOUSE_BUTTON_MIDDLE && !keyWizardScreen.getCategorySelectorExtended()) {
                keyWizardScreen.setSearchTextForKey(this.key);
                return true;
            }
            return super.mouseClicked(event, doubleClick);
        }

        @Override
        public void onPress(InputWithModifiers input) {
            if (input.hasAltDown() && input.hasControlDown()) {
                keyWizardScreen.setSearchTextForKey(this.key);
                return;
            }

            KeyMapping selectedKeyMapping = keyWizardScreen.getSelectedKeyMapping();
            if (selectedKeyMapping != null) {
                KeyModifier modifier = KeyModifier.getActiveModifiers().stream().findFirst().orElse(KeyModifier.NONE);
                KeyBindingUtil.setModifierAndKey(selectedKeyMapping, modifier, this.key);
                KeyBindingUtil.refreshMappings();
                keyWizardScreen.refreshBindingList();
            }
        }

        @Override
        public void tick() {
            List<Component> names = new ArrayList<>();
            for (KeyMapping b : KeyBindingUtil.getAllBindings()) {
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
