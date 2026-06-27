package org.openysm.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class GeneralConfig {

    public static ModConfigSpec.BooleanValue DISCLAIMER_SHOW;

    public static ModConfigSpec.BooleanValue PRINT_ANIMATION_ROULETTE_MSG;

    public static ModConfigSpec.BooleanValue BLUR_GUI;

    public static ModConfigSpec.BooleanValue DISABLE_SELF_MODEL;

    public static ModConfigSpec.BooleanValue DISABLE_OTHER_MODEL;

    public static ModConfigSpec.BooleanValue DISABLE_SELF_HANDS;

    public static ModConfigSpec.BooleanValue DISABLE_PROJECTILE_MODEL;

    public static ModConfigSpec.BooleanValue DISABLE_VEHICLE_MODEL;

    public static ModConfigSpec.BooleanValue DISABLE_EXTERNAL_FP_ANIM;

    public static ModConfigSpec.BooleanValue USE_COMPATIBILITY_RENDERER;

    public static ModConfigSpec.BooleanValue USE_GPU_RENDERER;

    public static ModConfigSpec.DoubleValue SOUND_VOLUME;

    public static ModConfigSpec.BooleanValue SHOW_MODEL_ID_FIRST;

    public static ModConfigSpec.EnumValue<ScreenMode> ROULETTE_SETTINGS_MODE;

    public static ModConfigSpec.EnumValue<RouletteMode> ROULETTE_MODE;

    public static ModConfigSpec.EnumValue<ScreenMode> TEXTURE_SCREEN_MODE;

    public static ModConfigSpec.EnumValue<ScreenMode> MODEL_INFO_SCREEN_MODE;

    public static ModConfigSpec.BooleanValue SOPHISTICATEDBACKPACK;

    public static ModConfigSpec.BooleanValue PARCOOL;

    public enum ScreenMode {
        MODERN,
        CLASSIC
    }

    public enum RouletteMode {
        CLASSIC,
        MODERN
    }

    public static ModConfigSpec buildSpec() {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
        defineGeneral(builder);
        ExtraPlayerRenderConfig.define(builder);
        LoadingStateConfig.define(builder);
        return builder.build();
    }

    public static void defineGeneral(ModConfigSpec.Builder builder) {
        builder.push("general");
        builder.comment("Whether to display disclaimer GUI");
        DISCLAIMER_SHOW = builder.define("DisclaimerShow", true);
        builder.comment("Whether to print animation roulette play message");
        PRINT_ANIMATION_ROULETTE_MSG = builder.define("PrintAnimationRouletteMsg", false);
        builder.comment("Whether to blur the background behind modern OpenYSM GUI overlays.");
        BLUR_GUI = builder.define("BlurGui", true);
        builder.comment("Prevents rendering of self player's model");
        DISABLE_SELF_MODEL = builder.define("DisableSelfModel", false);
        builder.comment("Prevents rendering of other player's model");
        DISABLE_OTHER_MODEL = builder.define("DisableOtherModel", false);
        builder.comment("Prevents rendering of self player's hand");
        DISABLE_SELF_HANDS = builder.define("DisableSelfHands", false);
        builder.comment("Prevents rendering of projectile model");
        DISABLE_PROJECTILE_MODEL = builder.define("DisableProjectileModel", false);
        builder.comment("Prevents rendering of vehicle model");
        DISABLE_VEHICLE_MODEL = builder.define("DisableVehicleModel", false);
        builder.comment("Disable first person animation from other mods.");
        DISABLE_EXTERNAL_FP_ANIM = builder.define("DisableExternalFirstPersonAnim", false);
        builder.comment("If rendering errors occur, try turning on this.");
        USE_COMPATIBILITY_RENDERER = builder.define("UseCompatibilityRenderer", false);
        builder.comment("Use experimental GPU renderer.");
        USE_GPU_RENDERER = builder.define("UseGpuRenderer", false);
        builder.comment("The amount of volume when the animation is played.");
        SOUND_VOLUME = builder.defineInRange("SoundVolume", 100.0d, 0.0d, 100.0d);
        builder.comment("Whether to display model ID first in the model selection screen, instead of the model name filled in by the model author.");
        SHOW_MODEL_ID_FIRST = builder.define("ShowModelIdFirst", false);
        builder.comment("Screen style for roulette model settings.");
        ROULETTE_SETTINGS_MODE = builder.defineEnum("RouletteSettingsMode", ScreenMode.MODERN);
        builder.comment("Animation roulette style.");
        ROULETTE_MODE = builder.defineEnum("RouletteMode", RouletteMode.CLASSIC);
        builder.comment("Texture selection screen style.");
        TEXTURE_SCREEN_MODE = builder.defineEnum("TextureScreenMode", ScreenMode.MODERN);
        builder.comment("Model information screen style.");
        MODEL_INFO_SCREEN_MODE = builder.defineEnum("ModelInfoScreenMode", ScreenMode.MODERN);
        builder.pop();
        builder.push("Integration");
        SOPHISTICATEDBACKPACK = builder.define("SophisticatedBackpack", true);
        PARCOOL = builder.define("Parcool", true);
        builder.pop();
    }
}
