package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;

public class ExplosionHandler {
    public static boolean shouldAllowExplosion(Explosion explosion) {
        /*if(ExplodeCommand.isCurrentlyRunning())
            return true;*/

        var directSource = explosion.getDirectSourceEntity();
        if(directSource == null) // shortcut the following tests
            return !TNTUtils.config().disableExplosions();

        var indirectSource = explosion.getIndirectSourceEntity(); // may still be null even when directSource isn't
        boolean disallowForType =
                (TNTUtils.config().disableExplosions()
                || directSource.getType().is(TNTUtilsTags.ENTITY_TYPE_DENY_EXPLOSIONS)
                || indirectSource != null && indirectSource.getType().is(TNTUtilsTags.ENTITY_TYPE_DENY_EXPLOSIONS))
            && !directSource.getType().is(TNTUtilsTags.ENTITY_TYPE_ALLOW_EXPLOSIONS)
            && !(indirectSource != null && indirectSource.getType().is(TNTUtilsTags.ENTITY_TYPE_ALLOW_EXPLOSIONS));
        if(
                (disallowForType
                || directSource.getTags().contains(TNTUtilsTags.ENTITY_DENY_EXPLOSIONS)
                || indirectSource != null && indirectSource.getTags().contains(TNTUtilsTags.ENTITY_DENY_EXPLOSIONS))
            && !directSource.getTags().contains(TNTUtilsTags.ENTITY_ALLOW_EXPLOSIONS)
            && !(indirectSource != null && indirectSource.getTags().contains(TNTUtilsTags.ENTITY_ALLOW_EXPLOSIONS))
        )
            return false;
        return true;
    }

    public static boolean shouldAllowTnt(Entity owner) {
        if(owner == null) // shortcut the following tests
            return !TNTUtils.config().disableTNT();

        boolean disallowForType =
                (TNTUtils.config().disableTNT() || owner.getType().is(TNTUtilsTags.ENTITY_TYPE_DENY_EXPLOSIONS))
            && !owner.getType().is(TNTUtilsTags.ENTITY_TYPE_ALLOW_EXPLOSIONS);
        if(
                (disallowForType || owner.getTags().contains(TNTUtilsTags.ENTITY_DENY_EXPLOSIONS))
            && !owner.getTags().contains(TNTUtilsTags.ENTITY_ALLOW_EXPLOSIONS)
        )
            return false;
        return true;
    }

    public static boolean shouldDamageEntity(Entity entity) {
        if(entity instanceof ItemEntity ie)
            return shouldDamageItemEntity(ie);

        var cfg = TNTUtils.config();
        if(
                (cfg.disableEntityDamage()
                || (cfg.disablePlayerDamage() && entity instanceof Player)
                || (cfg.disableMobDamage() && entity instanceof Mob)
                || entity.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_BLACKLIST))
            && !entity.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_WHITELIST)
        )
            return false;
        else
            return true;
    }

    private static boolean shouldDamageItemEntity(ItemEntity entity) {
        var cfg = TNTUtils.config();
        if(
                (cfg.disableEntityDamage()
                || cfg.disableItemDamage()
                || entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_BLACKLIST)
                || entity.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_BLACKLIST)) //for modded ItemEntity types
            && !entity.getItem().is(TNTUtilsTags.ITEM_EXPLOSION_WHITELIST)
            && !entity.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_WHITELIST) //for modded ItemEntity types
        )
            return false;
        else
            return true;
    }

    public static void disarmPrimedTnt(Entity tnt) {
        tnt.discard();
        var itemEntity = new ItemEntity(tnt.level(), tnt.getX(), tnt.getY(), tnt.getZ(), new ItemStack(Items.TNT));
        itemEntity.setDefaultPickUpDelay();
        tnt.level().addFreshEntity(itemEntity);
    }
}
