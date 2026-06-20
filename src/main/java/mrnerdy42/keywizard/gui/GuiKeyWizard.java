package mrnerdy42.keywizard.gui;

import static org.lwjgl.input.Keyboard.*;
import static org.lwjgl.input.Mouse.getButtonName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import org.lwjgl.input.Mouse;

import mrnerdy42.keywizard.KeyWizardConfig;
import mrnerdy42.keywizard.util.KeybindUtils;
import mrnerdy42.keywizard.util.KeyboardFactory;
import mrnerdy42.keywizard.util.KeyboardLayout;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiKeyWizard extends GuiScreen {

	private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation("keyboard_wizard_ce", "textures/gui/key_wizard_background.png");
	public static final String KEY_FILTER_PREFIX = "#key#";
	private static final int RESET_ALL_DIALOG_ID = 0;

	private enum SortType implements Comparator<KeyBinding> {
		NAME { @Override public int compare(KeyBinding arg0, KeyBinding arg1){ return I18n.format(arg0.getKeyDescription()).compareTo(I18n.format(arg1.getKeyDescription())); }},
		CATEGORY { @Override public int compare(KeyBinding arg0, KeyBinding arg1){ return I18n.format(arg0.getKeyCategory()).compareTo(I18n.format(arg1.getKeyCategory())); }},
		KEY { @Override public int compare(KeyBinding arg0, KeyBinding arg1){ return I18n.format(arg0.getDisplayName()).compareTo(I18n.format(arg1.getDisplayName())); }};

	}

	public KeyboardLayout layout = KeyWizardConfig.layout;

	protected GuiKeyboard keyboard;
	protected SortType sortType = SortType.NAME;

	private final GuiScreen parentScreen;

    private KeyboardLayout[] pages = {KeyWizardConfig.layout, KeyboardLayout.NUMPAD, KeyboardLayout.AUXILIARY};
    private int pageNum = 0;
	private int mouse = 0;
	private int maxMouse = KeyWizardConfig.maxMouseButtons - 1;
	private KeyBinding selectedKeybind;
	private KeyModifier activeModifier = KeyModifier.NONE;
	private String selectedCategory = "categories.all";
	private String searchText = "";
	private int guiWidth;
	private int guiStartX;


	private GuiCategorySelector categoryList;
	private GuiTextField searchBar;
	private GuiBindingList bindingList;
	private GuiButton buttonLayoutMain;
	private GuiButton buttonLayoutNumpad;
	private GuiButton buttonLayoutAux;
	private GuiButton buttonReset;
	private GuiButton buttonClear;
	private GuiButton buttonResetAll;
	private GuiButton buttonToggle;
	private GuiButton buttonActiveModifier;
	private GuiButton buttonMouse;
	private GuiButton buttonMousePlus;
	private GuiButton buttonMouseMinus;
	private GuiButton buttonHelp;



	public GuiKeyWizard(Minecraft mcIn, GuiScreen parentScreen) {
		this.mc = mcIn;
		this.parentScreen = parentScreen;
	}

	@Override
	public void initGui() {

		int maxBindingLength = 0;

		for (KeyBinding binding : KeybindUtils.ALL_BINDINGS) {
			int bindingWidth = this.fontRenderer.getStringWidth(I18n.format(binding.getKeyDescription()));
			if (bindingWidth > maxBindingLength)
				maxBindingLength = bindingWidth;
		}

		int bindingListWidth = maxBindingLength + 20;

		this.bindingList = new GuiBindingList(this, 10, this.height - 30, bindingListWidth, this.height - 40,
				fontRenderer.FONT_HEIGHT * 3 + 10);

		this.searchBar = new GuiTextField(0, this.fontRenderer, 10, this.height - 20, bindingListWidth, 14);
		this.searchBar.setFocused(true);
		this.searchBar.setCanLoseFocus(false);

		this.guiStartX = bindingListWidth + 15;
		this.guiWidth = this.width - this.guiStartX;

		ArrayList<String> categories = KeybindUtils.getCategories();
		categories.add(0, "categories.conflicts");
		categories.add(0, "categories.unbound");
		categories.add(0, "categories.all");
		categories.add("categories.ctrl");
		categories.add("categories.alt");
		categories.add("categories.shift");
		categories.add("categories.none");

		int maxCategoryLength = 0;
		for(String s:categories) {
			int width = this.fontRenderer.getStringWidth(I18n.format(s));
			if (width > maxCategoryLength)
				maxCategoryLength = width;
		}

		int categoryWidth = maxCategoryLength + 20;
		this.categoryList = new GuiCategorySelector(this, this.guiStartX, 5, categoryWidth, categories);
		this.selectedCategory = this.categoryList.getSelctedCategory();

		this.keyboard = KeyboardFactory.makeKeyboard(this.pages[this.pageNum], this, this.guiStartX, this.height / 2 - 90, this.guiWidth - 5, this.height);

		int layoutButtonX = this.guiStartX + categoryWidth + 8;
		this.buttonLayoutMain = new GuiButton(0, layoutButtonX, 5, 50, 20, I18n.format("gui.layout.main"));
		this.buttonLayoutNumpad = new GuiButton(0, layoutButtonX + 54, 5, 50, 20, I18n.format("gui.layout.numpad"));
		this.buttonLayoutAux = new GuiButton(0, layoutButtonX + 108, 5, 50, 20, I18n.format("gui.layout.auxiliary"));

		this.buttonReset = new GuiButton(0, this.guiStartX, this.height - 40, 75, 20, I18n.format("gui.resetBinding"));
		this.buttonClear = new GuiButton(0, this.guiStartX + 76, this.height - 40, 75, 20, I18n.format("gui.clearBinding"));
		this.buttonResetAll = new GuiButton(0, this.guiStartX + 152, this.height - 40, 75, 20, I18n.format("gui.resetAll"));

		this.buttonHelp = new GuiButton(2, this.width - 47, this.height - 22, 20, 20, "?");
		this.buttonToggle = new TexturedToggleButton(3, this.width - 22, this.height - 22, 20);

		this.buttonActiveModifier = new GuiButton(1, this.guiStartX, this.height - 63, 150, 20,
				I18n.format("gui.activeModifier")+ ": " + activeModifier.toString());

		int mouseRowY = this.height / 2 - 115;
		this.buttonMouseMinus = new GuiButton(0, this.width - 137, mouseRowY, 25, 20, "-");
		this.buttonMouse = new GuiButton(0, this.width - 110, mouseRowY, 78, 20, I18n.format("gui.mouse") + ": " + getButtonName(this.mouse));
		this.buttonMousePlus = new GuiButton(0, this.width - 30, mouseRowY, 25, 20, "+");

		this.setSelectedKeybind(this.bindingList.getSelectedKeybind());

		this.buttonList.add(this.buttonLayoutMain);
		this.buttonList.add(this.buttonLayoutNumpad);
		this.buttonList.add(this.buttonLayoutAux);
		this.buttonList.add(this.buttonReset);
		this.buttonList.add(this.buttonClear);
		this.buttonList.add(this.buttonResetAll);
		this.buttonList.add(this.buttonHelp);
		this.buttonList.add(this.buttonToggle);
		this.buttonList.add(this.buttonActiveModifier);
		this.buttonList.add(this.buttonMouse);
		this.buttonList.add(this.buttonMousePlus);
		this.buttonList.add(this.buttonMouseMinus);

		this.updateLayoutButtons();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();

		// Textured grid background + dark overlay (visual parity with the modern ports)
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.enableBlend();
		this.mc.getTextureManager().bindTexture(BACKGROUND_TEXTURE);
		drawModalRectWithCustomSizedTexture(0, 0, 0.0F, 0.0F, this.width, this.height, 512.0F, 512.0F);
		drawRect(0, 0, this.width, this.height, 0x77000000);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

		super.drawScreen(mouseX, mouseY, partialTicks);
		this.bindingList.drawScreen(mouseX, mouseY, partialTicks);
		this.searchBar.drawTextBox();

		this.keyboard.draw(this.mc, mouseX, mouseY, partialTicks);
		this.categoryList.drawButton(this.mc, mouseX, mouseY, partialTicks);

		if (this.buttonHelp != null && mouseX >= this.buttonHelp.x && mouseX < this.buttonHelp.x + this.buttonHelp.width
				&& mouseY >= this.buttonHelp.y && mouseY < this.buttonHelp.y + this.buttonHelp.height) {
			List<String> tooltip = new ArrayList<String>();
			tooltip.add(I18n.format("gui.help.tooltip.1"));
			tooltip.add(I18n.format("gui.help.tooltip.2"));
			tooltip.add(I18n.format("gui.help.tooltip.3"));
			tooltip.add(I18n.format("gui.help.tooltip.4"));
			tooltip.add(I18n.format("gui.help.tooltip.5"));
			GuiUtils.drawHoveringText(tooltip, mouseX, mouseY, this.width, this.height, -1, this.fontRenderer);
		}
	}

	@Override
	public void updateScreen() {
	    super.updateScreen();
	    this.searchBar.updateCursorCounter();
	    if ( this.buttonReset != null )
			this.buttonReset.enabled = this.selectedKeybind != null && !this.selectedKeybind.isSetToDefaultValue();
		if ( this.buttonClear != null ) {
			this.buttonClear.enabled = this.selectedKeybind != null && !(this.selectedKeybind.getKeyCode() == 0);
		}

	    if ( this.categoryList != null )
	    	this.selectedCategory = this.categoryList.getSelctedCategory();

	    if ( !this.searchBar.getText().equals(this.searchText) ) {
	    	this.searchText = this.searchBar.getText();
	    }

	    if (this.activeModifier != null) {
	    	switch (this.activeModifier.toString()) {
	    	case "CONTROL":
	    		this.keyboard.disableKey(KEY_LCONTROL);
	    		this.keyboard.disableKey(KEY_RCONTROL);

	    		this.keyboard.enableKey(KEY_LMENU);
	    		this.keyboard.enableKey(KEY_RMENU);
	    		this.keyboard.enableKey(KEY_LSHIFT);
	    		this.keyboard.enableKey(KEY_RSHIFT);
	    		break;
	    	case "ALT":
	    		this.keyboard.disableKey(KEY_LMENU);
	    		this.keyboard.disableKey(KEY_RMENU);

	    		this.keyboard.enableKey(KEY_LCONTROL);
	    		this.keyboard.enableKey(KEY_RCONTROL);
	    		this.keyboard.enableKey(KEY_LSHIFT);
	    		this.keyboard.enableKey(KEY_RSHIFT);
	    		break;
	    	case "SHIFT":
	    		this.keyboard.disableKey(KEY_LSHIFT);
	    		this.keyboard.disableKey(KEY_RSHIFT);

	    		this.keyboard.enableKey(KEY_LCONTROL);
	    		this.keyboard.enableKey(KEY_RCONTROL);
	    		this.keyboard.enableKey(KEY_LMENU);
	    		this.keyboard.enableKey(KEY_RMENU);
	    		break;
	    	case "NONE" :
	    		this.keyboard.enableKey(KEY_LCONTROL);
	    		this.keyboard.enableKey(KEY_RCONTROL);
	    		this.keyboard.enableKey(KEY_LMENU);
	    		this.keyboard.enableKey(KEY_RMENU);
	    		this.keyboard.enableKey(KEY_LSHIFT);
	    		this.keyboard.enableKey(KEY_RSHIFT);
	    	}
	    }

	    switch (KeybindUtils.getNumBindings(-100 + this.mouse, this.activeModifier)) {
	    case 0:
	    	this.buttonMouse.displayString = I18n.format("gui.mouse") + ": " + getButtonName(this.mouse);
	    	break;
	    case 1:
	    	this.buttonMouse.displayString = I18n.format("gui.mouse") + ": " + TextFormatting.GREEN + getButtonName(this.mouse);
	    	break;
	    default:
	    	this.buttonMouse.displayString = I18n.format("gui.mouse") + ": " + TextFormatting.RED + getButtonName(this.mouse);
	    	break;
	    }

		this.bindingList.updateList();
		if (this.buttonMouse != null) {
			this.buttonMouse.enabled = this.selectedKeybind != null;
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) {

		if (!this.categoryList.getExtended()) {
			if (button == this.buttonReset) {
				if (this.selectedKeybind == null) {
					return;
				}
				this.selectedKeybind.setToDefault();
				KeyBinding.resetKeyBindingArrayAndHash();
				this.buttonReset.enabled = !selectedKeybind.isSetToDefaultValue();
				return;
			}

			if (button == this.buttonClear) {
				if (this.selectedKeybind == null) {
					return;
				}
				this.selectedKeybind.setKeyModifierAndCode(KeyModifier.NONE, 0);
				KeyBinding.resetKeyBindingArrayAndHash();
				this.buttonClear.enabled = this.selectedKeybind.getKeyCode() != 0;
			}

			if (button == this.buttonResetAll) {
				this.mc.displayGuiScreen(new GuiYesNo(this, I18n.format("gui.confirm.resetAll"), "", RESET_ALL_DIALOG_ID));
				return;
			}

			if (button == this.buttonToggle) {
				if (this.parentScreen != null)
					this.mc.displayGuiScreen(this.parentScreen);
				else
					this.mc.displayGuiScreen((GuiScreen)null);
			}

			if (button == this.buttonActiveModifier) {
				this.changeActiveModifier();
			}

			if (button == this.buttonLayoutMain) {
				this.setLayout(0);
			}

			if (button == this.buttonLayoutNumpad) {
				this.setLayout(1);
			}

			if (button == this.buttonLayoutAux) {
				this.setLayout(2);
			}

			if (button == this.buttonMouse) {
				this.selectedKeybind.setKeyModifierAndCode(this.activeModifier, -100 + this.mouse);
				mc.gameSettings.setOptionKeyBinding(this.selectedKeybind, -100 + this.mouse);
				KeyBinding.resetKeyBindingArrayAndHash();
			}

			if (button == this.buttonMousePlus) {
				if(this.mouse >= this.maxMouse) {
					this.mouse = 0;
				} else {
					this.mouse++;
				}
			}

			if (button == this.buttonMouseMinus) {
				if(this.mouse <= 0) {
					this.mouse = this.maxMouse;
				} else {
					this.mouse--;
				}
			}

		if (this.buttonReset != null) {
			this.buttonReset.enabled = this.selectedKeybind != null && !this.selectedKeybind.isSetToDefaultValue();
		}
		}
	}

	@Override
	public void confirmClicked(boolean result, int id) {
		if (id == RESET_ALL_DIALOG_ID) {
			if (result) {
				for (KeyBinding binding : KeybindUtils.ALL_BINDINGS) {
					binding.setToDefault();
				}
				KeyBinding.resetKeyBindingArrayAndHash();
			}
			this.mc.displayGuiScreen(this);
		}
	}

	/** Switch the visible keyboard layout and refresh the layout buttons. */
	private void setLayout(int index) {
		this.pageNum = index;
		this.keyboard = KeyboardFactory.makeKeyboard(this.pages[this.pageNum], this, this.guiStartX, this.height / 2 - 90, this.guiWidth - 5, this.height);
		this.updateLayoutButtons();
	}

	/** Disable the button for the layout that is currently shown so it reads as selected. */
	private void updateLayoutButtons() {
		if (this.buttonLayoutMain != null)
			this.buttonLayoutMain.enabled = this.pageNum != 0;
		if (this.buttonLayoutNumpad != null)
			this.buttonLayoutNumpad.enabled = this.pageNum != 1;
		if (this.buttonLayoutAux != null)
			this.buttonLayoutAux.enabled = this.pageNum != 2;
	}


    @Override
	public void handleMouseInput() throws IOException {
		super.handleMouseInput();

		int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

		this.bindingList.handleMouseInput(mouseX, mouseY);
		this.categoryList.handleMouseInput(mouseX, mouseY);
	}

	@Override
	protected void mouseClicked(int x, int y, int button) throws IOException {
	    super.mouseClicked(x, y, button);
	    this.searchBar.mouseClicked(x, y, button);
	    this.categoryList.mouseClicked(this.mc, x, y, button);
	    this.keyboard.mouseClicked(mc, x, y, button);
	}

	@Override
	protected void keyTyped(char c, int keyCode) throws IOException {
	    super.keyTyped(c, keyCode);
	    this.searchBar.textboxKeyTyped(c, keyCode);
	}

	/** Change the active modifier */
	private void changeActiveModifier() {

		if (this.activeModifier == KeyModifier.NONE) {
			this.activeModifier = KeyModifier.ALT;
		} else if (this.activeModifier == KeyModifier.ALT) {
			this.activeModifier = KeyModifier.CONTROL;
		} else if (this.activeModifier == KeyModifier.CONTROL) {
			this.activeModifier = KeyModifier.SHIFT;
		} else {
			this.activeModifier = KeyModifier.NONE;
		}

		this.buttonActiveModifier.displayString = I18n.format("gui.activeModifier" )+ ": " + activeModifier.toString();
	}

    public Minecraft getClient() {
		return this.mc;
	}

	public FontRenderer getFontRenderer() {
		return this.fontRenderer;
	}

	public String getSearchText() {
		return this.searchText;
	}

	public void setSearchText(String s) {
		this.searchText = s;
		this.searchBar.setText(s);
	}

	public void setSearchTextForKey(int keyCode) {
		String keyName = getKeyName(keyCode);
		this.setSearchText(KEY_FILTER_PREFIX + "<" + keyName + ">");
	}

	public String getSelectedCategory() {
		return this.selectedCategory;
	}

	public KeyModifier getActiveModifier() {
		return this.activeModifier;
	}

	public KeyBinding getSelectedKeybind() {
		return this.selectedKeybind;
	}

	protected void setSelectedKeybind(KeyBinding binding){
    	this.selectedKeybind = binding;
    }

	public boolean getCategoryListExtended() {
		return this.categoryList.getExtended();
	}
}
