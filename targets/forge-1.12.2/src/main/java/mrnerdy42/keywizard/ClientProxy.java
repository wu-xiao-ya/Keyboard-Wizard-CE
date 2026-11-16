package mrnerdy42.keywizard;

import mrnerdy42.keywizard.handlers.ClientEventHandler;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }
}
