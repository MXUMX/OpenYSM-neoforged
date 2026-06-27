package org.openysm.client.gui;

import org.openysm.config.ExtraPlayerRenderConfig;
import org.openysm.config.GeneralConfig;
import org.openysm.config.LoadingStateConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import rip.ysm.gui.Option;
import rip.ysm.gui.OptionGroup;
import rip.ysm.gui.OptionScreen;
import rip.ysm.gui.components.BooleanOptionRow;
import rip.ysm.gui.components.EnumOptionRow;
import rip.ysm.gui.components.SliderOptionRow;

public class ExtraPlayerConfigScreen extends OptionScreen {
    private static final int CONFIG_ROW_HEIGHT = 43;

    public ExtraPlayerConfigScreen(@Nullable Screen parentScreen) {
        super(Component.literal("OpenYSM"), parentScreen);
    }

    @Override
    protected int computePanelWidth() {
        return Math.min(this.width - 32, 1080);
    }

    @Override
    protected int computePanelHeight() {
        return Math.min(this.height - 32, 568);
    }

    @Override
    protected boolean shouldUseCompactTabs() {
        return this.width < 720;
    }

    @Override
    protected int tabWidth() {
        return 220;
    }

    @Override
    protected int tabHeight() {
        return 44;
    }

    @Override
    protected int footerButtonWidth() {
        return 140;
    }

    @Override
    protected void registerGroups() {
        this.groups.add(new OptionGroup("general")
                .add(new SliderOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofDouble("sound_volume", GeneralConfig.SOUND_VOLUME), 0.0d, 100.0d, 1.0d, "%"))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_self_model", GeneralConfig.DISABLE_SELF_MODEL)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_other_model", GeneralConfig.DISABLE_OTHER_MODEL)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_self_hands", GeneralConfig.DISABLE_SELF_HANDS))));

        this.groups.add(new OptionGroup("rendering")
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_player_render", ExtraPlayerRenderConfig.DISABLE_PLAYER_RENDER)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_projectile_model", GeneralConfig.DISABLE_PROJECTILE_MODEL)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_vehicle_model", GeneralConfig.DISABLE_VEHICLE_MODEL)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_external_first_person_anim", GeneralConfig.DISABLE_EXTERNAL_FP_ANIM))));

        this.groups.add(new OptionGroup("performance")
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("use_compatibility_renderer", GeneralConfig.USE_COMPATIBILITY_RENDERER)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("use_gpu_renderer", GeneralConfig.USE_GPU_RENDERER))));

        this.groups.add(new OptionGroup("misc")
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("print_animation_roulette_msg", GeneralConfig.PRINT_ANIMATION_ROULETTE_MSG)))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("disable_loading_state_screen", LoadingStateConfig.DISABLE_LOADING_STATE_SCREEN)))
                .add(new EnumOptionRow<>(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofEnum("loading_state_position", LoadingStateConfig.LOADING_STATE_POSITION), LoadingStateConfig.Position.values()))
                .add(new EnumOptionRow<>(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofEnum("roulette_settings_mode", GeneralConfig.ROULETTE_SETTINGS_MODE), GeneralConfig.ScreenMode.values()))
                .add(new EnumOptionRow<>(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofEnum("roulette_mode", GeneralConfig.ROULETTE_MODE), GeneralConfig.RouletteMode.values()))
                .add(new EnumOptionRow<>(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofEnum("texture_screen_mode", GeneralConfig.TEXTURE_SCREEN_MODE), GeneralConfig.ScreenMode.values()))
                .add(new EnumOptionRow<>(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofEnum("model_info_screen_mode", GeneralConfig.MODEL_INFO_SCREEN_MODE), GeneralConfig.ScreenMode.values()))
                .add(new BooleanOptionRow(0, 0, 0, CONFIG_ROW_HEIGHT, Option.ofBoolean("blur_gui", GeneralConfig.BLUR_GUI))));
    }
}
