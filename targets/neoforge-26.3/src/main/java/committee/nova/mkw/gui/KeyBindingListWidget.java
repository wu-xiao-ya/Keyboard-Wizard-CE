package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.core.binding.BindingSearchParser;
import committee.nova.mkw.core.binding.BindingSearchQuery;
import committee.nova.mkw.core.gui.PixelKeyboardStyle;
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
import java.util.List;
import java.util.Map;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry> implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    private String currentFilterText = "";
    private String currentCategory = KeyBindingUtil.DYNAMIC_CATEGORY_ALL;

    public KeyBindingListWidget(KeyWizardScreen keyWizardScreen, int top, int left, int width, int height, int itemHeight) {
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

    private void updateList(boolean force) {
        boolean filterUpdate = !this.currentFilterText.equals(this.keyWizardScreen.getFilterText());
        boolean categoryUpdate = !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());

        if (force || categoryUpdate || filterUpdate) {
            if (categoryUpdate) this.currentCategory = this.keyWizardScreen.getSelectedCategory();

            KeyMapping[] bindings = getBindingsByCategory(this.currentCategory);

            this.currentFilterText = this.keyWizardScreen.getFilterText();
            BindingSearchQuery searchQuery = BindingSearchParser.parse(this.currentFilterText, KeyWizardScreen.KEY_FILTER_PREFIX);
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
            filtered = Arrays.stream(filtered)
                    .filter(binding -> searchQuery.matchesName(I18n.get(binding.getName())))
                    .toArray(KeyMapping[]::new);
        }
        return filtered;
    }

    private KeyMapping[] filterBindingsByKey(KeyMapping[] bindings, String keyName) {
        return Arrays.stream(bindings)
                .filter(b -> KeyBindingUtil.getKey(b).getDisplayName().getString().equalsIgnoreCase(keyName))
                .toArray(KeyMapping[]::new);
    }

    private KeyMapping[] getBindingsByCategory(String category) {
        KeyMapping[] bindings = KeyBindingUtil.getAllBindings();
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                Map<InputConstants.Key, Integer> bindingCounts = KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings)
                        .filter(b -> bindingCounts.getOrDefault(KeyBindingUtil.getKey(b), 0) > 1 && KeyBindingUtil.getKey(b).getValue() != InputConstants.UNKNOWN.getValue())
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings).filter(KeyBindingUtil::isUnbound).toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings).filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.CONTROL)).toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings).filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.ALT)).toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings).filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.SHIFT)).toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings).filter(k -> KeyBindingUtil.getModifier(k).equals(KeyModifier.NONE)).toArray(KeyMapping[]::new);
            default:
                return Arrays.stream(bindings).filter(b -> b.getCategory().label().getString().equals(Component.translatable(category).getString())).toArray(KeyMapping[]::new);
        }
    }

    void refreshSelectedBinding() {
        KeyMapping selected = getSelectedKeyMapping();
        double scroll = this.scrollAmount();
        updateList(true);
        if (selected != null) {
            for (var rawEntry : this.children()) {
                if (rawEntry instanceof BindingEntry entry && entry.keyMapping == selected) {
                    this.setSelected(entry);
                    this.setScrollAmount(scroll);
                    break;
                }
            }
        }
    }

    @Override
    public void tick() {
        updateList(false);
    }

    public class BindingEntry extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry {
        private static final int CATEGORY_RIGHT_PADDING = 4;
        private static final int CATEGORY_BOTTOM_PADDING = 3;
        private final KeyMapping keyMapping;

        public BindingEntry(KeyMapping keyMapping) {
            this.keyMapping = keyMapping;
        }

        @Override
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int contentX = this.getContentX();
            int contentY = this.getContentY();
            int maxTextWidth = this.labelWidth();
            List<Component> labels = this.labels();

            graphics.text(minecraft.font, PixelWidgets.fit(minecraft.font, labels.get(0), maxTextWidth),
                    contentX, contentY, PixelKeyboardStyle.INK, false);
            graphics.text(minecraft.font, PixelWidgets.fit(minecraft.font, labels.get(1), maxTextWidth),
                    contentX, contentY + minecraft.font.lineHeight + 5, PixelKeyboardStyle.PANEL_FOOT, false);
            var category = PixelWidgets.fit(minecraft.font, labels.get(2), maxTextWidth);
            int categoryX = contentX + maxTextWidth - minecraft.font.width(category);
            int categoryY = this.getContentBottom() - minecraft.font.lineHeight - CATEGORY_BOTTOM_PADDING;
            graphics.text(minecraft.font, category, categoryX, categoryY, PixelKeyboardStyle.PANEL_FOOT, false);
            if (hovered && !keyWizardScreen.getCategorySelectorExtended()
                    && labels.stream().anyMatch(label -> minecraft.font.width(label) > maxTextWidth)) {
                PixelWidgets.showTooltip(graphics, minecraft.font, labels, mouseX, mouseY);
            }
        }

        private List<Component> labels() {
            return List.of(Component.translatable(this.keyMapping.getName()), this.keyMapping.getTranslatedKeyMessage(),
                    Component.literal(this.getCategoryDisplayLabel(this.keyMapping)));
        }

        private int labelWidth() {
            return this.getContentWidth() - CATEGORY_RIGHT_PADDING;
        }

        private String getCategoryDisplayLabel(KeyMapping keyMapping) {
            KeyMapping.Category category = keyMapping.getCategory();
            String translatedCategory = category.label().getString();
            String modId = category.id().getNamespace();
            return ModList.get().getModContainerById(modId)
                    .map(mod -> mod.getModInfo().getDisplayName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }

    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
