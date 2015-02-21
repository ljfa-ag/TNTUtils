package ljfa.tntutils.asm;

import ljfa.tntutils.Config;
import ljfa.tntutils.util.ReflectionHelper;
import net.minecraft.world.Explosion;

public class HooksExplosion {
    public static final float alwaysDropThreshold = 10.0f;
    
    public static float getDropChance(Explosion expl) throws ReflectiveOperationException {
        //TODO: Use Access Transformer
        float explSize = (Float)ReflectionHelper.getField(Explosion.class, "explosionSize", expl);
        float baseChance = 1.0f / explSize;
        if(explSize <= alwaysDropThreshold)
            return baseChance + Config.dropChanceModifier * (1.0f - baseChance);
        else
            return baseChance;
    }
}
