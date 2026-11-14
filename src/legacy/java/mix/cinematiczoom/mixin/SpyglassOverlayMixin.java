package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class SpyglassOverlayMixin {

    @Inject(method = "renderSpyglassOverlay(Lnet/minecraft/client/gui/DrawContext;F)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$hideSpyglassOverlay(DrawContext context, float scale, CallbackInfo ci) {
        if (ZoomManager.suppressVanillaSpyglass()) {
            ci.cancel();
        }
    }
}
