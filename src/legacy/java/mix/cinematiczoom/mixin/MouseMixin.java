package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    /**
     * Перехватываем колёсико: во время удержания зума — управляем множителем и блокируем хотбар-скролл.
     */
    @Inject(method = "onMouseScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (client == null) return;
        if (client.currentScreen != null) return; // в GUI не мешаем

        if (ZoomManager.onWheel(vertical)) {
            ci.cancel(); // отменяем стандартный обработчик (чтобы не листался хотбар)
        }
    }
}
