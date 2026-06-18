package committee.nova.mkw.gui;

import com.mojang.blaze3d.platform.InputConstants;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.settings.KeyModifier;
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
                return Arrays.stream(bindings).filter(b -> b.getCategory().equals(category)).toArray(KeyMapping[]::new);
        }
    }

    @Override
    public void tick() {
        updateList();
    }

    public class BindingEntry extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry {
        private static final String CATEGORY_PREFIX = "key.categories.";
        private static final int CATEGORY_RIGHT_PADDING = 4;
        private static final int CATEGORY_BOTTOM_PADDING = 3;
        private final KeyMapping keyMapping;

        public BindingEntry(KeyMapping keyMapping) {
            this.keyMapping = keyMapping;
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            graphics.drawString(minecraft.font, Component.translatable(this.keyMapping.getName()), x, y, 0xFFFFFFFF);
            graphics.drawString(minecraft.font, this.keyMapping.getTranslatedKeyMessage(), x, y + minecraft.font.lineHeight + 5, 0xFF999999);
            String categoryText = getCategoryDisplayLabel(this.keyMapping);
            int maxCategoryWidth = entryWidth - CATEGORY_RIGHT_PADDING * 2;
            if (maxCategoryWidth > 0 && !categoryText.isEmpty()) {
                String clippedCategoryText = minecraft.font.plainSubstrByWidth(categoryText, maxCategoryWidth);
                int categoryX = x + entryWidth - CATEGORY_RIGHT_PADDING - minecraft.font.width(clippedCategoryText);
                int categoryY = y + entryHeight - minecraft.font.lineHeight - CATEGORY_BOTTOM_PADDING;
                graphics.drawString(minecraft.font, clippedCategoryText, categoryX, categoryY, 0xFF808080);
            }
        }

        private String getCategoryDisplayLabel(KeyMapping keyMapping) {
            String category = keyMapping.getCategory();
            String translatedCategory = Component.translatable(category).getString();
            if (category.startsWith(CATEGORY_PREFIX)) {
                return getCategoryDisplayLabel(category.substring(CATEGORY_PREFIX.length()), translatedCategory);
            }

            return translatedCategory;
        }

        private String getCategoryDisplayLabel(String categoryPath, String translatedCategory) {
            int separatorIndex = categoryPath.indexOf('.');
            String modId = separatorIndex > 0 ? categoryPath.substring(0, separatorIndex) : categoryPath;
            return ModList.get().getModContainerById(modId)
                    .map(mod -> mod.getModInfo().getDisplayName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }
}
