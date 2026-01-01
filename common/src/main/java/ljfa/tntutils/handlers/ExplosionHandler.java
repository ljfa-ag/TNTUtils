package ljfa.tntutils.handlers;

import org.jspecify.annotations.Nullable;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import ljfa.tntutils.command.ExplodeCommand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;

public class ExplosionHandler {
    public static boolean shouldAllowExplosion(@Nullable Entity directSource) {
        if(ExplodeCommand.isCurrentlyRunning())
            return true;

        if(directSource == null) // shortcut the following tests
            return !TNTUtils.config().disableExplosions();

        var indirectSource = Explosion.getIndirectSourceEntity(directSource); // may still be null even when directSource isn't
        boolean disallowForType =
                (TNTUtils.config().disableExplosions()
                || directSource.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_SOURCE_BLACKLIST)
                || indirectSource != null && indirectSource.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_SOURCE_BLACKLIST))
            && !directSource.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_SOURCE_WHITELIST)
            && !(indirectSource != null && indirectSource.getType().is(TNTUtilsTags.ENTITY_TYPE_EXPLOSION_SOURCE_WHITELIST));
        if(
                (disallowForType
                || directSource.getTags().contains(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_BLACKLIST)
                || indirectSource != null && indirectSource.getTags().contains(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_BLACKLIST))
            && !directSource.getTags().contains(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_WHITELIST)
            && !(indirectSource != null && indirectSource.getTags().contains(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_WHITELIST))
        )
            return false;
        return true;
    }

    public static void disarmPrimedTnt(PrimedTnt tnt) {
        tnt.discard();
        var item = tnt.getBlockState().getBlock().asItem();
        if(item == Items.AIR)
            item = Items.TNT;
        var itemEntity = new ItemEntity(tnt.level(), tnt.getX(), tnt.getY(), tnt.getZ(), new ItemStack(item));
        itemEntity.setDefaultPickUpDelay();
        tnt.level().addFreshEntity(itemEntity);
    }
}
