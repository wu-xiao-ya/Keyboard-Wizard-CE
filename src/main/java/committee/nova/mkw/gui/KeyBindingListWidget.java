package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.util.KeyBindingUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry> implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    private String currentFilterText = "";
    private String currentCategory = KeyBindingUtil.DYNAMIC_CATEGORY_ALL;

    public KeyBindingListWidget(KeyWizardScreen keyWizardScreen, int top, int left, int width, int height, int itemHeight) {
        super(Minecraft.getInstance(), top, left, width, height, itemHeight);
        this.keyWizardScreen = keyWizardScreen;

        for (KeyMapping k : this.minecraft.options.keyMappings) {
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
        boolean categoryUpdate = !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());
        boolean keyFilter = this.keyWizardScreen.getFilterText().startsWith(KeyWizardScreen.KEY_FILTER_PREFIX);

        if (categoryUpdate || filterUpdate) {
            if (categoryUpdate) this.currentCategory = this.keyWizardScreen.getSelectedCategory();

            KeyMapping[] bindings = getBindingsByCategory(keyFilter ? KeyBindingUtil.DYNAMIC_CATEGORY_ALL : this.currentCategory);

            if (filterUpdate) {
                this.currentFilterText = this.keyWizardScreen.getFilterText();
            }

            if (!this.currentFilterText.equals("")) {
                bindings = filterBindings(bindings, this.currentFilterText, keyFilter);
            }

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

    private KeyMapping[] filterBindings(KeyMapping[] bindings, String filterText, boolean keyFilter) {
        KeyMapping[] bindingsFiltered = bindings;
        if (keyFilter) {
            filterText = filterText.substring(KeyWizardScreen.KEY_FILTER_PREFIX.length());
        }

        Matcher keyNameMatcher = Pattern.compile("<.*>").matcher(filterText);

        if (keyNameMatcher.find()) {
            String keyNameWithBrackets = keyNameMatcher.group();
            String keyName = keyNameWithBrackets.replace("<", "").replace(">", "");
            filterText = filterText.replace(keyNameWithBrackets, "");
            bindingsFiltered = filterBindingsByKey(bindingsFiltered, keyName);
        }

        if (!filterText.equals("")) {
            bindingsFiltered = filterBindingsByName(bindingsFiltered, filterText);
        }

        return bindingsFiltered;
    }

    private KeyMapping[] filterBindingsByName(KeyMapping[] bindings, String bindingName) {
        String[] words = bindingName.split("\\s+");
        return Arrays.stream(bindings).filter(binding -> {
            boolean flag = true;
            for (String w : words) {
                flag = flag && I18n.get(binding.getName()).toLowerCase().contains(w.toLowerCase());
            }
            return flag;
        }).toArray(KeyMapping[]::new);
    }

    private KeyMapping[] filterBindingsByKey(KeyMapping[] bindings, String keyName) {
        return Arrays.stream(bindings)
                .filter(b -> KeyBindingUtil.getKey(b).getDisplayName().getString().equalsIgnoreCase(keyName))
                .toArray(KeyMapping[]::new);
    }

    private KeyMapping[] getBindingsByCategory(String category) {
        KeyMapping[] bindings = Arrays.copyOf(this.minecraft.options.keyMappings, this.minecraft.options.keyMappings.length);
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                Map<InputConstants.Key, Integer> bindingCounts = KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings)
                        .filter(b -> bindingCounts.getOrDefault(KeyBindingUtil.getKey(b), 0) > 1 && KeyBindingUtil.getKey(b).getValue() != InputConstants.UNKNOWN.getValue())
                        .toArray(KeyMapping[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings).filter(KeyMapping::isUnbound).toArray(KeyMapping[]::new);
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

    public void refreshSelectedBinding() {
        KeyMapping selected = getSelectedKeyMapping();
        this.currentFilterText = "\u0000";
        updateList();
        if (selected != null) {
            for (FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry rawEntry : this.children()) {
                if (rawEntry instanceof BindingEntry entry && entry.keyMapping == selected) {
                    this.setSelected(entry);
                    break;
                }
            }
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
        public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int contentX = this.getContentX();
            int contentY = this.getContentY();
            int contentRight = KeyBindingListWidget.this.getRowRight() - 6;
            int maxTextWidth = Math.max(0, contentRight - contentX);

            graphics.text(minecraft.font, trimToWidth(Component.translatable(this.keyMapping.getName()), maxTextWidth), contentX, contentY, 0xFFFFFFFF);
            graphics.text(minecraft.font, trimToWidth(this.keyMapping.getTranslatedKeyMessage(), maxTextWidth), contentX, contentY + minecraft.font.lineHeight + 5, 0xFF999999);

            String categoryLabel = getCategoryDisplayLabel(this.keyMapping);
            if (!categoryLabel.isEmpty()) {
                String trimmedCategory = minecraft.font.plainSubstrByWidth(categoryLabel, maxTextWidth);
                int categoryWidth = minecraft.font.width(trimmedCategory);
                int categoryX = Math.max(contentX, contentRight - CATEGORY_RIGHT_PADDING - categoryWidth);
                int categoryY = this.getContentY() + this.getContentHeight() - minecraft.font.lineHeight - CATEGORY_BOTTOM_PADDING;
                graphics.text(minecraft.font, trimmedCategory, categoryX, categoryY, 0xFF808080);
            }
        }

        private String getCategoryDisplayLabel(KeyMapping keyMapping) {
            KeyMapping.Category category = keyMapping.getCategory();
            String translatedCategory = category.label().getString();
            String modId = category.id().getNamespace();
            return FabricLoader.getInstance().getModContainer(modId)
                    .map(mod -> mod.getMetadata().getName() + " / " + translatedCategory)
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
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
