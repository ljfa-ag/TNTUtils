package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
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
     * Called at the end of the Explosion constructor.
     */
    public static void onExplosionStart(Explosion expl) {
        expl.damageCalculator = new WrappedExplosionDamageCalculator(expl.damageCalculator);
        expl.radius *= TNTUtils.config().sizeMultiplier();
    }

    public static void disarmPrimedTnt(Entity tnt) {
        tnt.discard();
        var itemEntity = new ItemEntity(tnt.level(), tnt.getX(), tnt.getY(), tnt.getZ(), new ItemStack(Items.TNT));
        itemEntity.setDefaultPickUpDelay();
        tnt.level().addFreshEntity(itemEntity);
    }
}
