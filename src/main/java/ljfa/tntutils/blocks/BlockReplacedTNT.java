package ljfa.tntutils.blocks;

import net.minecraft.block.BlockTNT;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockReplacedTNT extends BlockTNT {
	
	public BlockReplacedTNT() {
		setHardness(0.0F);
		setStepSound(SoundType.PLANT);
		setUnlocalizedName("tnt");
	}
    
    @Override
    public void onBlockDestroyedByExplosion(World world, BlockPos pos, Explosion expl) {
        //do nothing
    }
    
    @Override
    public boolean canDropFromExplosion(Explosion expl) {
        return true;
    };
}
