package mix.cinematiczoom;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;

public class ZoomKeyMapping extends KeyMapping {

    public ZoomKeyMapping(String name, InputConstants.Type type, int keyCode, KeyMapping.Category category) {
        super(name, type, keyCode, category);
    }

}
