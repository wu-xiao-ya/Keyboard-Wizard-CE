package mrnerdy42.keywizard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = KeyWizard.MODID, name = KeyWizard.NAME, version = KeyWizard.VERSION, acceptedMinecraftVersions = "[1.12.2]", clientSideOnly = true)
public class KeyWizard {

    public static final String MODID = "keyboard_wizard_ce";
    public static final String NAME = "\u6309\u952e\u7cbe\u7075\u793e\u533a\u7248\uff08Keyboard Wizard CE\uff09";
    public static final String VERSION = "1.0.0";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @SidedProxy(clientSide = "mrnerdy42.keywizard.ClientProxy", serverSide = "mrnerdy42.keywizard.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }
}
