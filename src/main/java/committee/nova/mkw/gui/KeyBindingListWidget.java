package committee.nova.mkw.gui;

import committee.nova.mkw.ModernKeyBinding;
import committee.nova.mkw.api.IKeyBinding;
import committee.nova.mkw.keybinding.KeyModifier;
import committee.nova.mkw.mixin.AccessorKeyBinding;
import committee.nova.mkw.util.KeyBindingUtil;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;
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
        boolean keyFilter = this.keyWizardScreen.getFilterText().startsWith(KeyWizardScreen.KEY_FILTER_PREFIX);

        if (categoryUpdate || filterUpdate) {
            if (categoryUpdate) {
                this.currentCategory = this.keyWizardScreen.getSelectedCategory();
            }

            KeyBinding[] bindings = getBindingsByCategory(keyFilter ? KeyBindingUtil.DYNAMIC_CATEGORY_ALL : this.currentCategory);

            if (filterUpdate) {
                this.currentFilterText = this.keyWizardScreen.getFilterText();
            }

            if (!this.currentFilterText.equals("")) {
                bindings = filterBindings(bindings, this.currentFilterText, keyFilter);
            }

            this.children().clear();
            if (bindings.length > 0) {
                for (KeyBinding keyBinding : bindings) {
                    this.addEntry(new BindingEntry(keyBinding));
                }
                this.setSelected(this.children().get(0));
            } else {
                this.setSelected(null);
            }
            this.setScrollAmount(0);
        }
    }

    private KeyBinding[] filterBindings(KeyBinding[] bindings, String filterText, boolean keyFilter) {
        KeyBinding[] bindingsFiltered = bindings;
        if (keyFilter) {
            filterText = filterText.substring(KeyWizardScreen.KEY_FILTER_PREFIX.length());
        }

        String keyNameRegex = "<.*>";
        Matcher keyNameMatcher = Pattern.compile(keyNameRegex).matcher(filterText);

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

    private KeyBinding[] filterBindingsByName(KeyBinding[] bindings, String bindingName) {
        String[] words = bindingName.split("\\s+");
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
            Text text = ((AccessorKeyBinding) binding).getBoundKey().getLocalizedText();
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
                Map<InputUtil.Key, Integer> bindingCounts = KeyBindingUtil.getBindingCountsByKey();
                return Arrays.stream(bindings).filter(binding -> bindingCounts.get(((AccessorKeyBinding) binding).getBoundKey()) > 1 && ((AccessorKeyBinding) binding).getBoundKey().getCode() != -1).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_UNBOUND:
                return Arrays.stream(bindings).filter(KeyBinding::isUnbound).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_CTRL:
                return Arrays.stream(bindings).filter(keyBinding -> ((IKeyBinding) keyBinding).getKeyModifier().equals(KeyModifier.CONTROL)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_ALT:
                return Arrays.stream(bindings).filter(keyBinding -> ((IKeyBinding) keyBinding).getKeyModifier().equals(KeyModifier.ALT)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_SHIFT:
                return Arrays.stream(bindings).filter(keyBinding -> ((IKeyBinding) keyBinding).getKeyModifier().equals(KeyModifier.SHIFT)).toArray(KeyBinding[]::new);
            case KeyBindingUtil.DYNAMIC_CATEGORY_NONE:
                return Arrays.stream(bindings).filter(keyBinding -> ((IKeyBinding) keyBinding).getKeyModifier().equals(KeyModifier.NONE)).toArray(KeyBinding[]::new);
            default:
                return Arrays.stream(bindings).filter(binding -> binding.getCategory().equals(category)).toArray(KeyBinding[]::new);
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
            String category = keyBinding.getCategory();
            String translatedCategory = Text.translatable(category).getString();
            if (!category.startsWith(CATEGORY_PREFIX)) {
                return translatedCategory;
            }

            String categoryPath = category.substring(CATEGORY_PREFIX.length());
            if (categoryPath.isEmpty()) {
                return translatedCategory;
            }

            String modId = categoryPath.split("\\.")[0];
            return FabricLoader.getInstance().getModContainer(modId)
                    .map(mod -> mod.getMetadata().getName() + " / " + translatedCategory)
                    .orElse(translatedCategory);
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {
    }
}
