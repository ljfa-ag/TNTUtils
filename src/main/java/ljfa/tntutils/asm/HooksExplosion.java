package ljfa.tntutils.asm;

import ljfa.tntutils.Config;
import net.minecraft.world.Explosion;

public class HooksExplosion {
    public static final float alwaysDropThreshold = 10.0f;
    
    public static float getDropChance(Explosion expl) {
        float baseChance = 1.0f / expl.size;
        if(Config.dropChanceModifier == 0.0f)
            return baseChance;
        else if(Config.dropChanceModifier > 0.0f) {
            if(expl.size <= alwaysDropThreshold)
                return baseChance + Config.dropChanceModifier * (1.0f - baseChance);
            else
                return baseChance;
        } else
            return (1.0f + Config.dropChanceModifier) * baseChance;
    }
}
