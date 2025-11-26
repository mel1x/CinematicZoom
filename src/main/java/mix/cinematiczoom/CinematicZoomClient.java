package mix.cinematiczoom;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.KeyBinding.Category;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CinematicZoomClient implements ClientModInitializer {

    public static final String MODID = "cinematiczoom";
    public static KeyBinding ZOOM_KEYBIND;

    @Override
    public void onInitializeClient() {
        ZoomConfig.INSTANCE.load();

        ZOOM_KEYBIND = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cinematiczoom.zoom",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> ZoomManager.tick(client, ZOOM_KEYBIND));
    }
}
