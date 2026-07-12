package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseMixin {

    @Shadow @Final private Minecraft minecraft;

    /**
     * Перехватываем колёсико: во время удержания зума — управляем множителем и блокируем хотбар-скролл.
     */
    @Inject(method = "onScroll(JDD)V", at = @At("HEAD"), cancellable = true)
    private void cinematiczoom$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (minecraft == null) return;
        if (minecraft.screen != null) return; // в GUI не мешаем

        if (ZoomManager.onWheel(vertical)) {
            ci.cancel(); // отменяем стандартный обработчик (чтобы не листался хотбар)
        }
    }
}
