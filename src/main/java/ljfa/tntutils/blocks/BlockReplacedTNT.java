package ljfa.tntutils.blocks;

import net.minecraft.block.BlockTNT;
import net.minecraft.util.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockReplacedTNT extends BlockTNT {

    /*@Override
    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
    }
    
    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, Block block) {
        super.onNeighborBlockChange(world, x, y, z, block);
    }*/
    
    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion expl) {
        //do nothing
    }
    
    /*@Override
    public void onBlockDestroyedByPlayer(World world, int x, int y, int z, int meta) {
        super.onBlockDestroyedByPlayer(world, x, y, z, meta);
    }*/
    
    /** Spawns primed TNT */
    /*@Override
    public void func_150114_a(World world, int x, int y, int z, int meta, EntityLivingBase exploder) {
        super.func_150114_a(world, x, y, z, meta, exploder);
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float fx, float fy, float fz) {
        return super.onBlockActivated(world, x, y, z, player, side, fx, fy, fz);
    }
    
    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        super.onEntityCollidedWithBlock(world, x, y, z, entity);
    }*/
    
    @Override
    public boolean canDropFromExplosion(Explosion expl) {
        return true;
    };
}
