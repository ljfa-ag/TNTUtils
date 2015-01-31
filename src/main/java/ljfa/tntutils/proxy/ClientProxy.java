package ljfa.tntutils.proxy;

import net.minecraft.client.Minecraft;
import ljfa.tntutils.blocks.ModBlocks;
import cpw.mods.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        ModBlocks.replaced_tnt.registerBlockIcons(mc.getTextureMapBlocks());
    }
    
    private Minecraft mc;
}
