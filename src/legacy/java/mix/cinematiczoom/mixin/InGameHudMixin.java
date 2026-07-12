package mix.cinematiczoom.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Рисуем чёрные полосы всегда, даже когда HUD скрыт (F1).
 * Втыкаемся в самый вход render(...) до любых ранних return.
 */
@Mixin(InGameHud.class)
public class InGameHudMixin {

    // сигнатура для 1.21.7: (DrawContext, RenderTickCounter) -> void
    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At("HEAD"))
    private void cinematiczoom$drawBarsHead(DrawContext ctx, RenderTickCounter tickCounter, CallbackInfo ci) {
        // Тут НЕ делаем frameUpdate(), чтобы не ускорять сглаживание (оно уже вызывается в getFov)
        mix.cinematiczoom.ZoomManager.renderBars(ctx);
    }
}
