package mix.cinematiczoom;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import com.mojang.blaze3d.platform.InputConstants;
import org.lwjgl.glfw.GLFW;

public class CinematicZoomClient implements ClientModInitializer {

    public static final String MODID = "cinematiczoom";
    public static KeyMapping ZOOM_KEYBIND;

    @Override
    public void onInitializeClient() {
        ZoomConfig.INSTANCE.load();

        ZOOM_KEYBIND = new KeyMapping(
                "key.cinematiczoom.zoom",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_C,
                KeyMapping.Category.register(Identifier.parse("cinematiczoom:cinematiczoom"))
        );
    }
}
