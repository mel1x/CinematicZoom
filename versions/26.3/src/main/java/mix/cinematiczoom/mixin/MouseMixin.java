package mix.cinematiczoom.mixin;

import com.mojang.blaze3d.Blaze3D;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow public abstract boolean isMouseGrabbed();
    @Shadow protected abstract void turnPlayer(double timeDelta);

    @Unique private double cinematiczoom$lastInertiaTime;

    @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (minecraft.gui.screen() != null) return;

        if (ZoomManager.onWheel(vertical)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleAccumulatedMovement()V", at = @At("TAIL"))
    private void cinematiczoom$continueInertia(CallbackInfo ci) {
        if (!isMouseGrabbed() && minecraft.player != null && (ZoomManager.isZoomActive() || minecraft.options.smoothCamera)) {
            double now = Blaze3D.getTime();
            double dt = now - cinematiczoom$lastInertiaTime;
            cinematiczoom$lastInertiaTime = now;
            if (dt > 0.0 && dt < 0.2) {
                turnPlayer(dt);
            }
        } else {
            cinematiczoom$lastInertiaTime = Blaze3D.getTime();
        }
    }
}
