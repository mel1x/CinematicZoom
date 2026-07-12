package mix.cinematiczoom.mixin;

import net.minecraft.client.Options;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Options.class)
public class OptionsMixin {
    @Mutable
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cinematiczoom$addKeyBinding(net.minecraft.client.Minecraft client, java.io.File optionsFile, CallbackInfo ci) {
        if (mix.cinematiczoom.CinematicZoomClient.ZOOM_KEYBIND != null) {
            KeyMapping[] newMappings = new KeyMapping[this.keyMappings.length + 1];
            System.arraycopy(this.keyMappings, 0, newMappings, 0, this.keyMappings.length);
            newMappings[this.keyMappings.length] = mix.cinematiczoom.CinematicZoomClient.ZOOM_KEYBIND;
            this.keyMappings = newMappings;
        }
    }
}
