package mix.cinematiczoom;

final class ZoomController {
    private static final double LN10 = 2.302585092994046;

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
        heldMultiplier = ZoomConfig.INSTANCE.baseZoomMultiplier;
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

        double deltaMs = Math.min((now - lastFrameNanos) * 1e-6, 50.0);
        lastFrameNanos = now;

        int smoothMs = ZoomConfig.INSTANCE.smoothMs;
        if (smoothMs <= 0) {
            currentMultiplier = targetMultiplier;
            currentBarsPercent = targetBarsPercent;
            return;
        }

        if (currentMultiplier == targetMultiplier && currentBarsPercent == targetBarsPercent) {
            return;
        }

        double tau = smoothMs / LN10;
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
        if (!active || !ZoomConfig.INSTANCE.mouseWheelEnabled || vertical == 0.0) {
            return false;
        }

        heldMultiplier = clamp(
                heldMultiplier - (float) vertical * ZoomConfig.INSTANCE.wheelStep,
                ZoomConfig.INSTANCE.minZoomMultiplier,
                ZoomConfig.INSTANCE.maxZoomMultiplier
        );
        targetMultiplier = heldMultiplier;
        return true;
    }

    double currentMultiplier() {
        return currentMultiplier;
    }

    boolean isActive() {
        return active || Math.abs(currentMultiplier - 1.0f) > 1e-4f;
    }

    float currentBarsPercent() {
        return currentBarsPercent;
    }

    private static double lerp(double start, double end, double amount) {
        return start + (end - start) * amount;
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
}
