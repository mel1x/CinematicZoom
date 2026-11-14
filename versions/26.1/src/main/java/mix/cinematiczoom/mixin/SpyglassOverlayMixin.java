package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)
public class SpyglassOverlayMixin {

    @Inject(method = "extractSpyglassOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;F)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$hideSpyglassOverlay(GuiGraphicsExtractor context, float scale, CallbackInfo ci) {
        if (ZoomManager.suppressVanillaSpyglass()) {
            ci.cancel();
        }
    }
}
