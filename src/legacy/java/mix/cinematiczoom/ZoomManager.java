package mix.cinematiczoom;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public final class ZoomManager {
    private static final ZoomController ZOOM = new ZoomController();

    private static boolean smoothCameraForcedByUs;
    private static boolean toggled;
    private static boolean wasKeyDown;

    private ZoomManager() {
    }

    public static void tick(MinecraftClient client, KeyBinding key) {
        boolean inWorld = client.world != null && client.player != null;
        boolean canInteract = inWorld
                && client.currentScreen == null
                && client.isWindowFocused();

        boolean isKeyDown = canInteract && isZoomKeyPressed(client, key);
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

    private static boolean isZoomKeyPressed(MinecraftClient client, KeyBinding key) {
        if (key == null) {
            return false;
        }
        if (key.isPressed()) {
            return true;
        }
        if (key.isUnbound() || client.getWindow() == null) {
            return false;
        }
        InputUtil.Key boundKey = KeyBindingHelper.getBoundKeyOf(key);
        if (boundKey.getCode() == InputUtil.UNKNOWN_KEY.getCode()) {
            return false;
        }
        long handle = client.getWindow().getHandle();
        if (boundKey.getCategory() == InputUtil.Type.KEYSYM) {
            return GLFW.glfwGetKey(handle, boundKey.getCode()) == GLFW.GLFW_PRESS;
        }
        if (boundKey.getCategory() == InputUtil.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(handle, boundKey.getCode()) == GLFW.GLFW_PRESS;
        }
        return false;
    }

    public static void reset(MinecraftClient client) {
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

    public static boolean shouldHideHud() {
        return HudController.shouldHideHud();
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
        HudController.acquire(client);
        if (ZoomConfig.INSTANCE.enableCinematicCamera && !client.options.smoothCameraEnabled) {
            client.options.smoothCameraEnabled = true;
            smoothCameraForcedByUs = true;
        }
    }

    private static void releaseOverrides(MinecraftClient client) {
        HudController.release(client);
        if (smoothCameraForcedByUs) {
            client.options.smoothCameraEnabled = false;
            smoothCameraForcedByUs = false;
        }
    }
}
