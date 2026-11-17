package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;

import committee.nova.mkw.core.binding.BindingSearchParser;
import committee.nova.mkw.core.binding.BindingSearchQuery;
import committee.nova.mkw.util.KeyBindingUtil;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.settings.KeyModifier;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>
        implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    private String currentFilterText = "";
    private String currentCategory = KeyBindingUtil.DYNAMIC_CATEGORY_ALL;

    public KeyBindingListWidget(
            KeyWizardScreen keyWizardScreen,
            int top,
            int left,
            int width,
            int height,
            int itemHeight) {
        super(Minecraft.getInstance(), top, left, width, height, itemHeight);
        this.keyWizardScreen = keyWizardScreen;

        for (KeyMapping k : KeyBindingUtil.getAllBindings()) {
            this.addEntry(new BindingEntry(k));
        }
        if (!this.children().isEmpty()) {
            this.setSelected(this.children().get(0));
        }
    }

    @Nullable
    public KeyMapping getSelectedKeyMapping() {
        if (this.getSelected() == null) {
            return null;
        }
        return ((BindingEntry) this.getSelected()).keyMapping;
    }

    private void updateList() {
        boolean filterUpdate = !this.currentFilterText.equals(this.keyWizardScreen.getFilterText());
        boolean categoryUpdate =
                !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());

        if (categoryUpdate || filterUpdate) {
            if (categoryUpdate) this.currentCategory = this.keyWizardScreen.getSelectedCategory();

            KeyMapping[] bindings = getBindingsByCategory(this.currentCategory);

            this.currentFilterText = this.keyWizardScreen.getFilterText();
            BindingSearchQuery searchQuery =
                    BindingSearchParser.parse(
                            this.currentFilterText, KeyWizardScreen.KEY_FILTER_PREFIX);
            bindings = filterBindings(bindings, searchQuery);

            this.clearEntries();
            if (bindings.length > 0) {
                for (KeyMapping k : bindings) this.addEntry(new BindingEntry(k));
                this.setSelected(this.children().get(0));
            } else {
                this.setSelected(null);
            }
            this.setScrollAmount(0);
        }
    }

    private KeyMapping[] filterBindings(KeyMapping[] bindings, BindingSearchQuery searchQuery) {
        KeyMapping[] filtered = bindings;
        if (searchQuery.hasKeyNameFilter()) {
            filtered = filterBindingsByKey(filtered, searchQuery.keyNameFilter());
        }
        if (searchQuery.hasTextTerms()) {
            filtered =
                    Arrays.stream(filtered)
                            .filter(binding -> searchQuery.matchesName(I18n.get(binding.getName())))
                            .toArray(KeyMapping[]::new);
        }
        return filtered;
    }

    private KeyMapping[] filterBindingsByKey(KeyMapping[] bindings, String keyName) {
        return Arrays.stream(bindings)
                .filter(
                        b ->
                                KeyBindingUtil.getKey(b)
                                        .getDisplayName()
                                        .getString()
                                        .equalsIgnoreCase(keyName))
                .toArray(KeyMapping[]::new);
    }

    private KeyMapping[] getBindingsByCategory(String category) {
        KeyMapping[] bindings = KeyBindingUtil.getAllBindings();
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                Map<InputConstants.Key, Integer> bindingCounts =
                        KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings)
                        .filter(
                                b ->
                                        bindingCounts.getOrDefault(KeyBindingUtil.getKey(b), 0) > 1
                                                && KeyBindingUtil.getKey(b).getValue()
                                                        != InputConstants.UNKNOWN.getValue())
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings)
                        .filter(KeyBindingUtil::isUnbound)
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings)
                        .filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.CONTROL))
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings)
                        .filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.ALT))
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings)
                        .filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.SHIFT))
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings)
                        .filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.NONE))
                        .toArray(KeyMapping[]::new);
            default:
                return Arrays.stream(bindings)
                        .filter(
                                b ->
                                        b.getCategory()
                                                .label()
                                                .getString()
                                                .equals(
                                                        Component.translatable(category)
                                                                .getString()))
                        .toArray(KeyMapping[]::new);
        }
    }

    @Override
    public void tick() {
        updateList();
    }

    public class BindingEntry extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry {
        private static final int CATEGORY_RIGHT_PADDING = 6;
        private static final int CATEGORY_BOTTOM_PADDING = 3;
        private final KeyMapping keyMapping;

        public BindingEntry(KeyMapping keyMapping) {
            this.keyMapping = keyMapping;
        }

        @Override
        public void extractContent(
                GuiGraphicsExtractor graphics,
                int mouseX,
                int mouseY,
                boolean hovered,
                float tickDelta) {
            int contentX = this.getContentX();
            int contentY = this.getContentY();
            int contentRight = KeyBindingListWidget.this.getRowRight() - 6;
            int maxTextWidth = Math.max(0, contentRight - contentX);

            graphics.text(
                    minecraft.font,
                    trimToWidth(Component.translatable(this.keyMapping.getName()), maxTextWidth),
                    contentX,
                    contentY,
                    0xFF202020);
            graphics.text(
                    minecraft.font,
                    trimToWidth(this.keyMapping.getTranslatedKeyMessage(), maxTextWidth),
                    contentX,
                    contentY + minecraft.font.lineHeight + 5,
                    0xFF999999);

            String categoryLabel = getCategoryDisplayLabel(this.keyMapping);
            if (!categoryLabel.isEmpty()) {
                String trimmedCategory =
                        minecraft.font.plainSubstrByWidth(categoryLabel, maxTextWidth);
                int categoryWidth = minecraft.font.width(trimmedCategory);
                int categoryX =
                        Math.max(contentX, contentRight - CATEGORY_RIGHT_PADDING - categoryWidth);
                int categoryY =
                        this.getContentY()
                                + this.getContentHeight()
                                - minecraft.font.lineHeight
                                - CATEGORY_BOTTOM_PADDING;
                graphics.text(minecraft.font, trimmedCategory, categoryX, categoryY, 0xFF808080);
            }
        }

        private String getCategoryDisplayLabel(KeyMapping keyMapping) {
            KeyMapping.Category category = keyMapping.getCategory();
            String translatedCategory = category.label().getString();
            String modId = category.id().getNamespace();
            return ModList.get()
                    .getModContainerById(modId)
                    .map(mod -> mod.getModInfo().getDisplayName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }

        private Component trimToWidth(Component component, int width) {
            if (width <= 0) {
                return Component.empty();
            }
            String text = component.getString();
            return Component.literal(minecraft.font.plainSubstrByWidth(text, width));
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {}
}
