package committee.nova.mkw.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ResetAllConfirmScreen extends ConfirmScreen {
    public ResetAllConfirmScreen(BooleanConsumer callback) {
        super(callback, new TranslationTextComponent("gui.confirm.resetAll"), StringTextComponent.EMPTY);
    }
}
