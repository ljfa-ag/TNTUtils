package ljfa.tntutils.asm;

import ljfa.tntutils.Config;
import net.minecraft.world.Explosion;

public class HooksExplosion {
    public static final float alwaysDropThreshold = 7.0f;
    
    public static float getDropChance(Explosion expl) {
        if(Config.alwaysDropItems && expl.explosionSize <= alwaysDropThreshold)
            return 1.0f;
        else
            return 1.0f / expl.explosionSize;
    }
}
