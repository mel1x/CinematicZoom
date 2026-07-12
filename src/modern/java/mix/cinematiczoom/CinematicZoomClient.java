package mix.cinematiczoom;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public final class CinematicZoomClient implements ClientModInitializer {

    private static final String MODID = "cinematiczoom";
    public static KeyMapping ZOOM_KEYBIND;

    @Override
    public void onInitializeClient() {
        ZoomConfig.INSTANCE.load();

        ZOOM_KEYBIND = new KeyMapping(
                "key.cinematiczoom.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KeyMapping.Category.register(Identifier.parse(MODID + ":cinematiczoom"))
        );

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ZoomManager.reset(client));
        ClientLifecycleEvents.CLIENT_STOPPING.register(ZoomManager::reset);
    }
}
