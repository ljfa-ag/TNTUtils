package ljfa.tntutils.handlers;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ExplosionHandler {
    public static boolean shouldCancelExplosion() {
        return TNTUtils.config().disableExplosions();
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
