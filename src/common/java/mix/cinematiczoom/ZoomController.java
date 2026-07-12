package mix.cinematiczoom;

final class ZoomController {
    private boolean active;
    private float currentMultiplier = 1.0f;
    private float targetMultiplier = 1.0f;
    private float heldMultiplier = ZoomConfig.INSTANCE.baseZoomMultiplier;
    private float currentBarsPercent;
    private float targetBarsPercent;
    private long lastFrameNanos;

    boolean update(boolean shouldZoom) {
        boolean starting = shouldZoom && !active;
        if (starting) {
            heldMultiplier = clamp(
                    ZoomConfig.INSTANCE.baseZoomMultiplier,
                    ZoomConfig.INSTANCE.minZoomMultiplier,
                    ZoomConfig.INSTANCE.maxZoomMultiplier
            );
        }

        active = shouldZoom;
        targetMultiplier = active ? heldMultiplier : 1.0f;
        targetBarsPercent = active ? ZoomConfig.INSTANCE.barsPercent : 0f;
        return starting;
    }

    void reset() {
        active = false;
        currentMultiplier = 1.0f;
        targetMultiplier = 1.0f;
        currentBarsPercent = 0f;
        targetBarsPercent = 0f;
        lastFrameNanos = 0L;
    }

    void updateFrame() {
        long now = System.nanoTime();
        if (lastFrameNanos == 0L) {
            lastFrameNanos = now;
            return;
        }

        double deltaMs = Math.min((now - lastFrameNanos) / 1_000_000.0, 50.0);
        lastFrameNanos = now;

        int smoothMs = ZoomConfig.INSTANCE.smoothMs;
        if (smoothMs <= 0) {
            currentMultiplier = targetMultiplier;
            currentBarsPercent = targetBarsPercent;
            return;
        }

        double tau = smoothMs / 2.302585092994046;
        double alpha = 1.0 - Math.exp(-deltaMs / tau);
        currentMultiplier = (float) lerp(currentMultiplier, targetMultiplier, alpha);
        currentBarsPercent = (float) lerp(currentBarsPercent, targetBarsPercent, alpha);

        if (Math.abs(currentMultiplier - targetMultiplier) < 1e-4f) {
            currentMultiplier = targetMultiplier;
        }
        if (Math.abs(currentBarsPercent - targetBarsPercent) < 1e-3f) {
            currentBarsPercent = targetBarsPercent;
        }
    }

    boolean onWheel(double vertical) {
        if (!active || !ZoomConfig.INSTANCE.mouseWheelEnabled || vertical == 0) {
            return false;
        }

        heldMultiplier += vertical > 0 ? -ZoomConfig.INSTANCE.wheelStep : ZoomConfig.INSTANCE.wheelStep;
        heldMultiplier = clamp(
                heldMultiplier,
                ZoomConfig.INSTANCE.minZoomMultiplier,
                ZoomConfig.INSTANCE.maxZoomMultiplier
        );
        targetMultiplier = heldMultiplier;
        return true;
    }

    double currentMultiplier() {
        return currentMultiplier;
    }

    float currentBarsPercent() {
        return currentBarsPercent;
    }

    private static double lerp(double start, double end, double amount) {
        if (amount <= 0) return start;
        if (amount >= 1) return end;
        return start + (end - start) * amount;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
