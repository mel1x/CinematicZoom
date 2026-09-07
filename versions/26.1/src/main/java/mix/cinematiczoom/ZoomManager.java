package mix.cinematiczoom;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class ZoomManager {
    private static final ZoomController ZOOM = new ZoomController();

    private static boolean hudForcedByUs;
    private static boolean smoothCameraForcedByUs;
    private static boolean toggled;
    private static boolean wasKeyDown;

    private ZoomManager() {
    }

    public static void tick(Minecraft client, KeyMapping key) {
        boolean inWorld = client.level != null && client.player != null;
        boolean canInteract = inWorld
                && client.screen == null
                && client.isWindowActive();

        boolean isKeyDown = canInteract && isZoomKeyDown(client, key);
        boolean wantZoom;

        if (ZoomConfig.INSTANCE.toggleMode) {
            if (isKeyDown && !wasKeyDown) {
                toggled = !toggled;
            }
            if (!inWorld) {
                toggled = false;
            }
            wantZoom = toggled && inWorld;
        } else {
            toggled = false;
            wantZoom = isKeyDown && canInteract;
        }
        wasKeyDown = isKeyDown;

        if (ZOOM.update(wantZoom)) {
            acquireOverrides(client);
        }
        if (!wantZoom) {
            releaseOverrides(client);
        }
    }

    private static boolean isZoomKeyDown(Minecraft client, KeyMapping key) {
        return key != null && key.isDown();
    }

    public static void reset(Minecraft client) {
        toggled = false;
        wasKeyDown = false;
        releaseOverrides(client);
        ZOOM.reset();
    }

    public static void frameUpdate() {
        ZOOM.updateFrame();
    }

    public static double getCurrentFovMul() {
        return ZOOM.currentMultiplier();
    }

    public static boolean isZoomActive() {
        return ZOOM.isActive();
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
