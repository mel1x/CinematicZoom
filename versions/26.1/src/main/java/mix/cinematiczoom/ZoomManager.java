package mix.cinematiczoom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ZoomManager {
    private static final ZoomController ZOOM = new ZoomController();

    private static boolean hudForcedByUs;
    private static boolean smoothCameraForcedByUs;

    private ZoomManager() {
    }

    public static void tick(Minecraft client, KeyMapping key) {
        boolean canZoom = client.level != null
                && client.player != null
                && client.screen == null
                && client.isWindowActive();
        boolean wantZoom = key.isDown() && canZoom;

        if (ZOOM.update(wantZoom)) {
            acquireOverrides(client);
        }
        if (!wantZoom) {
            // Release even when a key-up transition was missed.
            releaseOverrides(client);
        }
    }

    public static void reset(Minecraft client) {
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

    public static void renderBars(GuiGraphicsExtractor context) {
        float barsPercent = ZOOM.currentBarsPercent();
        if (barsPercent <= 0.0001f) return;

        int width = context.guiWidth();
        int height = context.guiHeight();
        int barHeight = Math.round(height * barsPercent / 100f);
        if (barHeight <= 0) return;

        context.fill(0, 0, width, barHeight, 0xFF000000);
        context.fill(0, height - barHeight, width, height, 0xFF000000);
    }

    private static void acquireOverrides(Minecraft client) {
        if (ZoomConfig.INSTANCE.hideHudDuringZoom && !client.options.hideGui) {
            client.options.hideGui = true;
            hudForcedByUs = true;
        }
        if (ZoomConfig.INSTANCE.enableCinematicCamera && !client.options.smoothCamera) {
            client.options.smoothCamera = true;
            smoothCameraForcedByUs = true;
        }
    }

    private static void releaseOverrides(Minecraft client) {
        if (hudForcedByUs) {
            client.options.hideGui = false;
            hudForcedByUs = false;
        }
        if (smoothCameraForcedByUs) {
            client.options.smoothCamera = false;
            smoothCameraForcedByUs = false;
        }
    }
}
