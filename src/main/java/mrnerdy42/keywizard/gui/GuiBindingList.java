package mrnerdy42.keywizard.gui;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mrnerdy42.keywizard.util.KeybindUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiBindingList extends GuiScrollingList {
	
	private GuiKeyWizard parent;
	private KeyBinding[] bindings;
	
	private String searchText;
	private String selectedCategory;
	
	private KeyBinding selectedKeybind;
	private int selectedKeybindId;
	private static final String CATEGORY_PREFIX = "key.categories.";

	public GuiBindingList(GuiKeyWizard parent, int left, int bottom, int width, int height, int entryHeight) {
		     //Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight
		super(parent.getClient(), width, height, bottom - height, bottom, left, entryHeight, parent.width, parent.height);
		
		this.parent = parent;
		this.bindings = Arrays.copyOf(KeybindUtils.ALL_BINDINGS, KeybindUtils.ALL_BINDINGS.length);
		this.searchText = this.parent.getSearchText();
		this.selectedCategory = this.parent.getSelectedCategory();
		if (this.bindings.length > 0) {
			this.selectKeybind(0);
		} else {
			this.clearSelection();
		}
	}

	@Override
	protected int getSize() {
		return bindings.length;
	}

	@Override
	protected void elementClicked(int index, boolean doubleClick) {
		this.selectKeybind(index);
	}

	@Override
	protected boolean isSelected(int index) {
		return this.selectedKeybindId == index;
	}

	@Override
	protected void drawBackground() {
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		FontRenderer fontRender = this.parent.getFontRenderer();
		KeyBinding currentBinding = this.bindings[slotIdx];
		
		fontRender.drawStringWithShadow(I18n.format(currentBinding.getKeyDescription()), this.left + 3 , slotTop, 0xFFFFFF);

		int keyColor = getBindingColor(currentBinding);
		fontRender.drawStringWithShadow(currentBinding.getDisplayName(), this.left + 3, slotTop + fontRender.FONT_HEIGHT + 2, keyColor);

		String categoryLabel = getCategoryDisplayLabel(currentBinding);
		int maxLabelWidth = this.listWidth - 6;
		if (!categoryLabel.isEmpty() && maxLabelWidth > 0) {
			String clipped = fontRender.trimStringToWidth(categoryLabel, maxLabelWidth);
			int labelWidth = fontRender.getStringWidth(clipped);
			int labelX = this.left + this.listWidth - 3 - labelWidth;
			int labelY = slotTop + fontRender.FONT_HEIGHT * 2 + 3;
			fontRender.drawStringWithShadow(clipped, labelX, labelY, 0xFF7F7F7F);
		}
	}
	
	protected void updateList(){
		if ( !this.searchText.equals(this.parent.getSearchText()) || !this.selectedCategory.equals(this.parent.getSelectedCategory()) ) {
			this.searchText = this.parent.getSearchText();
			this.selectedCategory = this.parent.getSelectedCategory();
			boolean keyFilter = this.searchText.startsWith(GuiKeyWizard.KEY_FILTER_PREFIX);
			KeyBinding[] bindingsNew = bindingsByCategory(keyFilter ? "categories.all" : this.selectedCategory);
			String[] words = this.searchText.split("\\s+");

			if (keyFilter) {
				bindingsNew = filterBindingsBySearch(bindingsNew, this.searchText.substring(GuiKeyWizard.KEY_FILTER_PREFIX.length()));
			} else if (words.length != 0) {
				if (words[0].length()>0 && words[0].charAt(0) == '@') {
					bindingsNew = filterBindingsByKey(bindingsNew, words[0].substring(1, words[0].length()));
					words[0] = "";
				}
				bindingsNew = filterBindingsByName(bindingsNew, words);
			}
			
			this.bindings = bindingsNew;
			
			if (this.bindings.length != 0) {
				this.selectKeybind(0);
			} else {
				this.clearSelection();
			}
		}
	}

	private void clearSelection() {
		this.selectedKeybindId = -1;
		this.selectedKeybind = null;
		this.parent.setSelectedKeybind(null);
	}
	
	private void selectKeybind(int id){
		this.selectedKeybindId = id;
		this.selectedKeybind = this.bindings[id];
		this.parent.setSelectedKeybind(this.selectedKeybind);
	}

	private int getBindingColor(KeyBinding binding) {
		if (binding.getKeyCode() == 0) {
			return 0xFF555555;
		}
		int conflicts = KeybindUtils.getNumConficts(binding);
		if (conflicts > 0) {
			return 0xFFFF0000;
		}
		return binding.isSetToDefaultValue() ? 0xFFFFFFFF : 0xFF00FF00;
	}

	private String getCategoryDisplayLabel(KeyBinding binding) {
		String category = binding.getKeyCategory();
		String translatedCategory = I18n.format(category);
		if (!category.startsWith(CATEGORY_PREFIX)) {
			return translatedCategory;
		}
		String categoryPath = category.substring(CATEGORY_PREFIX.length());
		if (categoryPath.isEmpty()) {
			return translatedCategory;
		}
		String modId = categoryPath.split("\\.")[0];
		String modName = Loader.instance().getIndexedModList().get(modId) == null ? null : Loader.instance().getIndexedModList().get(modId).getName();
		return modName == null ? translatedCategory : modName + " / " + translatedCategory;
	}
	
	private KeyBinding[] bindingsByCategory(String category) {
		KeyBinding[] bindings = Arrays.copyOf(KeybindUtils.ALL_BINDINGS, KeybindUtils.ALL_BINDINGS.length);
		
		switch (category) {
		case "categories.all":
			return bindings;
		case "categories.conflicts":
			return Arrays.stream(bindings).filter(binding -> KeybindUtils.getNumConficts(binding) >= 1 && binding.getKeyCode() != 0).toArray(KeyBinding[]::new);
		case "categories.unbound":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyCode() == 0).toArray(KeyBinding[]::new);
		case "categories.ctrl":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyModifier() == KeyModifier.CONTROL).toArray(KeyBinding[]::new);
		case "categories.alt":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyModifier() == KeyModifier.ALT).toArray(KeyBinding[]::new);
		case "categories.shift":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyModifier() == KeyModifier.SHIFT).toArray(KeyBinding[]::new);
		case "categories.none":
			return Arrays.stream(bindings).filter(binding -> binding.getKeyModifier() == KeyModifier.NONE && binding.getKeyCode() != 0).toArray(KeyBinding[]::new);
		default:
			return Arrays.stream(bindings).filter(binding -> binding.getKeyCategory().equals(category)).toArray(KeyBinding[]::new);
		}
	}
	
	private KeyBinding[] filterBindingsByName(KeyBinding[] bindings, String[] words){
		KeyBinding[] filtered = {};
		filtered = Arrays.stream(bindings).filter(binding -> {
				boolean flag = true;
				for (String w:words) {
					if (w == null || w.isEmpty()) {
						continue;
					}
					flag = flag && I18n.format(binding.getKeyDescription()).toLowerCase().contains(w.toLowerCase());
				}
				return flag;
			}).toArray(KeyBinding[]::new);
		return filtered;
	}

	private KeyBinding[] filterBindingsByKey(KeyBinding[] bindings, String keyName) {
		KeyBinding[] filtered = {};
		filtered = Arrays.stream(bindings).filter(binding -> binding.getDisplayName().toLowerCase().contains(keyName.toLowerCase())).toArray(KeyBinding[]::new);
		return filtered;
	}

	private KeyBinding[] filterBindingsBySearch(KeyBinding[] bindings, String searchText) {
		KeyBinding[] filtered = bindings;
		Matcher keyNameMatcher = Pattern.compile("<.*>").matcher(searchText);
		if (keyNameMatcher.find()) {
			String keyNameWithBrackets = keyNameMatcher.group();
			String keyName = keyNameWithBrackets.substring(1, keyNameWithBrackets.length() - 1);
			searchText = searchText.replace(keyNameWithBrackets, "");
			filtered = filterBindingsByKey(filtered, keyName);
		}
		if (!searchText.isEmpty()) {
			filtered = filterBindingsByName(filtered, searchText.split("\\s+"));
		}
		return filtered;
	}
	
	public KeyBinding getSelectedKeybind(){
		return this.selectedKeybind;
	}

}
