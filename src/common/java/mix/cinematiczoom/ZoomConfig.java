package mix.cinematiczoom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ZoomConfig {
    public static final ZoomConfig INSTANCE = new ZoomConfig();

    private static final Logger LOGGER = LoggerFactory.getLogger("cinematiczoom");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("cinematiczoom.json");

    // User-facing options.
    public float barsPercent = 15.0f;
    public int smoothMs = 240;
    public boolean mouseWheelEnabled = true;
    public boolean hideHudDuringZoom = true;
    public boolean enableCinematicCamera = true;

    // Advanced values kept in the config file.
    public float baseZoomMultiplier = 0.33f;
    public float minZoomMultiplier = 0.10f;
    public float maxZoomMultiplier = 1.00f;
    public float wheelStep = 0.05f;

    public void load() {
        if (!Files.exists(PATH)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(PATH)) {
            ZoomConfig loaded = GSON.fromJson(reader, ZoomConfig.class);
            if (loaded != null) {
                copyFrom(loaded);
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load config from {}", PATH, exception);
        }
        clamp();
    }

    public void save() {
        clamp();
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            LOGGER.warn("Failed to save config to {}", PATH, exception);
        }
    }

    public void clamp() {
        barsPercent = Math.max(0f, Math.min(50f, barsPercent));
        smoothMs = Math.max(0, smoothMs);

        maxZoomMultiplier = Math.max(0.05f, Math.min(1.0f, maxZoomMultiplier));
        minZoomMultiplier = Math.max(0.05f, Math.min(maxZoomMultiplier, minZoomMultiplier));
        baseZoomMultiplier = Math.max(minZoomMultiplier, Math.min(maxZoomMultiplier, baseZoomMultiplier));
        wheelStep = Math.max(0.01f, Math.min(0.25f, wheelStep));
    }

    private void copyFrom(ZoomConfig other) {
        barsPercent = other.barsPercent;
        smoothMs = other.smoothMs;
        mouseWheelEnabled = other.mouseWheelEnabled;
        hideHudDuringZoom = other.hideHudDuringZoom;
        enableCinematicCamera = other.enableCinematicCamera;
        baseZoomMultiplier = other.baseZoomMultiplier;
        minZoomMultiplier = other.minZoomMultiplier;
        maxZoomMultiplier = other.maxZoomMultiplier;
        wheelStep = other.wheelStep;
    }
}
