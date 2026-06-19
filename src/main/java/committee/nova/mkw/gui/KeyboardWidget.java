package committee.nova.mkw.gui;

import committee.nova.mkw.util.DrawingUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.TabOrderedElement;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.resources.language.I18n;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraftforge.client.settings.KeyModifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class KeyboardWidget extends AbstractContainerEventHandler implements Renderable, TickableElement, TabOrderedElement, NarratableEntry {
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
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width,
                height, InputConstants.Type.KEYSYM));
        return relativeX + width + keySpacing;
    }

    public float addKey(float relativeX, float relativeY, float width, float height, float keySpacing, int keyCode,
                        InputConstants.Type keyType) {
        this.keys.put(keyCode, new KeyboardKeyWidget(keyCode, this.anchorX + relativeX, this.anchorY + relativeY, width,
                height, keyType));
        return relativeX + width + keySpacing;
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        List<? extends KeyboardKeyWidget> keys = this.children();
        for (KeyboardKeyWidget k : keys) {
            k.render(poseStack, mouseX, mouseY, delta);
        }

        if (!keyWizardScreen.getCategorySelectorExtended()) {
            for (KeyboardKeyWidget k : keys) {
                if (k.active && k.isHovered() && Minecraft.getInstance().screen != null) {
                    Minecraft.getInstance().screen.renderTooltip(poseStack, k.tooltipText, mouseX, mouseY);
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
            super((int) x, (int) y, (int) width, (int) height, Component.literal(""));
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.key = keyType.getOrCreate(keyCode);
            this.setMessage(this.key.getDisplayName());
        }

        @Override
        protected void renderWidget(PoseStack poseStack, int mouseX, int mouseY, float delta) {
            int bindingCount = this.tooltipText.size();
            int color;
            if (this.active) {
                if (this.isHovered() && !keyWizardScreen.getCategorySelectorExtended()) {
                    color = 0xFFAAAAAA;
                    if (bindingCount == 1) {
                        color = 0xFF00AA00;
                    } else if (bindingCount > 1) {
                        color = 0xFFAA0000;
                    }
                } else {
                    color = 0xFFFFFFFF;
                    if (bindingCount == 1) {
                        color = 0xFF00FF00;
                    } else if (bindingCount > 1) {
                        color = 0xFFFF0000;
                    }
                }
            } else {
                color = 0xFF555555;
            }
            DrawingUtil.drawNoFillRect(poseStack, this.x, this.y, this.x + this.width, this.y + this.height, color);
            @SuppressWarnings("resource")
            Font textRenderer = Minecraft.getInstance().font;
            //textRenderer.drawWithShadow(ctx, this.getMessage(),
            //        (this.x + (this.width) / 2 - textRenderer.getWidth(this.getMessage()) / 2.0F),
            //        this.y + (this.height - 6) / 2, color);
            textRenderer.draw(poseStack, getMessage(), (int) (this.x + (this.width) / 2 - textRenderer.width(this.getMessage()) / 2.0F), (int) (this.y + (this.height - 6) / 2), color);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput builder) {

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
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            if (Screen.hasAltDown() && Screen.hasControlDown()) {
                Component t = this.getMessage();
                String keyName;
                if (t.getContents() instanceof TranslatableContents contents) {
                    keyName = I18n.get(contents.getKey());
                } else {
                    keyName = t.getString();
                }
                keyWizardScreen.setSearchText("<" + keyName + ">");
            } else {
                KeyMapping selectedKeyMapping = keyWizardScreen.getSelectedKeyMapping();
                if (selectedKeyMapping != null) {
                    selectedKeyMapping.setKeyModifierAndCode(getActiveModifier(), this.key);
                    KeyMapping.resetMapping();
                }
            }
        }

        @SuppressWarnings("resource")
        private void updateTooltip() {
            ArrayList<String> tooltipText = new ArrayList<>();
            for (KeyMapping b : Minecraft.getInstance().options.keyMappings) {
                if (b.getKey().equals(this.key)) {
                    tooltipText.add(I18n.get(b.getName()));
                }
            }
            this.tooltipText = tooltipText.stream().sorted().map(Component::literal).collect(Collectors.toCollection(ArrayList<Component>::new));
        }

        @Override
        public void tick() {
            updateTooltip();
        }

        @SuppressWarnings("removal")
        private KeyModifier getActiveModifier() {
            return KeyModifier.getActiveModifier();
        }
    }

    @Override
    public int getTabOrderGroup() {
        return 0;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
    }

}
