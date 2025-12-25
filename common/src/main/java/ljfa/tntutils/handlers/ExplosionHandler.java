package ljfa.tntutils.handlers;

import org.jspecify.annotations.Nullable;

import ljfa.tntutils.TNTUtils;
import ljfa.tntutils.TNTUtilsTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;

public class ExplosionHandler {
    public static boolean shouldCancelExplosion(@Nullable Entity directSource) {
        if(TNTUtils.config().disableExplosions())
            return true;
        if(directSource != null && directSource.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_BLACKLIST))
            return true;
        var indirectSource = Explosion.getIndirectSourceEntity(directSource);
        if(indirectSource != null && indirectSource.getType().is(TNTUtilsTags.ENTITY_EXPLOSION_SOURCE_BLACKLIST))
            return true;
        return false;
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
