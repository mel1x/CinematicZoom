package mix.cinematiczoom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class ZoomConfig {
    public static final ZoomConfig INSTANCE = new ZoomConfig();

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("cinematiczoom.json");

    // --- Настройки пользователя ---
    /** Высота чёрных полос в процентах от высоты экрана (суммарно 2 * value) */
    public float barsPercent = 15.0f;

    /** Скорость плавности: время выхода на ~90% к цели, миллисекунды. 0 = мгновенно */
    public int smoothMs = 240;

    /** Разрешить управление зумом колёсиком мыши во время удержания */
    public boolean mouseWheelEnabled = true;

    /** Скрывать HUD/прицел во время зума */
    public boolean hideHudDuringZoom = true;

    /** Включать "кинематографичную" камеру (smooth camera) во время зума */
    public boolean enableCinematicCamera = true;


    // --- Внутренние (не показываем в Mod Menu, но храним) ---
    /** Базовый множитель FOV при старте зума (меньше 1 = приближение) */
    public float baseZoomMultiplier = 0.33f;

    /** Минимальный/максимальный множитель при колесе */
    public float minZoomMultiplier = 0.10f;
    public float maxZoomMultiplier = 1.00f;

    /** Шаг изменения множителя на один "щелчок" колеса */
    public float wheelStep = 0.05f;
    

    public void load() {
        if (Files.exists(PATH)) {
            try (Reader r = Files.newBufferedReader(PATH)) {
                ZoomConfig loaded = GSON.fromJson(r, ZoomConfig.class);
                if (loaded != null) copyFrom(loaded);
            } catch (Exception ignored) {}
        } else {
            save();
        }
        clamp();
    }

    public void save() {
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer w = Files.newBufferedWriter(PATH)) {
                GSON.toJson(this, w);
            }
        } catch (IOException ignored) {}
    }

    private void copyFrom(ZoomConfig o) {
        this.barsPercent = o.barsPercent;
        this.smoothMs = o.smoothMs;
        this.mouseWheelEnabled = o.mouseWheelEnabled;

        this.baseZoomMultiplier = o.baseZoomMultiplier;
        this.minZoomMultiplier = o.minZoomMultiplier;
        this.maxZoomMultiplier = o.maxZoomMultiplier;
        this.wheelStep = o.wheelStep;

        this.hideHudDuringZoom = o.hideHudDuringZoom;
        this.enableCinematicCamera = o.enableCinematicCamera;
    }

    public void clamp() {
        if (barsPercent < 0f) barsPercent = 0f;
        if (barsPercent > 50f) barsPercent = 50f; // не больше половины экрана
        if (smoothMs < 0) smoothMs = 0;

        if (minZoomMultiplier < 0.05f) minZoomMultiplier = 0.05f;
        if (maxZoomMultiplier > 1.0f) maxZoomMultiplier = 1.0f;
        if (minZoomMultiplier > maxZoomMultiplier) minZoomMultiplier = maxZoomMultiplier;

        if (baseZoomMultiplier < minZoomMultiplier) baseZoomMultiplier = minZoomMultiplier;
        if (baseZoomMultiplier > maxZoomMultiplier) baseZoomMultiplier = maxZoomMultiplier;

        if (wheelStep <= 0f) wheelStep = 0.01f;
        if (wheelStep > 0.25f) wheelStep = 0.25f;
    }
}
