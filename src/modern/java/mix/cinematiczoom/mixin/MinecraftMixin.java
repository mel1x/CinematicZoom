package mix.cinematiczoom.mixin;

import mix.cinematiczoom.CinematicZoomClient;
import mix.cinematiczoom.ZoomManager;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void cinematiczoom$onTick(CallbackInfo ci) {
        ZoomManager.tick(Minecraft.getInstance(), CinematicZoomClient.ZOOM_KEYBIND);
    }
}
