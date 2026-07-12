package mix.cinematiczoom.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void cinematiczoom$onTick(CallbackInfo ci) {
        mix.cinematiczoom.ZoomManager.tick(Minecraft.getInstance(), mix.cinematiczoom.CinematicZoomClient.ZOOM_KEYBIND);
    }
}
