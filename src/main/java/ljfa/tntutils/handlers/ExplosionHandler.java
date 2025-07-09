package ljfa.tntutils.handlers;

import ljfa.tntutils.Config;
import ljfa.tntutils.asm.HooksExplosion;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ItemStackHolder;

public class ExplosionHandler {
    @ItemStackHolder(value = "appliedenergistics2:material", meta = 47)
    public static final ItemStack AE2_SINGULARITY = ItemStack.EMPTY; //must be public

    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
        if(Config.disableExplosions)
            event.setCanceled(true);
        else
            event.getExplosion().size *= Config.sizeMultiplier;
    }

    @SubscribeEvent
    public void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        final World world = event.getWorld();
        if(world.isRemote)
            return;

        //Block damage
        if(Config.disableBlockDamage || (Config.disableCreeperBlockDamage && event.getExplosion().exploder instanceof EntityCreeper))
            event.getAffectedBlocks().clear(); //fast path, we can just remove everything
        else {
            if(Config.spareTileEntities || Config.blacklistActive) {
                //Remove blacklisted blocks and tile entities (if configured) from the list
                event.getAffectedBlocks().removeIf(pos -> shouldSpareBlock(world.getBlockState(pos)));
            }

            if(Config.preventChainExpl) {
                //Manually remove TNT blocks from the world before the actual explosion destroys them
                //(which would cause a chain explosion)
                for(BlockPos pos: event.getAffectedBlocks()) {
                    IBlockState state = world.getBlockState(pos);
                    if(state.getBlock() instanceof BlockTNT) {
                        state.getBlock().dropBlockAsItemWithChance(world, pos, state, HooksExplosion.getDropChance(event.getExplosion()), 0);
                        world.setBlockToAir(pos);
                    }
                }
            }
        }

        //Entity damage
        if(Config.disableEntityDamage && (AE2_SINGULARITY.isEmpty() || !Config.alwaysAffectAE2Singularities))
            event.getAffectedEntities().clear(); //fast path, we can just remove everything
        else if(Config.disableEntityDamage || Config.disablePlayerDamage || Config.disableItemDamage || Config.disableNPCDamage || Config.preventChainExpl) {
            //Remove configured entities from the list
            event.getAffectedEntities().removeIf(ent -> shouldSpareEntity(ent));
        }
    }

    private static boolean shouldSpareBlock(IBlockState state) {
        if(Config.spareTileEntities && state.getBlock().hasTileEntity(state))
            return true;
        Integer mask = Config.blackWhiteList.get(state.getBlock());
        boolean matches =  mask != null && (mask & (1 << state.getBlock().getMetaFromState(state))) != 0;
        return Config.listIsWhitelist ? !matches : matches;
    }

    private static boolean shouldSpareEntity(Entity ent) {
        if((Config.disableEntityDamage || Config.disableItemDamage) && ent instanceof EntityItem) {
            if(Config.alwaysAffectAE2Singularities)
                return !((EntityItem) ent).getItem().isItemEqual(AE2_SINGULARITY);
            else
                return true;
        }

        return Config.disableEntityDamage
            || (Config.disableNPCDamage && ent instanceof EntityLivingBase && !(ent instanceof EntityPlayer))
            || (Config.disablePlayerDamage && ent instanceof EntityPlayer)
            || (Config.preventChainExpl && ent instanceof EntityMinecartTNT);
    }
}
