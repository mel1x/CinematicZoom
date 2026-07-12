package mix.cinematiczoom.mixin;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.DeltaTracker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Рисуем чёрные полосы всегда, даже когда HUD скрыт (F1).
 * Втыкаемся в самый вход render(...) до любых ранних return.
 */
@Mixin(Gui.class)
public class InGameHudMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V",
            at = @At("HEAD"))
    private void cinematiczoom$drawBarsHead(GuiGraphicsExtractor ctx, DeltaTracker tickCounter, CallbackInfo ci) {
        // Тут НЕ делаем frameUpdate(), чтобы не ускорять сглаживание (оно уже вызывается в getFov)
        mix.cinematiczoom.ZoomManager.renderBars(ctx);
    }
}
