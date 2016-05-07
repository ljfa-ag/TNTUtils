package ljfa.tntutils.handlers;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import ljfa.tntutils.Config;
import ljfa.tntutils.asm.HooksExplosion;
import ljfa.tntutils.util.ListHelper;
import ljfa.tntutils.util.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecartTNT;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.world.ExplosionEvent;

public class ExplosionHandler {
    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
        if(Config.disableExplosions)
            event.setCanceled(true);
        else
            event.explosion.explosionSize *= Config.sizeMultiplier;
    }

    @SubscribeEvent
    public void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        final World world = event.world;
        if(world.isRemote)
            return;
        
        //Block damage
        if(Config.disableBlockDamage || (Config.disableCreeperBlockDamage && event.explosion.exploder instanceof EntityCreeper))
            event.getAffectedBlocks().clear();
        else {
            if(Config.spareTileEntities || Config.blacklistActive) {
                //Remove blacklisted blocks and tile entities (if configured) from the list
                ListHelper.removeIf(event.getAffectedBlocks(), new Predicate<ChunkPosition>() {
                    @Override
                    public boolean test(ChunkPosition pos) {
                        Block block = world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                        int meta = world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                        return shouldBePreserved(block, meta);
                    }
                });
            }
            
            if(Config.preventChainExpl) {
                //Manually remove TNT blocks from the world before the actual explosion destroys them
                //(which would cause a chain explosion)
                for(ChunkPosition pos: event.getAffectedBlocks()) {
                    int x = pos.chunkPosX, y = pos.chunkPosY, z = pos.chunkPosZ;
                    Block block = world.getBlock(x, y, z);
                    if(block instanceof BlockTNT) {
                        block.dropBlockAsItemWithChance(world, x, y, z, world.getBlockMetadata(x, y, z), HooksExplosion.getDropChance(event.explosion), 0);
                        world.setBlockToAir(x, y, z);
                    }
                }
            }
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

    public static boolean shouldBePreserved(Block block, int meta) {
        if(Config.spareTileEntities && block.hasTileEntity(meta))
            return true;
        Integer mask = Config.blackWhiteList.get(block);
        boolean matches = mask != null && (mask & (1 << meta)) != 0;
        return Config.listIsWhitelist ? !matches : matches;
    }
}
