package ljfa.tntutils.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import ljfa.tntutils.TNTUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;

@Mixin(TntBlock.class)
public abstract class TntBlockMixin extends Block {
    @Inject(method = "wasExploded", at = @At("HEAD"), cancellable = true)
    private static void onWasExploded(CallbackInfo ci) {
        if(TNTUtils.config().preventChainExplosions())
            ci.cancel();
    }

    @Inject(method = "dropFromExplosion", at = @At("HEAD"), cancellable = true)
    private static void onDropFromExplosion(CallbackInfoReturnable<Boolean> ci) {
        if(TNTUtils.config().preventChainExplosions())
            ci.setReturnValue(true);
    }

    private TntBlockMixin(Properties properties) {
        super(properties);
    }
}
