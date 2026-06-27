package org.openysm;

import org.openysm.config.GeneralConfig;
import org.openysm.config.ModSoundEvents;
import org.openysm.config.ServerConfig;
import org.openysm.capability.YSMDataAttachments;
import org.openysm.event.YsmEventBootstrap;
import org.openysm.network.NetworkHandler;
import org.openysm.util.obfuscate.Keep;
import net.minecraftforge.network.NetworkRegistry;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.fml.loading.FMLPaths;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

/**
 * TODO:
 * 默认模型应该就在模组架加载的时候就预加载了
 * 其它模型统统都是进入世界后加载
 */
@Mod(OpenYSM.MOD_ID)
public class OpenYSM {

    public static final String MOD_ID = "openysm";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public OpenYSM(IEventBus modEventBus, ModContainer modContainer) throws IOException {
        YsmEventBootstrap.register(modEventBus);
        NativeLibLoader.init();
        if (!NativeLibLoader.isAvailable()) {
            LOGGER.error(getErrorMessage());
        } else {
            initConfig(modEventBus, modContainer);
            YSMDataAttachments.REGISTER.register(modEventBus);
            NetworkHandler.init();
        }
        modEventBus.addListener(this::registerPayloadHandlers);
    }

    @SuppressWarnings({"deprecation", "removal"})
    private static void initConfig(IEventBus modEventBus, ModContainer modContainer) {
        File oldConfig = FMLPaths.CONFIGDIR.get().resolve("openysm-common.toml").toFile();
        if (oldConfig.isFile()) {
            File file2 = FMLPaths.CONFIGDIR.get().resolve("openysm-client.toml").toFile();
            if (!file2.isFile()) {
                oldConfig.renameTo(file2);
            } else {
                oldConfig.delete();
            }
        }
        modContainer.registerConfig(ModConfig.Type.CLIENT, GeneralConfig.buildSpec());
        modContainer.registerConfig(ModConfig.Type.SERVER, ServerConfig.buildSpec());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModSoundEvents.REGISTER.register(modEventBus);
        }
    }

    private void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        NetworkRegistry.channels().forEach(channel -> channel.registerPayload(event));
    }

    @Keep
    public static boolean isAvailable() {
        return NativeLibLoader.isAvailable();
    }

    public static boolean isOnAndroid() {
        return NativeLibLoader.isOnAndroid();
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendUnavailableMessage() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer != null) {
            localPlayer.sendSystemMessage(getUnavailableComponent());
        }
    }

    public static Component getUnavailableComponent() {
        return NativeLibLoader.getErrorComponent();
    }

    public static String getErrorMessage() {
        return NativeLibLoader.getErrorMessage();
    }
}
