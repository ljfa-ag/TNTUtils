package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.mixin.ExplosionAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;

public class ExplosionHandler {
    public static boolean shouldCancelExplosion() {
        return TNTUtils.config().disableExplosions();
    }

    /**
     * Called as or just before {@link Explosion#explode()} is called.
     */
    public static void onExplosionStart(Explosion expl) {
        var explA = (ExplosionAccessor) expl;
        explA.setDamageCalculator(new WrappedExplosionDamageCalculator(explA.getDamageCalculator()));
        explA.setRadius(explA.getRadius() * TNTUtils.config().sizeMultiplier());
    }

    public static void disarmPrimedTnt(Entity tnt) {
        tnt.discard();
        var itemEntity = new ItemEntity(tnt.level(), tnt.getX(), tnt.getY(), tnt.getZ(), new ItemStack(Items.TNT));
        itemEntity.setDefaultPickUpDelay();
        tnt.level().addFreshEntity(itemEntity);
    }
}
