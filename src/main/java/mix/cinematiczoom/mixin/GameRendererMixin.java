package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @ModifyVariable(method = "extractGui(Lnet/minecraft/client/DeltaTracker;ZZ)V", at = @At("STORE"), ordinal = 0)
    private GuiGraphicsExtractor cinematiczoom$drawBarsFirst(GuiGraphicsExtractor ctx) {
        // Мы рендерим полосы сразу после создания GuiGraphicsExtractor
        // Это гарантирует, что они будут позади всех остальных элементов GUI (включая hotbar),
        // но при этом они будут отрендерены даже если остальной GUI скрыт (renderHud = false).
        ZoomManager.renderBars(ctx);
        return ctx;
    }
}
