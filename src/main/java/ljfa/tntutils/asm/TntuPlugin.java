package ljfa.tntutils.asm;

import java.util.Map;

import ljfa.tntutils.Config;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.Name;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name("TNTUtilities Core")
@MCVersion("1.7.10")
@SortingIndex(1100)
@TransformerExclusions("ljfa.tntutils")
public class TntuPlugin implements IFMLLoadingPlugin {
    @Override
    public String[] getASMTransformerClass() {
        if(Config.alwaysDrop)
            return new String[] { ExplosionTransformer.class.getName() };
        else
            return null;
    }

    @Override
    public String getModContainerClass() {
        return TntuModContainer.class.getName();
    }

    @Override
    public String getSetupClass() {
        return TntuSetup.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {

    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
