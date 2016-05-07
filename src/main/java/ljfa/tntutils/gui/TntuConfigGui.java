package ljfa.tntutils.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import ljfa.tntutils.Config;
import ljfa.tntutils.Reference;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;

public class TntuConfigGui extends GuiConfig {
    public TntuConfigGui(GuiScreen parent) {
        super(parent, getConfigElements(), Reference.MODID, false, false, "TNTUtils configuration");
    }
    
    /** Compiles a list of config elements
     * Borrowed from EnderIO's implementation
     */
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        
        for(String name: Config.conf.getCategoryNames())
            list.add(new ConfigElement<ConfigCategory>(Config.conf.getCategory(name)));
        
        return list;
    }
}
