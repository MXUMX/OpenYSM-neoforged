package org.openysm.client.compat.parcool;

import org.openysm.geckolib3.core.controller.CompositeAnimationController;
import org.openysm.geckolib3.core.controller.IAnimationController;
import org.openysm.client.animation.predicate.PlayerSpecialAnimationPredicate;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.config.GeneralConfig;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.client.animation.molang.CtrlBinding;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.BiFunction;

public class ParcoolCompat {
    private static final ArtifactVersion MIN_VERSION = new DefaultArtifactVersion("3.4.1");
    private static final String MOD_ID = "parcool";

    private static boolean IS_LOADED;

    private static boolean INCOMPATIBLE_VERSION;

    public static void init() {
        if (GeneralConfig.PARCOOL != null && !GeneralConfig.PARCOOL.get()) {
            IS_LOADED = false;
            return;
        }
        ModFileInfo modFileById = LoadingModList.get().getModFileById(MOD_ID);
        if (modFileById != null) {
            if (modFileById.getMods().get(0).getVersion().compareTo(MIN_VERSION) >= 0) {
                IS_LOADED = isSupportedRuntime();
            } else {
                INCOMPATIBLE_VERSION = true;
            }
        }
    }

    private static boolean isSupportedRuntime() {
        ClassLoader classLoader = ParcoolCompat.class.getClassLoader();
        return classLoader.getResource("com/alrex/parcool/common/attachment/client/Animation.class") != null
                && classLoader.getResource("com/alrex/parcool/common/attachment/common/Parkourability.class") != null;
    }

    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        if (INCOMPATIBLE_VERSION) {
            return Optional.of(Pair.of(MOD_ID, MIN_VERSION.toString()));
        }
        return Optional.empty();
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static Optional<BiFunction<String, CustomPlayerEntity, IAnimationController<CustomPlayerEntity>>> getControllerFactory() {
        if (IS_LOADED) {
            return Optional.of((name, entity) -> new CompositeAnimationController(entity, name, 0.1f, new PlayerSpecialAnimationPredicate()));
        }
        return Optional.empty();
    }

    public static boolean isPlayerParcooling(Player player) {
        if (isLoaded()) {
            return ParcoolAnimationHandler.hasParcoolAnimation(player);
        }
        return false;
    }

    @Nullable
    public static String getActionName(Player player) {
        if (isLoaded()) {
            return ParcoolAnimationHandler.getParcoolAnimationName(player);
        }
        return null;
    }

    public static void registerBindings(CtrlBinding binding) {
        if (isLoaded()) {
            ParcoolBinding.registerBindings(binding);
        } else {
            registerDummyBindings(binding);
        }
    }

    private static void registerDummyBindings(CtrlBinding binding) {
        binding.livingEntityVar("parcool_state", interfaceC0807x6b368640 -> StringPool.EMPTY);
    }
}
