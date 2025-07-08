package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ExplosionHandler {
    public static boolean shouldCancelExplosion() {
        return TNTUtils.config().disableExplosions();
    }

    public static boolean shouldDamageEntity(Entity entity) {
        var cfg = TNTUtils.config();
        if(
                (cfg.disableEntityDamage()
                || (cfg.disablePlayerDamage() && entity instanceof Player)
                || (cfg.disableMobDamage() && entity instanceof Mob)
                || (entity instanceof ItemEntity ie && shouldSpareItemEntity(ie))
                || entity.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_BLACKLIST))
            && !entity.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_WHITELIST)
        )
            return false;
        else
            return true;
    }

    private static boolean shouldSpareItemEntity(ItemEntity entity) {
        return (TNTUtils.config().disableItemDamage() || entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_BLACKLIST))
                && !entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_WHITELIST);
    }

    public static void disarmPrimedTnt(Entity tnt) {
        tnt.discard();
        var itemEntity = new ItemEntity(tnt.level(), tnt.getX(), tnt.getY(), tnt.getZ(), new ItemStack(Items.TNT));
        itemEntity.setDefaultPickUpDelay();
        tnt.level().addFreshEntity(itemEntity);
    }
}
