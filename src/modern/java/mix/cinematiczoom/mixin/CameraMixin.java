package mix.cinematiczoom.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = Camera.class, priority = 1500)
public class CameraMixin {

    @WrapMethod(method = "calculateFov(F)F")
    private float cinematiczoom$applyZoom(float partialTicks, Operation<Float> original) {
        float fov = original.call(partialTicks);
        ZoomManager.frameUpdate();

        double mul = ZoomManager.getCurrentFovMul();
        return mul != 1.0 ? (float) (fov * mul) : fov;
    }
}
