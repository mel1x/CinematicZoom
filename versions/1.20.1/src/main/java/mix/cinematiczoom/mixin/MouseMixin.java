package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.SmoothUtil;
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
    @Shadow @Final private SmoothUtil cursorXSmoother;
    @Shadow @Final private SmoothUtil cursorYSmoother;

    @Unique private double cinematiczoom$lastInertiaTime;

    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (client.currentScreen != null) return;

        if (ZoomManager.onWheel(vertical)) {
            ci.cancel();
        }
    }

    @Inject(method = "updateMouse()V", at = @At("RETURN"))
    private void cinematiczoom$continueInertia(CallbackInfo ci) {
        if (!isCursorLocked() && client.player != null && (ZoomManager.isZoomActive() || client.options.smoothCameraEnabled)) {
            double now = GLFW.glfwGetTime();
            double dt = now - cinematiczoom$lastInertiaTime;
            cinematiczoom$lastInertiaTime = now;
            if (dt > 0.0 && dt < 0.2) {
                double sens = this.client.options.getMouseSensitivity().getValue() * 0.6 + 0.2;
                double mult = sens * sens * sens * 8.0;
                double dx = this.cursorXSmoother.smooth(0.0, dt * mult);
                double dy = this.cursorYSmoother.smooth(0.0, dt * mult);
                int ySign = this.client.options.getInvertYMouse().getValue() ? -1 : 1;
                this.client.player.changeLookDirection(dx, dy * ySign);
            }
        } else {
            cinematiczoom$lastInertiaTime = GLFW.glfwGetTime();
        }
    }
}
