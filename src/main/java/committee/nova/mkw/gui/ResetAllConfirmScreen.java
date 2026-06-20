package committee.nova.mkw.gui;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

public class ResetAllConfirmScreen extends ConfirmScreen {
    public ResetAllConfirmScreen(BooleanConsumer callback) {
        super(callback, Component.translatable("gui.confirm.resetAll"), Component.empty());
    }
}
