package mix.cinematiczoom.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = GameRenderer.class, priority = 1500)
public class GameRendererMixin {

    @WrapMethod(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)F")
    private float cinematiczoom$applyZoom(Camera camera, float tickDelta, boolean changingFov, Operation<Float> original) {
        float fov = original.call(camera, tickDelta, changingFov);
        ZoomManager.frameUpdate();

        double mul = ZoomManager.getCurrentFovMul();
        return mul != 1.0 ? (float) (fov * mul) : fov;
    }
}
