package mix.cinematiczoom;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class ZoomKeyMapping extends KeyMapping {

    public ZoomKeyMapping(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        super(name, type, keyCode, category);
    }

    @Override
    public boolean isDown() {
        if (super.isDown()) {
            return true;
        }
        if (isUnbound()) {
            return false;
        }
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getWindow() == null) {
            return false;
        }
        long handle = mc.getWindow().handle();
        if (this.key.getType() == InputConstants.Type.KEYSYM) {
            return GLFW.glfwGetKey(handle, this.key.getValue()) == GLFW.GLFW_PRESS;
        }
        if (this.key.getType() == InputConstants.Type.MOUSE) {
            return GLFW.glfwGetMouseButton(handle, this.key.getValue()) == GLFW.GLFW_PRESS;
        }
        return false;
    }
}
