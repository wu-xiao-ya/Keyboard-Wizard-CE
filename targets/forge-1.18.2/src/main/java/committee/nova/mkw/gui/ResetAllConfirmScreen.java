package committee.nova.mkw.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class ResetAllConfirmScreen extends ConfirmScreen {
    public ResetAllConfirmScreen(BooleanConsumer callback) {
        super(callback, new TranslatableComponent("gui.confirm.resetAll"), TextComponent.EMPTY);
    }
}
