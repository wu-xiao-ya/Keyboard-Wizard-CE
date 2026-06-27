package mrnerdy42.keywizard.gui;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mrnerdy42.keywizard.util.KeybindUtils;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiBindingList extends GuiScrollingList {
	
	private GuiKeyWizard parent;
	private KeyBinding[] bindings;
	
	private String searchText;
	private String selectedCategory;
	
	private KeyBinding selectedKeybind;
	private int selectedKeybindId;
	private static final String CATEGORY_PREFIX = "key.categories.";
	private static final int BORDER = 4;
	private static final int SCROLLBAR_WIDTH = 6;
	private static final int SCROLLBAR_GAP = 5;
	private static final int CONTENT_PADDING = 3;
	private float scrollDistance;
	private boolean wasMouseDown;
	private boolean scrolling;

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
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.mouseX = mouseX;
		this.mouseY = mouseY;
		this.handleClick(mouseX, mouseY);
		this.applyScrollLimits();

		Gui.drawRect(this.left, this.top, this.left + this.listWidth, this.bottom, 0xAA020606);
		Gui.drawRect(this.left, this.top, this.left + this.listWidth, this.top + 1, 0x663E9F82);
		Gui.drawRect(this.left, this.bottom - 1, this.left + this.listWidth, this.bottom, 0x663E9F82);

		double scaleX = this.parent.getClient().displayWidth / (double) this.parent.width;
		double scaleY = this.parent.getClient().displayHeight / (double) this.parent.height;
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		GL11.glScissor((int) (this.left * scaleX), (int) ((this.parent.height - this.bottom) * scaleY),
				(int) (this.listWidth * scaleX), (int) ((this.bottom - this.top) * scaleY));

		Tessellator tess = Tessellator.getInstance();
		int entryRight = this.getContentRight();
		int baseY = this.top + BORDER - (int) this.scrollDistance;
		for (int slotIdx = 0; slotIdx < this.bindings.length; slotIdx++) {
			int slotTop = baseY + slotIdx * this.slotHeight;
			int slotBuffer = this.slotHeight - BORDER;
			if (slotTop <= this.bottom && slotTop + slotBuffer >= this.top) {
				if (this.isSelected(slotIdx)) {
					Gui.drawRect(this.left, slotTop - 2, entryRight, slotTop + slotBuffer + 2, 0xFF808080);
					Gui.drawRect(this.left + 1, slotTop - 1, entryRight - 1, slotTop + slotBuffer + 1, 0xFF000000);
				}
				this.drawSlot(slotIdx, entryRight, slotTop, slotBuffer, tess);
			}
		}

		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		this.drawScrollBar();
	}

	@Override
	public void handleMouseInput(int mouseX, int mouseY) {
		this.handleMouseInput(mouseX, mouseY, Mouse.getEventDWheel());
	}

	public void handleMouseInput(int mouseX, int mouseY, int scroll) {
		if (scroll == 0 || mouseX < this.left || mouseX > this.left + this.listWidth || mouseY < this.top || mouseY > this.bottom) {
			return;
		}
		this.scrollDistance += (float) ((-1 * scroll / 120.0F) * this.slotHeight / 2);
		this.applyScrollLimits();
	}

	@Override
	protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
		FontRenderer fontRender = this.parent.getFontRenderer();
		KeyBinding currentBinding = this.bindings[slotIdx];
		
		fontRender.drawStringWithShadow(I18n.format(currentBinding.getKeyDescription()), this.left + 3 , slotTop, 0xFFFFFF);

		int keyColor = getBindingColor(currentBinding);
		fontRender.drawStringWithShadow(currentBinding.getDisplayName(), this.left + 3, slotTop + fontRender.FONT_HEIGHT + 2, keyColor);

		String categoryLabel = getCategoryDisplayLabel(currentBinding);
		int contentRight = entryRight - CONTENT_PADDING;
		int maxLabelWidth = contentRight - (this.left + CONTENT_PADDING);
		if (!categoryLabel.isEmpty() && maxLabelWidth > 0) {
			String clipped = fontRender.trimStringToWidth(categoryLabel, maxLabelWidth);
			int labelWidth = fontRender.getStringWidth(clipped);
			int labelX = contentRight - labelWidth;
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

	private void handleClick(int mouseX, int mouseY) {
		boolean mouseDown = Mouse.isButtonDown(0);
		if (!mouseDown) {
			this.scrolling = false;
		} else if (!this.wasMouseDown && mouseX >= this.getScrollBarLeft() && mouseX <= this.getScrollBarRight()
				&& mouseY >= this.top && mouseY <= this.bottom && this.hasScrollbar()) {
			this.scrolling = true;
		}

		if (this.scrolling) {
			this.scrollToMouse(mouseY);
		} else if (mouseDown && !this.wasMouseDown && mouseX >= this.left && mouseX <= this.getContentRight()
				&& mouseY >= this.top && mouseY <= this.bottom) {
			int mouseListY = mouseY - this.top + (int) this.scrollDistance - BORDER;
			int slotIndex = mouseListY / this.slotHeight;
			if (slotIndex >= 0 && mouseListY >= 0 && slotIndex < this.bindings.length) {
				this.elementClicked(slotIndex, false);
			}
		}
		this.wasMouseDown = mouseDown;
	}

	private void applyScrollLimits() {
		int maxScroll = this.getSize() * this.slotHeight - (this.bottom - this.top - BORDER);
		if (maxScroll < 0) {
			maxScroll = 0;
		}
		if (this.scrollDistance < 0.0F) {
			this.scrollDistance = 0.0F;
		}
		if (this.scrollDistance > maxScroll) {
			this.scrollDistance = maxScroll;
		}
	}

	private int getContentRight() {
		return this.left + this.listWidth - SCROLLBAR_WIDTH - SCROLLBAR_GAP;
	}

	private int getScrollBarLeft() {
		return this.left + this.listWidth - SCROLLBAR_WIDTH;
	}

	private int getScrollBarRight() {
		return this.left + this.listWidth;
	}

	private boolean hasScrollbar() {
		return this.getSize() * this.slotHeight + BORDER > this.bottom - this.top;
	}

	private void scrollToMouse(int mouseY) {
		int viewHeight = this.bottom - this.top;
		int contentHeight = this.getSize() * this.slotHeight;
		int extraHeight = contentHeight + BORDER - viewHeight;
		if (extraHeight <= 0) {
			this.scrollDistance = 0.0F;
			return;
		}

		int barHeight = viewHeight * viewHeight / contentHeight;
		if (barHeight < 32) {
			barHeight = 32;
		}
		if (barHeight > viewHeight - BORDER * 2) {
			barHeight = viewHeight - BORDER * 2;
		}

		float scrollRange = viewHeight - barHeight;
		if (scrollRange <= 0.0F) {
			this.scrollDistance = 0.0F;
		} else {
			this.scrollDistance = (mouseY - this.top - barHeight / 2.0F) * extraHeight / scrollRange;
		}
		this.applyScrollLimits();
	}
	private void drawScrollBar() {
		int viewHeight = this.bottom - this.top;
		int contentHeight = this.getSize() * this.slotHeight;
		int extraHeight = contentHeight + BORDER - viewHeight;
		if (extraHeight <= 0) {
			return;
		}

		int scrollBarRight = this.getScrollBarRight();
		int scrollBarLeft = this.getScrollBarLeft();
		int barHeight = viewHeight * viewHeight / contentHeight;
		if (barHeight < 32) {
			barHeight = 32;
		}
		if (barHeight > viewHeight - BORDER * 2) {
			barHeight = viewHeight - BORDER * 2;
		}

		int barTop = (int) this.scrollDistance * (viewHeight - barHeight) / extraHeight + this.top;
		if (barTop < this.top) {
			barTop = this.top;
		}
		Gui.drawRect(scrollBarLeft, this.top, scrollBarRight, this.bottom, 0xFF000000);
		Gui.drawRect(scrollBarLeft, barTop, scrollBarRight, barTop + barHeight, 0xFF808080);
		Gui.drawRect(scrollBarLeft, barTop, scrollBarRight - 1, barTop + barHeight - 1, 0xFFC0C0C0);
	}

}
