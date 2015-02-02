package ljfa.tntutils.gui;

import java.util.ArrayList;
import java.util.List;

import ljfa.tntutils.Config;
import ljfa.tntutils.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import cpw.mods.fml.client.config.DummyConfigElement;
import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;

public class TntuConfigGui extends GuiConfig {
    public TntuConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), Reference.MODID, false, false, "TNTUtils configuration");
    }
    
    /** Compiles a list of config elements */
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        
        //Add categories to config GUI
        list.add(new ConfigElement<ConfigCategory>(Config.conf.getCategory(Config.CATEGORY_GENERAL)));
        return list;
    }
}
