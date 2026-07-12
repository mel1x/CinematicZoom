package mix.cinematiczoom.mixin;

import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Gui.class)
public class GuiMixin {

    @ModifyVariable(method = "extractRenderState(Lnet/minecraft/client/DeltaTracker;ZZ)V", at = @At("STORE"), ordinal = 0)
    private GuiGraphicsExtractor cinematiczoom$drawBarsFirst(GuiGraphicsExtractor ctx) {
        // Draw bars behind the GUI, including when the HUD is hidden.
        ZoomManager.renderBars(ctx);
        return ctx;
    }
}
