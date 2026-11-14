package mix.cinematiczoom.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {

    @ModifyExpressionValue(method = "getFieldOfViewModifier",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/AbstractClientPlayer;isScoping()Z"))
    private boolean cinematiczoom$disableSpyglassFov(boolean scoping) {
        return scoping && !ZoomManager.suppressVanillaSpyglass();
    }
}
