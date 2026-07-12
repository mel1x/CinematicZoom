package mix.cinematiczoom.integrations;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.text.Text;
import mix.cinematiczoom.ZoomConfig;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ZoomConfig cfg = ZoomConfig.INSTANCE;

            ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("cinematiczoom.config.title"))
                .setSavingRunnable(() -> { cfg.clamp(); cfg.save(); });

            ConfigCategory cat = builder.getOrCreateCategory(Text.translatable("cinematiczoom.config.category.general"));
            ConfigEntryBuilder eb = builder.entryBuilder();

            cat.addEntry(
                eb.startFloatField(Text.translatable("cinematiczoom.option.bars_percent"), cfg.barsPercent)
                  .setMin(0f).setMax(50f)
                  .setTooltip(Text.translatable("cinematiczoom.option.bars_percent.tooltip"))
                  .setSaveConsumer(v -> cfg.barsPercent = v)
                  .build()
            );

            cat.addEntry(
                eb.startIntField(Text.translatable("cinematiczoom.option.smooth_ms"), cfg.smoothMs)
                  .setMin(0).setMax(2000)
                  .setTooltip(Text.translatable("cinematiczoom.option.smooth_ms.tooltip"))
                  .setSaveConsumer(v -> cfg.smoothMs = v)
                  .build()
            );

            cat.addEntry(
                eb.startBooleanToggle(Text.translatable("cinematiczoom.option.mouse_wheel_enabled"), cfg.mouseWheelEnabled)
                  .setTooltip(Text.translatable("cinematiczoom.option.mouse_wheel_enabled.tooltip"))
                  .setSaveConsumer(v -> cfg.mouseWheelEnabled = v)
                  .build()
            );

            cat.addEntry(
                eb.startBooleanToggle(Text.translatable("cinematiczoom.option.hide_hud"), cfg.hideHudDuringZoom)
                  .setTooltip(Text.translatable("cinematiczoom.option.hide_hud.tooltip"))
                  .setSaveConsumer(v -> cfg.hideHudDuringZoom = v)
                  .build()
            );

            cat.addEntry(
                eb.startBooleanToggle(Text.translatable("cinematiczoom.option.cinematic_cam"), cfg.enableCinematicCamera)
                  .setTooltip(Text.translatable("cinematiczoom.option.cinematic_cam.tooltip"))
                  .setSaveConsumer(v -> cfg.enableCinematicCamera = v)
                  .build()
            );

            return builder.build();
        };
    }
}
