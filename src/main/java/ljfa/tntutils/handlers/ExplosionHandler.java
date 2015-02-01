package ljfa.tntutils.handlers;

import java.util.List;
import java.util.function.Predicate;

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
        List<ChunkPosition> blocks = event.getAffectedBlocks();
        blocks.removeIf(new Predicate<ChunkPosition>() {
            @Override
            public boolean test(ChunkPosition pos) {
                Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                int meta = event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                return block.hasTileEntity(meta);
            }
        });
    }
}
