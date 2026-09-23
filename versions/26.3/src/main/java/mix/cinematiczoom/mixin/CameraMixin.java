package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public class CameraMixin {

    @Inject(method = "calculateFov(F)F", at = @At("RETURN"), cancellable = true)
    private void cinematiczoom$applyZoom(float partialTicks, CallbackInfoReturnable<Float> cir) {
        ZoomManager.frameUpdate();

        float fov = cir.getReturnValue();
        double mul = ZoomManager.getCurrentFovMul();
        if (mul != 1.0) {
            cir.setReturnValue((float) (fov * mul));
        }
    }
}
