package mix.cinematiczoom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public final class ZoomManager {
    private static final ZoomController ZOOM = new ZoomController();

    private static boolean hudForcedByUs;
    private static boolean smoothCameraForcedByUs;

    private ZoomManager() {
    }

    public static void tick(MinecraftClient client, KeyBinding key) {
        boolean canZoom = client.world != null
                && client.player != null
                && client.currentScreen == null
                && client.isWindowFocused();
        boolean wantZoom = key.isPressed() && canZoom;

        if (ZOOM.update(wantZoom)) {
            acquireOverrides(client);
        }
        if (!wantZoom) {
            // Release even when a key-up transition was missed.
            releaseOverrides(client);
        }
    }

    public static void reset(MinecraftClient client) {
        releaseOverrides(client);
        ZOOM.reset();
    }

    public static void frameUpdate() {
        ZOOM.updateFrame();
    }

    public static double getCurrentFovMul() {
        return ZOOM.currentMultiplier();
    }

    public static boolean onWheel(double vertical) {
        return ZOOM.onWheel(vertical);
    }

    public static void renderBars(DrawContext context) {
        float barsPercent = ZOOM.currentBarsPercent();
        if (barsPercent <= 0.0001f) return;

        int width = context.getScaledWindowWidth();
        int height = context.getScaledWindowHeight();
        int barHeight = Math.round(height * barsPercent / 100f);
        if (barHeight <= 0) return;

        context.fill(0, 0, width, barHeight, 0xFF000000);
        context.fill(0, height - barHeight, width, height, 0xFF000000);
    }

    private static void acquireOverrides(MinecraftClient client) {
        if (ZoomConfig.INSTANCE.hideHudDuringZoom && !client.options.hudHidden) {
            client.options.hudHidden = true;
            hudForcedByUs = true;
        }
        if (ZoomConfig.INSTANCE.enableCinematicCamera && !client.options.smoothCameraEnabled) {
            client.options.smoothCameraEnabled = true;
            smoothCameraForcedByUs = true;
        }
    }

    private static void releaseOverrides(MinecraftClient client) {
        if (hudForcedByUs) {
            client.options.hudHidden = false;
            hudForcedByUs = false;
        }
        if (smoothCameraForcedByUs) {
            client.options.smoothCameraEnabled = false;
            smoothCameraForcedByUs = false;
        }
    }
}
