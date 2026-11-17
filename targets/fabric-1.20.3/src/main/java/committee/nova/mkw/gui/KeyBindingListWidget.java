package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyBinding;
import committee.nova.mkw.bridge.binding.CategoryDisplayResolver;
import committee.nova.mkw.core.binding.BindingSearchParser;
import committee.nova.mkw.core.binding.BindingSearchQuery;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.util.KeyBindingUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;

public class KeyBindingListWidget extends FreeFormListWidget<KeyBindingListWidget.BindingEntry> implements TickableElement {
    public KeyWizardScreen keyWizardScreen;
    private String currentFilterText = "";
    private String currentCategory = KeyBindingUtil.DYNAMIC_CATEGORY_ALL;

    public KeyBindingListWidget(KeyWizardScreen keyWizardScreen, int top, int left, int width, int height, int itemHeight) {
        super(MinecraftClient.getInstance(), top, left, width, height, itemHeight);
        this.keyWizardScreen = keyWizardScreen;

        for (KeyBinding keyBinding : this.client.options.allKeys) {
            this.addEntry(new BindingEntry(keyBinding));
        }
        this.setSelected(this.children().get(0));
    }

    @Nullable
    public KeyBinding getSelectedKeyMapping() {
        if (this.getSelectedOrNull() == null) {
            return null;
        }
        return ((BindingEntry) this.getSelectedOrNull()).keyBinding;
    }

    private void updateList() {
        boolean filterUpdate = !this.currentFilterText.equals(this.keyWizardScreen.getFilterText());
        boolean categoryUpdate = !this.currentCategory.equals(this.keyWizardScreen.getSelectedCategory());
        BindingSearchQuery searchQuery = BindingSearchParser.parse(this.keyWizardScreen.getFilterText(), KeyWizardScreen.KEY_FILTER_PREFIX);

        if (categoryUpdate || filterUpdate) {
            if (categoryUpdate) {
                this.currentCategory = this.keyWizardScreen.getSelectedCategory();
            }

            KeyBinding[] bindings = getBindingsByCategory(searchQuery.keyFilter() ? KeyBindingUtil.DYNAMIC_CATEGORY_ALL : this.currentCategory);

            if (filterUpdate) {
                this.currentFilterText = this.keyWizardScreen.getFilterText();
                searchQuery = BindingSearchParser.parse(this.currentFilterText, KeyWizardScreen.KEY_FILTER_PREFIX);
            }

            if (!this.currentFilterText.equals("")) {
                bindings = filterBindings(bindings, searchQuery);
            }

            this.clearEntries();
            for (KeyBinding keyBinding : bindings) {
                this.addEntry(new BindingEntry(keyBinding));
            }
            this.setSelected(bindings.length > 0 ? this.children().get(0) : null);
            this.setScrollAmount(0);
        }
    }

    private KeyBinding[] filterBindings(KeyBinding[] bindings, BindingSearchQuery searchQuery) {
        KeyBinding[] bindingsFiltered = bindings;

        if (searchQuery.hasKeyNameFilter()) {
            bindingsFiltered = filterBindingsByKey(bindingsFiltered, searchQuery.keyNameFilter());
        }

        if (searchQuery.hasTextTerms()) {
            bindingsFiltered = filterBindingsByName(bindingsFiltered, searchQuery.textTerms().toArray(String[]::new));
        }

        return bindingsFiltered;
    }

    private KeyBinding[] filterBindingsByName(KeyBinding[] bindings, String[] words) {
        return Arrays.stream(bindings).filter(binding -> {
            boolean flag = true;
            for (String word : words) {
                flag = flag && I18n.translate(binding.getTranslationKey()).toLowerCase().contains(word.toLowerCase());
            }
            return flag;
        }).toArray(KeyBinding[]::new);
    }

    private KeyBinding[] filterBindingsByKey(KeyBinding[] bindings, String keyName) {
        return Arrays.stream(bindings).filter(binding -> {
            Text text = KeyBindingUtil.getKey(binding).getLocalizedText();
            if (text.getContent() instanceof TranslatableTextContent contents) {
                return I18n.translate(contents.getKey()).equalsIgnoreCase(keyName);
            } else {
                return text.getString().equalsIgnoreCase(keyName);
            }
        }).toArray(KeyBinding[]::new);
    }

    private KeyBinding[] getBindingsByCategory(String category) {
        KeyBinding[] bindings = Arrays.copyOf(this.client.options.allKeys, this.client.options.allKeys.length);
        switch (category) {
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALL:
                return bindings;
            case KeyBindingUtil.DYNAMIC_CATEGORY_CONFLICTS:
                if (ModernKeyBinding.nonConflictKeys()) {
                    return new KeyBinding[0];
                }
                return Arrays.stream(bindings).filter(binding -> KeyBindingUtil.getBindingCountsByKey().get(KeyBindingUtil.getKey(binding)) > 1 && KeyBindingUtil.getKey(binding).getCode() != -1).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings).filter(KeyBindingUtil::isUnbound).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings).filter(keyBinding -> KeyBindingUtil.getModifier(keyBinding).equals(KeyModifier.CONTROL)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings).filter(keyBinding -> KeyBindingUtil.getModifier(keyBinding).equals(KeyModifier.ALT)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings).filter(keyBinding -> KeyBindingUtil.getModifier(keyBinding).equals(KeyModifier.SHIFT)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings).filter(keyBinding -> KeyBindingUtil.getModifier(keyBinding).equals(KeyModifier.NONE)).toArray(KeyBinding[]::new);
            default:
                return Arrays.stream(bindings).filter(binding -> binding.getCategory().equals(category)).toArray(KeyBinding[]::new);
        }
    }

    public void refreshSelectedBinding() {
        KeyBinding selected = getSelectedKeyMapping();
        this.currentFilterText = "\u0000";
        updateList();
        if (selected != null) {
            for (FreeFormListWidget<KeyBindingListWidget.BindingEntry>.Entry rawEntry : this.children()) {
                if (rawEntry instanceof BindingEntry entry && entry.keyBinding == selected) {
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
        private static final int CATEGORY_RIGHT_PADDING = 4;
        private static final int CATEGORY_BOTTOM_PADDING = 3;
        private final KeyBinding keyBinding;

        public BindingEntry(KeyBinding keyBinding) {
            this.keyBinding = keyBinding;
        }

        @Override
        public void render(DrawContext ctx, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            int contentRight = x + entryWidth - CATEGORY_RIGHT_PADDING;
            int maxTextWidth = Math.max(0, contentRight - x);

            ctx.drawTextWithShadow(client.textRenderer, trimToWidth(Text.translatable(this.keyBinding.getTranslationKey()), maxTextWidth), x, y, 0xFFFFFFFF);
            int color = 0xFF999999;
            ctx.drawTextWithShadow(client.textRenderer, trimToWidth(this.keyBinding.getBoundKeyLocalizedText(), maxTextWidth), x, y + client.textRenderer.fontHeight + 5, color);
            String categoryLabel = getCategoryDisplayLabel(this.keyBinding);
            if (maxTextWidth > 0 && !categoryLabel.isEmpty()) {
                String clippedCategoryLabel = client.textRenderer.trimToWidth(categoryLabel, maxTextWidth);
                int categoryX = Math.max(x, contentRight - client.textRenderer.getWidth(clippedCategoryLabel));
                int categoryY = y + entryHeight - client.textRenderer.fontHeight - CATEGORY_BOTTOM_PADDING;
                ctx.drawTextWithShadow(client.textRenderer, clippedCategoryLabel, categoryX, categoryY, 0xFF7F7F7F);
            }
        }

        private Text trimToWidth(Text text, int width) {
            if (width <= 0) {
                return Text.empty();
            }
            return Text.literal(client.textRenderer.trimToWidth(text.getString(), width));
        }

        private String getCategoryDisplayLabel(KeyBinding keyBinding) {
            return CategoryDisplayResolver.resolve(keyBinding.getCategory()).getString();
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
