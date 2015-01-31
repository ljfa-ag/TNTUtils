package ljfa.tntutils.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ModBlocks {
    public static Block replaced_tnt;
    
    public static void preInit() {
        replaced_tnt = new BlockTNT() {
            @Override
            public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion expl) {
                //do nothing
            }
            
            @Override
            public boolean canDropFromExplosion(Explosion expl) {
                return true;
            };
        }.setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("tnt").setBlockTextureName("tnt");
    }
}
