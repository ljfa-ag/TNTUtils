package ljfa.tntutils.handlers;

import ljfa.tntutils.Config;
import ljfa.tntutils.util.ListHelper;
import ljfa.tntutils.util.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExplosionHandler {
    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
        if(Config.disableExplosions)
            event.setCanceled(true);
        else
            event.getExplosion().explosionSize *= Config.sizeMultiplier;
    }

    @SubscribeEvent
    public void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        if(event.getWorld().isRemote)
            return;
        //Block damage
        if(Config.disableBlockDamage || (Config.disableCreeperBlockDamage && event.getExplosion().exploder instanceof EntityCreeper))
            event.getAffectedBlocks().clear();
        else if(Config.spareTileEntities || Config.blacklistActive) {
            //Remove blacklisted blocks and tile entities (if configured) from the list
            ListHelper.removeIf(event.getAffectedBlocks(), new Predicate<BlockPos>() {
                @Override
                public boolean test(BlockPos pos) {
                    return shouldBePreserved(event.getWorld().getBlockState(pos));
                }
            });
        }
        
        //Entity damage
        if(Config.disableEntityDamage)
            event.getAffectedEntities().clear();
        else if(Config.disablePlayerDamage || Config.disableItemDamage || Config.disableNPCDamage || Config.preventChainExpl) {
            //Remove configured entities from the list
            ListHelper.removeIf(event.getAffectedEntities(), new Predicate<Entity>() {
                @Override
                public boolean test(Entity ent) {
                    return (Config.disableNPCDamage && ent instanceof EntityLivingBase && !(ent instanceof EntityPlayer))
                        || (Config.disablePlayerDamage && ent instanceof EntityPlayer)
                        || (Config.disableItemDamage && ent instanceof EntityItem)
                        || (Config.preventChainExpl && ent instanceof EntityMinecartTNT);
                }
            });
        }
    }

    public static boolean shouldBePreserved(IBlockState state) {
        if(Config.spareTileEntities && state.getBlock().hasTileEntity(state))
            return true;
        Integer mask = Config.blackWhiteList.get(state.getBlock());
        boolean matches =  mask != null && (mask & (1 << state.getBlock().getMetaFromState(state))) != 0;
        return Config.listIsWhitelist ? !matches : matches;
    }
}
