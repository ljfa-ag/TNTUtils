package ljfa.tntutils.asm;

import net.minecraft.world.Explosion;

public class HooksExplosion {
    public static float getDropChance(Explosion expl) {
        return 1.0f / expl.explosionSize;
    }
}
