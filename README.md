# CinematicZoom

Smooth cinematic zoom for Minecraft Fabric with animated black bars, per-frame easing and optional mouse-wheel control.

<img src="https://raw.githubusercontent.com/mel1x/CinematicZoom/refs/heads/main/CinematicZoom.gif" alt="CinematicZoom preview">

## Supported versions

| Minecraft | Mod version | Java |
| --- | --- | --- |
| 1.21.1 | 0.8.0 | 21+ |
| 1.21.5 | 0.9.0 | 21+ |
| 1.21.7 | 1.0.0 | 21+ |
| 1.21.11 | 1.0.1 | 21+ |
| 26.1–26.1.2 | 26.1 | 25+ |
| 26.2–26.2.2 | 26.2 | 25+ |

All ports live on `main`. The Gradle build keeps shared logic in common source sets and adds only the API-specific classes required by each Minecraft version.

## Building

The complete build requires JDK 25 because the `26.x` targets use Java 25 bytecode. The legacy targets are still compiled with `--release 21`.

Build every supported version:

```powershell
.\gradlew.bat clean buildAll
```

On Linux or macOS:

```bash
./gradlew clean buildAll
```

The six release JARs are collected in `build/libs/`.

Build only one target when developing:

```powershell
.\gradlew.bat :1.21.7:build
.\gradlew.bat :26.2:build
```

Per-version build output is also available under `versions/<minecraft-version>/build/libs/`.

## Project layout

- `src/common` — Java code shared by every target.
- `src/main/resources` — assets and translations shared by every target.
- `src/legacy` — shared Yarn-based code for Minecraft `1.21.x`.
- `src/legacy-pre-1.21.11` — key-binding API used before `1.21.11`.
- `src/legacy-float-fov` — FOV signature used by `1.21.5–1.21.11`.
- `src/modern` — shared unobfuscated API code for Minecraft `26.x`.
- `versions/<version>` — only metadata and Java classes that differ for that exact version.
- `build.gradle` — the single version/dependency matrix and all build configuration.

## Features

- Hold-to-zoom (default key: `C`, rebindable).
- Per-frame smoothing.
- Configurable animated black bars.
- Optional mouse-wheel zoom adjustment.
- Optional HUD/crosshair hiding.
- Optional cinematic camera while zooming.
- English and Russian translations.
- Mod Menu and Cloth Config integration.

## Installation

1. Install Fabric Loader and Fabric API for your Minecraft version.
2. Put the matching CinematicZoom JAR in the `mods` folder.
3. Optionally install Mod Menu and Cloth Config for the in-game configuration screen.

## Credits

- Original author and idea: [KubePixel](https://www.curseforge.com/members/kubepixel/projects), creator of the original [Cinematic Zoom](https://www.curseforge.com/minecraft/mc-mods/cinematic-zoom) Forge mod.
- Fabric implementation: mix.

This project is an independent Fabric implementation inspired by the original concept and is not affiliated with or endorsed by KubePixel.
