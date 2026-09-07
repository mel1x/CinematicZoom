package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public abstract class MouseMixin {

    @Shadow @Final private MinecraftClient client;
    @Shadow public abstract boolean isCursorLocked();
    @Shadow private void updateMouse(double timeDelta) { throw new AssertionError(); }

    @Unique private double cinematiczoom$lastInertiaTime;

    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (client.currentScreen != null) return;

        if (ZoomManager.onWheel(vertical)) {
            ci.cancel();
        }
    }

    @Inject(method = "tick()V", at = @At("TAIL"))
    private void cinematiczoom$continueInertia(CallbackInfo ci) {
        if (!isCursorLocked() && client.player != null && (ZoomManager.isZoomActive() || client.options.smoothCameraEnabled)) {
            double now = GLFW.glfwGetTime();
            double dt = now - cinematiczoom$lastInertiaTime;
            cinematiczoom$lastInertiaTime = now;
            if (dt > 0.0 && dt < 0.2) {
                updateMouse(dt);
            }
        } else {
            cinematiczoom$lastInertiaTime = GLFW.glfwGetTime();
        }
    }
}
