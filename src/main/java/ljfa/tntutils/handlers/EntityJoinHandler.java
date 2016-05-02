package ljfa.tntutils.handlers;

import ljfa.tntutils.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EntityJoinHandler {
    @SubscribeEvent
    public void onEntityJoin(EntityJoinWorldEvent event) {
        Entity ent = event.getEntity();
        if(Config.disableTNT && ent instanceof EntityTNTPrimed) {
            event.setCanceled(true);
            event.getWorld().spawnEntityInWorld(new EntityItem(event.getWorld(), ent.posX, ent.posY, ent.posZ, new ItemStack(Blocks.tnt)));
        }
        else if(Config.disableTNTMinecart && ent instanceof EntityMinecartTNT) {
            event.setCanceled(true);
            event.getWorld().spawnEntityInWorld(new EntityItem(event.getWorld(), ent.posX, ent.posY, ent.posZ, new ItemStack(Blocks.tnt)));
            event.getWorld().spawnEntityInWorld(new EntityItem(event.getWorld(), ent.posX, ent.posY, ent.posZ, new ItemStack(Items.minecart)));
        }
    }
}
