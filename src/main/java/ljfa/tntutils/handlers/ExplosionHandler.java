package ljfa.tntutils.handlers;

import java.util.function.Predicate;

import ljfa.tntutils.Config;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.event.world.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExplosionHandler {
    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
        if(Config.disableExplosions)
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        if(event.world.isRemote)
            return;
        //Block damage
        if(Config.disableBlockDamage || (Config.disableCreeperBlockDamage && event.explosion.exploder instanceof EntityCreeper))
            event.explosion.affectedBlockPositions.clear();
        else if(Config.spareTileEntities || Config.blacklistActive) {
            //Remove blacklisted blocks and tile entities (if configured) from the list
            event.getAffectedBlocks().removeIf(new Predicate<ChunkPosition>() {
                @Override
                public boolean test(ChunkPosition pos) {
                    Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                    int meta = event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                    return shouldBePreserved(block, meta);
                }
            });
        }
        
        //Entity damage
        if(Config.disableEntityDamage)
            event.getAffectedEntities().clear();
        else if(Config.disableNPCDamage) {
            //Remove non-players (if configured) from the list
            event.getAffectedEntities().removeIf(new Predicate<Entity>() {
                @Override
                public boolean test(Entity ent) {
                    return !(ent instanceof EntityPlayer);
                }
            });
        }
    }

    public static boolean shouldBePreserved(Block block, int meta) {
        return Config.spareTileEntities && block.hasTileEntity(meta)
            || Config.blacklistActive && Config.blacklist.contains(block);
    }
}
