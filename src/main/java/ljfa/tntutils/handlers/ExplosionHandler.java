package ljfa.tntutils.handlers;

import java.util.ArrayList;
import java.util.function.Predicate;

import ljfa.tntutils.Config;
import net.minecraft.block.Block;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.event.world.ExplosionEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class ExplosionHandler {
    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {

    }

    @SubscribeEvent
    public void onExplosionDetonate(final ExplosionEvent.Detonate event) {
        if(event.world.isRemote)
            return;
        if(Config.disableBlockDamage)
            event.explosion.affectedBlockPositions = new ArrayList();
        else {
            event.getAffectedBlocks().removeIf(new Predicate<ChunkPosition>() {
                @Override
                public boolean test(ChunkPosition pos) {
                    Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                    int meta = event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                    return !shouldBePreserved(block, meta);
                }
            });
        }
    }

    public static boolean shouldBePreserved(Block block, int meta) {
        return Config.spareTileEntities && block.hasTileEntity(meta)
                || Config.blacklistActive && Config.blacklist.contains(block);
    }
}
