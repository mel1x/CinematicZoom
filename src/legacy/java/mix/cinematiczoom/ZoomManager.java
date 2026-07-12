package mix.cinematiczoom;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;

public class ZoomManager {

    private static boolean zoomHeld = false;

    private static float currentZoomMul = 1.0f;
    private static float targetZoomMul = 1.0f;
    private static float holdZoomMul = ZoomConfig.INSTANCE.baseZoomMultiplier;

    private static float currentBarsPct = 0f;
    private static float targetBarsPct = 0f;

    private static long lastNs = 0L;

    private static Boolean prevHudHidden = null;
    private static Boolean prevSmoothCamera = null;

    public static void tick(MinecraftClient client, KeyBinding key) {
        boolean inScreen = client.currentScreen != null;
        boolean wantZoom = key.isPressed() && !inScreen;

        boolean starting = wantZoom && !zoomHeld;
        boolean ending   = !wantZoom && zoomHeld;

        if (starting) {
            holdZoomMul = clamp(ZoomConfig.INSTANCE.baseZoomMultiplier,
                    ZoomConfig.INSTANCE.minZoomMultiplier,
                    ZoomConfig.INSTANCE.maxZoomMultiplier);

            // Прячем HUD/прицел
            if (ZoomConfig.INSTANCE.hideHudDuringZoom) {
                prevHudHidden = client.options.hudHidden;
                client.options.hudHidden = true;
            }
            // Включаем кинематографичную камеру
            if (ZoomConfig.INSTANCE.enableCinematicCamera) {
                prevSmoothCamera = client.options.smoothCameraEnabled;
                client.options.smoothCameraEnabled = true;
            }
        }

        if (ending) {
            // Вернуть HUD
            if (prevHudHidden != null) {
                client.options.hudHidden = prevHudHidden;
                prevHudHidden = null;
            }
            // Вернуть камеру
            if (prevSmoothCamera != null) {
                client.options.smoothCameraEnabled = prevSmoothCamera;
                prevSmoothCamera = null;
            }
        }

        zoomHeld = wantZoom;

        targetZoomMul = zoomHeld ? holdZoomMul : 1.0f;
        targetBarsPct = zoomHeld ? ZoomConfig.INSTANCE.barsPercent : 0f;
    }

    public static void frameUpdate() {
        final int smooth = ZoomConfig.INSTANCE.smoothMs;

        long now = System.nanoTime();
        if (lastNs == 0L) {
            lastNs = now;
            return;
        }
        double dtMs = (now - lastNs) / 1_000_000.0;
        lastNs = now;
        if (dtMs > 50) dtMs = 50;

        if (smooth <= 0) {
            currentZoomMul = targetZoomMul;
            currentBarsPct = targetBarsPct;
            return;
        }

        final double tau = smooth / 2.302585092994046;
        final double alpha = 1.0 - Math.exp(-dtMs / tau);

        currentZoomMul = (float) lerp(currentZoomMul, targetZoomMul, alpha);
        currentBarsPct = (float) lerp(currentBarsPct, targetBarsPct, alpha);

        if (Math.abs(currentZoomMul - targetZoomMul) < 1e-4f) currentZoomMul = targetZoomMul;
        if (Math.abs(currentBarsPct - targetBarsPct) < 1e-3f) currentBarsPct = targetBarsPct;
    }

    public static boolean isZoomHeld() { return zoomHeld; }
    public static double getCurrentFovMul() { return currentZoomMul; }

    public static boolean onWheel(double vertical) {
        if (!zoomHeld || !ZoomConfig.INSTANCE.mouseWheelEnabled) return false;
        if (vertical == 0) return false;

        float step = ZoomConfig.INSTANCE.wheelStep;
        if (vertical > 0) holdZoomMul -= step; else holdZoomMul += step;

        holdZoomMul = clamp(holdZoomMul, ZoomConfig.INSTANCE.minZoomMultiplier, ZoomConfig.INSTANCE.maxZoomMultiplier);
        return true;
    }

    public static void renderBars(DrawContext ctx) {
        if (currentBarsPct <= 0.0001f) return;

        int sw = ctx.getScaledWindowWidth();
        int sh = ctx.getScaledWindowHeight();

        int h = Math.round(sh * (currentBarsPct / 100f));
        if (h <= 0) return;

        int color = 0xFF000000;
        ctx.fill(0, 0, sw, h, color);
        ctx.fill(0, sh - h, sw, sh, color);
    }

    private static double lerp(double a, double b, double t) {
        if (t <= 0) return a;
        if (t >= 1) return b;
        return a + (b - a) * t;
    }

    private static float clamp(float v, float min, float max) {
        return Math.max(min, Math.min(max, v));
    }
}
