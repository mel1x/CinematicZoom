package mix.cinematiczoom.mixin;

import mix.cinematiczoom.CinematicZoomClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(Options.class)
public class OptionsMixin {
    @Mutable
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cinematiczoom$addKeyBinding(Minecraft client, File optionsFile, CallbackInfo ci) {
        if (CinematicZoomClient.ZOOM_KEYBIND != null) {
            KeyMapping[] newMappings = new KeyMapping[this.keyMappings.length + 1];
            System.arraycopy(this.keyMappings, 0, newMappings, 0, this.keyMappings.length);
            newMappings[this.keyMappings.length] = CinematicZoomClient.ZOOM_KEYBIND;
            this.keyMappings = newMappings;
        }
    }
}
