package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;

@Mixin(Explosion.class)
public interface ExplosionAccessor {
    @Accessor
    float getRadius();
    @Accessor
    @Mutable
    void setRadius(float radius);

    @Accessor
    ExplosionDamageCalculator getDamageCalculator();
    @Accessor
    @Mutable
    void setDamageCalculator(ExplosionDamageCalculator damageCalculator);
}
