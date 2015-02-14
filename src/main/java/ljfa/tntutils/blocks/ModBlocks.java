package ljfa.tntutils.blocks;

import net.minecraft.block.Block;

public class ModBlocks {
    public static Block replaced_tnt;
    
    public static void preInit() {
        replaced_tnt = new BlockReplacedTNT().setHardness(0.0F).setStepSound(Block.soundTypeGrass).setBlockName("tnt").setBlockTextureName("tnt");
    }
}
