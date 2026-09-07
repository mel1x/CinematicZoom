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
import java.util.Arrays;

@Mixin(Options.class)
public class OptionsMixin {
    @Mutable
    @Shadow
    public KeyMapping[] keyMappings;

    @Inject(method = "load", at = @At("HEAD"))
    private void cinematiczoom$onLoad(CallbackInfo ci) {
        cinematiczoom$ensureKeyMapping();
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void cinematiczoom$onInitTail(Minecraft client, File optionsFile, CallbackInfo ci) {
        cinematiczoom$ensureKeyMapping();
    }

    private void cinematiczoom$ensureKeyMapping() {
        if (this.keyMappings == null) {
            return;
        }
        KeyMapping zoomKey = CinematicZoomClient.getKeyMapping();
        for (KeyMapping mapping : this.keyMappings) {
            if (mapping == zoomKey) {
                return;
            }
        }
        KeyMapping[] newMappings = Arrays.copyOf(this.keyMappings, this.keyMappings.length + 1);
        newMappings[this.keyMappings.length] = zoomKey;
        this.keyMappings = newMappings;
    }
}
