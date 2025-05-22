package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor
    float getRadius();
    @Accessor
    void setRadius(float radius);

    @Accessor
    ExplosionDamageCalculator getDamageCalculator();
    @Accessor
    void setDamageCalculator(ExplosionDamageCalculator damageCalculator);
}
