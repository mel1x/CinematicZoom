package mix.cinematiczoom;

import net.minecraft.client.MinecraftClient;

final class HudController {
    private static boolean hiddenByUs;

    private HudController() {
    }

    static void acquire(MinecraftClient client) {
        if (ZoomConfig.INSTANCE.hideHudDuringZoom && !client.options.hudHidden) {
            client.options.hudHidden = true;
            hiddenByUs = true;
        }
    }

    static void release(MinecraftClient client) {
        if (hiddenByUs) {
            client.options.hudHidden = false;
            hiddenByUs = false;
        }
    }

    static boolean shouldHideHud() {
        return false;
    }
}
