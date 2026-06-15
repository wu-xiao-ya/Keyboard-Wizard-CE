package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.util.DrawingUtil;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : keys) {
                if (k.active && k.isHovered()) {
                    graphics.renderComponentTooltip(Minecraft.getInstance().font, k.tooltipText, mouseX, mouseY);
                }
            }
        }
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
        public float x;
        public float y;

        protected float width;
        protected float height;

        private final InputConstants.Key key;
        private List<Component> tooltipText = new ArrayList<>();

        protected KeyboardKeyWidget(int keyCode, float x, float y, float width, float height, InputConstants.Type keyType) {
            super((int) x, (int) y, (int) width, (int) height, Component.empty());
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.key = keyType.getOrCreate(keyCode);
            this.setMessage(this.key.getDisplayName());
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            int bindingCount = this.tooltipText.size();
            int color;
            if (this.active) {
                if (this.isHovered() && !keyWizardScreen.getCategorySelectorExtended()) {
                    color = bindingCount == 0 ? 0xFFAAAAAA : bindingCount == 1 ? 0xFF00AA00 : 0xFFAA0000;
                } else {
                    color = bindingCount == 0 ? 0xFFFFFFFF : bindingCount == 1 ? 0xFF00FF00 : 0xFFFF0000;
                }
            } else {
                color = 0xFF555555;
            }

            DrawingUtil.drawNoFillRect(graphics, this.x, this.y, this.x + this.width, this.y + this.height, color);
            Font font = Minecraft.getInstance().font;
            graphics.drawString(font, getMessage(), (int) (this.x + this.width / 2 - font.width(this.getMessage()) / 2.0F), (int) (this.y + (this.height - 6) / 2), color);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput output) {
            output.add(NarratedElementType.TITLE, this.getMessage());
        }

        @Override
        public void onPress() {
            if (Screen.hasAltDown() && Screen.hasControlDown()) {
                keyWizardScreen.setSearchText("<" + this.getMessage().getString() + ">");
                return;
            }

            KeyMapping selectedKeyMapping = keyWizardScreen.getSelectedKeyMapping();
            if (selectedKeyMapping != null) {
                KeyBindingUtil.setModifierAndKey(selectedKeyMapping, KeyModifier.getActiveModifier(), this.key);
                KeyMapping.resetMapping();
            }
        }

        private void updateTooltip() {
            ArrayList<String> tooltipText = new ArrayList<>();
            for (KeyMapping b : Minecraft.getInstance().options.keyMappings) {
                if (KeyBindingUtil.getKey(b).equals(this.key)) {
                    tooltipText.add(I18n.get(b.getName()));
                }
            }
            this.tooltipText = tooltipText.stream().sorted().map(Component::literal).collect(Collectors.toCollection(ArrayList::new));
        }

        @Override
        public void tick() {
            updateTooltip();
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
