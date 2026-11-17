package committee.nova.mkw.gui;

import com.mojang.blaze3d.matrix.MatrixStack;

import committee.nova.mkw.core.binding.BindingSearchParser;
import committee.nova.mkw.core.binding.BindingSearchQuery;
import committee.nova.mkw.util.KeyBindingUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.ModList;

import java.util.Arrays;
import java.util.Map;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>
        implements TickableElement {
    private static final String CATEGORY_PREFIX = "key.categories.";
    private static final int CATEGORY_RIGHT_PADDING = 4;
    private static final int CATEGORY_BOTTOM_PADDING = 3;

    private final KeyWizardScreen keyWizardScreen;
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

        for (KeyBinding binding : KeyBindingUtil.getVisibleBindings()) {
            this.addEntry(new BindingEntry(binding));
        }
        if (!this.children().isEmpty()) {
            this.setSelected(this.children().get(0));
        }
    }

    public KeyBinding getSelectedKeyMapping() {
        return this.getSelected() == null ? null : ((BindingEntry) this.getSelected()).keyBinding;
    }

    @Override
    public void tick() {
        updateList();
    }

    private void updateList() {
        boolean filterUpdate = !this.currentFilterText.equals(this.keyWizardScreen.getFilterText());
        boolean categoryUpdate =
                !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());

        if (!(categoryUpdate || filterUpdate)) {
            return;
        }

        if (categoryUpdate) {
            this.currentCategory = this.keyWizardScreen.getSelectedCategory();
        }

        this.currentFilterText = this.keyWizardScreen.getFilterText();
        BindingSearchQuery searchQuery =
                BindingSearchParser.parse(
                        this.currentFilterText, KeyWizardScreen.KEY_FILTER_PREFIX);
        KeyBinding[] bindings =
                getBindingsByCategory(
                        searchQuery.keyFilter()
                                ? KeyBindingUtil.DYNAMIC_CATEGORY_ALL
                                : this.currentCategory);
        bindings = filterBindings(bindings, searchQuery);

        this.clearEntries();
        for (KeyBinding binding : bindings) {
            this.addEntry(new BindingEntry(binding));
        }
        this.setSelected(this.children().isEmpty() ? null : this.children().get(0));
        this.setScrollAmount(0);
    }

    private KeyBinding[] filterBindings(KeyBinding[] bindings, BindingSearchQuery searchQuery) {
        KeyBinding[] filtered = bindings;

        if (searchQuery.hasKeyNameFilter()) {
            filtered = filterBindingsByKey(filtered, searchQuery.keyNameFilter());
        }

        if (searchQuery.hasTextTerms()) {
            filtered =
                    Arrays.stream(filtered)
                            .filter(binding -> searchQuery.matchesName(I18n.get(binding.getName())))
                            .toArray(KeyBinding[]::new);
        }

        return filtered;
    }

    private KeyBinding[] filterBindingsByKey(KeyBinding[] bindings, String keyName) {
        return Arrays.stream(bindings)
                .filter(
                        binding ->
                                binding.getTranslatedKeyMessage()
                                        .getString()
                                        .equalsIgnoreCase(keyName))
                .toArray(KeyBinding[]::new);
    }

    private KeyBinding[] getBindingsByCategory(String category) {
        KeyBinding[] bindings = KeyBindingUtil.getVisibleBindings();
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                Map<InputMappings.Input, Integer> bindingCounts =
                        KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings)
                        .filter(
                                binding ->
                                        bindingCounts.getOrDefault(
                                                                KeyBindingUtil.getKey(binding), 0)
                                                        > 1
                                                && !KeyBindingUtil.isUnbound(binding))
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings)
                        .filter(KeyBindingUtil::isUnbound)
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings)
                        .filter(
                                binding ->
                                        KeyBindingUtil.isModifierActive(
                                                KeyModifier.CONTROL, binding))
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings)
                        .filter(
                                binding ->
                                        KeyBindingUtil.isModifierActive(KeyModifier.ALT, binding))
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings)
                        .filter(
                                binding ->
                                        KeyBindingUtil.isModifierActive(KeyModifier.SHIFT, binding))
                        .toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings)
                        .filter(
                                binding ->
                                        KeyBindingUtil.isModifierActive(KeyModifier.NONE, binding))
                        .toArray(KeyBinding[]::new);
            default:
                return Arrays.stream(bindings)
                        .filter(binding -> binding.getCategory().equals(category))
                        .toArray(KeyBinding[]::new);
        }
    }

    public class BindingEntry extends FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry {
        private final KeyBinding keyBinding;

        public BindingEntry(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
        }

        @Override
        public void render(
                MatrixStack matrixStack,
                int index,
                int y,
                int x,
                int entryWidth,
                int entryHeight,
                int mouseX,
                int mouseY,
                boolean hovered,
                float tickDelta) {
            minecraft.font.draw(
                    matrixStack,
                    new TranslationTextComponent(this.keyBinding.getName()),
                    x,
                    y,
                    0xFF202020);
            minecraft.font.draw(
                    matrixStack,
                    this.keyBinding.getTranslatedKeyMessage(),
                    x,
                    y + minecraft.font.lineHeight + 5,
                    0xFF999999);

            String categoryLabel = getCategoryDisplayLabel(this.keyBinding);
            int maxCategoryWidth = entryWidth - CATEGORY_RIGHT_PADDING * 2;
            if (maxCategoryWidth <= 0 || categoryLabel.isEmpty()) {
                return;
            }

            String clippedCategoryLabel =
                    minecraft.font.plainSubstrByWidth(categoryLabel, maxCategoryWidth);
            int categoryX =
                    x
                            + entryWidth
                            - CATEGORY_RIGHT_PADDING
                            - minecraft.font.width(clippedCategoryLabel);
            int categoryY = y + entryHeight - minecraft.font.lineHeight - CATEGORY_BOTTOM_PADDING;
            minecraft.font.draw(
                    matrixStack, clippedCategoryLabel, categoryX, categoryY, 0xFF7F7F7F);
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
            return ModList.get()
                    .getModContainerById(modId)
                    .map(mod -> mod.getModInfo().getDisplayName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }
    }
}
