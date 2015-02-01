package ljfa.tntutils.handlers;

import java.util.ArrayList;
import java.util.List;

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
        List<ChunkPosition> oldList = event.getAffectedBlocks();
        List<ChunkPosition> newList = new ArrayList<ChunkPosition>(oldList.size());
        for(ChunkPosition pos: oldList) {
            Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
            int meta = event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
            if(!block.hasTileEntity(meta))
                newList.add(pos);
        }
        event.explosion.affectedBlockPositions = newList;
        /*blocks.removeIf(new Predicate<ChunkPosition>() {
            @Override
            public boolean test(ChunkPosition pos) {
                Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                int meta = event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
                return block.hasTileEntity(meta);
            }
        });*/
    }
}
