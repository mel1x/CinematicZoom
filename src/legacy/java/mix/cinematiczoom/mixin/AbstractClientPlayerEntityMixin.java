package mix.cinematiczoom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayerEntity.class)
public class AbstractClientPlayerEntityMixin {

    @ModifyExpressionValue(method = "getFovMultiplier",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingSpyglass()Z"))
    private boolean cinematiczoom$disableSpyglassFov(boolean usingSpyglass) {
        return usingSpyglass && !ZoomManager.suppressVanillaSpyglass();
    }
}
