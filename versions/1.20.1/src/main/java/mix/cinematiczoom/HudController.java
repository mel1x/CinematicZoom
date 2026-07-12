package mix.cinematiczoom;

import net.minecraft.client.MinecraftClient;

final class HudController {
    private static boolean hiddenByUs;

    private HudController() {
    }

    static void acquire(MinecraftClient client) {
        hiddenByUs = ZoomConfig.INSTANCE.hideHudDuringZoom;
    }

    static void release(MinecraftClient client) {
        hiddenByUs = false;
    }

    static boolean shouldHideHud() {
        return hiddenByUs;
    }
}
