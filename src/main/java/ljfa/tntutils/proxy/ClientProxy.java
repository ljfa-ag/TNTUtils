package ljfa.tntutils.proxy;

import ljfa.tntutils.TNTUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

public class ClientProxy extends CommonProxy {
    public ClientProxy() {
        mc = Minecraft.getMinecraft();
    }
    
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        mc.getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(TNTUtils.replaced_tnt), 0,
                new ModelResourceLocation("minecraft:tnt", "inventory"));
    }
    
    private Minecraft mc;
}
