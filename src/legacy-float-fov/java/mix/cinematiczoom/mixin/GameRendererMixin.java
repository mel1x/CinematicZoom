package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)F",
            at = @At("RETURN"), cancellable = true)
    private void cinematiczoom$applyZoom(Camera camera, float tickDelta, boolean changingFov,
                                         CallbackInfoReturnable<Float> cir) {
        ZoomManager.frameUpdate();

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return;

        float fov = cir.getReturnValue();
        double mul = ZoomManager.getCurrentFovMul();
        if (mul != 1.0) {
            cir.setReturnValue((float) (fov * mul));
        }
    }
}
