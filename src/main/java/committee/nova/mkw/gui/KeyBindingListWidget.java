package committee.nova.mkw.gui;

import committee.nova.mkw.util.KeyBindingUtil;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry> implements TickableElement {
    private static final String CATEGORY_PREFIX = "key.categories.";
    private static final int CATEGORY_RIGHT_PADDING = 4;
    private static final int CATEGORY_BOTTOM_PADDING = 3;

    private final KeyWizardScreen keyWizardScreen;
    private String currentFilterText = "";
    private String currentCategory = KeyBindingUtil.DYNAMIC_CATEGORY_ALL;

    public KeyBindingListWidget(KeyWizardScreen keyWizardScreen, int top, int left, int width, int height, int itemHeight) {
        super(Minecraft.getInstance(), top, left, width, height, itemHeight);
        this.keyWizardScreen = keyWizardScreen;

        for (KeyBinding binding : this.minecraft.options.keyMappings) {
            this.addEntry(new BindingEntry(binding));
        }
        if (!this.children().isEmpty()) {
            this.setSelected(this.children().get(0));
        }
    }

    public KeyBinding getSelectedKeyMapping() {
        return this.getSelected() == null ? null : this.getSelected().keyBinding;
    }

    @Override
    public void tick() {
        updateList();
    }

    private void updateList() {
        boolean filterUpdate = !this.currentFilterText.equals(this.keyWizardScreen.getFilterText());
        boolean categoryUpdate = !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());
        boolean keyFilter = this.keyWizardScreen.getFilterText().startsWith(KeyWizardScreen.KEY_FILTER_PREFIX);

        if (!(categoryUpdate || filterUpdate)) {
            return;
        }

        if (categoryUpdate) {
            this.currentCategory = this.keyWizardScreen.getSelectedCategory();
        }

        KeyBinding[] bindings = getBindingsByCategory(keyFilter ? KeyBindingUtil.DYNAMIC_CATEGORY_ALL : this.currentCategory);

        if (filterUpdate) {
            this.currentFilterText = this.keyWizardScreen.getFilterText();
        }

        if (!this.currentFilterText.isEmpty()) {
            bindings = filterBindings(bindings, this.currentFilterText, keyFilter);
        }

        this.clearEntries();
        for (KeyBinding binding : bindings) {
            this.addEntry(new BindingEntry(binding));
        }
        this.setSelected(this.children().isEmpty() ? null : this.children().get(0));
        this.setScrollAmount(0);
    }

    private KeyBinding[] filterBindings(KeyBinding[] bindings, String filterText, boolean keyFilter) {
        KeyBinding[] filtered = bindings;
        String searchText = keyFilter ? filterText.substring(KeyWizardScreen.KEY_FILTER_PREFIX.length()) : filterText;

        Matcher keyNameMatcher = Pattern.compile("<.*>").matcher(searchText);
        if (keyNameMatcher.find()) {
            String keyNameWithBrackets = keyNameMatcher.group();
            String keyName = keyNameWithBrackets.substring(1, keyNameWithBrackets.length() - 1);
            searchText = searchText.replace(keyNameWithBrackets, "");
            filtered = filterBindingsByKey(filtered, keyName);
        }

        if (!searchText.isEmpty()) {
            filtered = filterBindingsByName(filtered, searchText);
        }

        return filtered;
    }

    private KeyBinding[] filterBindingsByName(KeyBinding[] bindings, String bindingName) {
        String[] words = bindingName.split("\\s+");
        return Arrays.stream(bindings).filter(binding -> {
            for (String word : words) {
                if (!I18n.get(binding.getName()).toLowerCase().contains(word.toLowerCase())) {
                    return false;
                }
            }
            return true;
        }).toArray(KeyBinding[]::new);
    }

    private KeyBinding[] filterBindingsByKey(KeyBinding[] bindings, String keyName) {
        return Arrays.stream(bindings).filter(binding -> binding.getTranslatedKeyMessage().getString().equalsIgnoreCase(keyName)).toArray(KeyBinding[]::new);
    }

    private KeyBinding[] getBindingsByCategory(String category) {
        KeyBinding[] bindings = Arrays.copyOf(this.minecraft.options.keyMappings, this.minecraft.options.keyMappings.length);
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                Map<InputMappings.Input, Integer> bindingCounts = KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings)
                        .filter(binding -> bindingCounts.getOrDefault(binding.getKey(), 0) > 1 && binding.getKey() != InputMappings.UNKNOWN)
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings).filter(binding -> binding.getKey() == InputMappings.UNKNOWN).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings).filter(binding -> KeyBindingUtil.isModifierActive(KeyModifier.CONTROL, binding)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings).filter(binding -> KeyBindingUtil.isModifierActive(KeyModifier.ALT, binding)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings).filter(binding -> KeyBindingUtil.isModifierActive(KeyModifier.SHIFT, binding)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings).filter(binding -> KeyBindingUtil.isModifierActive(KeyModifier.NONE, binding)).toArray(KeyBinding[]::new);
            default:
                return Arrays.stream(bindings).filter(binding -> binding.getCategory().equals(category)).toArray(KeyBinding[]::new);
        }
    }

    public class BindingEntry extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry {
        private final KeyBinding keyBinding;

        public BindingEntry(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
        }

        @Override
        public void render(MatrixStack matrixStack, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            minecraft.font.draw(matrixStack, new TranslationTextComponent(this.keyBinding.getName()), x, y, 0xFFFFFFFF);
            minecraft.font.draw(matrixStack, this.keyBinding.getTranslatedKeyMessage(), x, y + minecraft.font.lineHeight + 5, 0xFF999999);

            String categoryLabel = getCategoryDisplayLabel(this.keyBinding);
            int maxCategoryWidth = entryWidth - CATEGORY_RIGHT_PADDING * 2;
            if (maxCategoryWidth <= 0 || categoryLabel.isEmpty()) {
                return;
            }

            String clippedCategoryLabel = minecraft.font.plainSubstrByWidth(categoryLabel, maxCategoryWidth);
            int categoryX = x + entryWidth - CATEGORY_RIGHT_PADDING - minecraft.font.width(clippedCategoryLabel);
            int categoryY = y + entryHeight - minecraft.font.lineHeight - CATEGORY_BOTTOM_PADDING;
            minecraft.font.draw(matrixStack, clippedCategoryLabel, categoryX, categoryY, 0xFF7F7F7F);
        }

        private String getCategoryDisplayLabel(KeyBinding keyBinding) {
            String category = keyBinding.getCategory();
            String translatedCategory = new TranslationTextComponent(category).getString();
            if (!category.startsWith(CATEGORY_PREFIX)) {
                return translatedCategory;
            }

            String categoryPath = category.substring(CATEGORY_PREFIX.length());
            if (categoryPath.isEmpty()) {
                return translatedCategory;
            }

            String modId = categoryPath.split("\\.")[0];
            return ModList.get().getModContainerById(modId)
                    .map(mod -> mod.getModInfo().getDisplayName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }
    }
}
