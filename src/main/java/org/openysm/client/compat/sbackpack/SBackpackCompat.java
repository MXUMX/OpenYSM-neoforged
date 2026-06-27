package org.openysm.client.compat.sbackpack;

import org.openysm.client.renderer.RendererManager;
import org.openysm.client.renderer.layer.SophisticatedBackpackLayer;
import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.client.compat.curios.CuriosItemGetter;
import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.config.GeneralConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SBackpackCompat {
    private static final ArtifactVersion MIN_VERSION = new DefaultArtifactVersion("3.24.25");
    private static final String MOD_ID = "sophisticatedbackpacks";

    private static boolean IS_LOADED;

    private static boolean INCOMPATIBLE_VERSION;

    public static void init() {
        ModFileInfo modFileById;
        if (!GeneralConfig.SOPHISTICATEDBACKPACK.get() && (modFileById = LoadingModList.get().getModFileById(MOD_ID)) != null) {
            if (modFileById.getMods().get(0).getVersion().compareTo(MIN_VERSION) >= 0) {
                IS_LOADED = true;
            } else {
                INCOMPATIBLE_VERSION = true;
            }
        }
    }

    public static void setupRenderLayers() {
        if (IS_LOADED) {
            RendererManager.getPlayerRenderer().addLayerRenderer(new SophisticatedBackpackLayer());
        }
    }

    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        if (INCOMPATIBLE_VERSION) {
            return Optional.of(Pair.of("Sophisticated Backpacks", MIN_VERSION.toString()));
        }
        return Optional.empty();
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    public static void registerControllerFunctions(CtrlBinding binding) {
        if (isLoaded()) {
            BackpackBinding.registerFunctions(binding);
        } else {
            registerBackpackFunctions(binding);
        }
    }

    private static void registerBackpackFunctions(CtrlBinding binding) {
        binding.livingEntityVar("has_sophisticated_backpack", interfaceC0807x6b368640 -> false);
    }

    @Nullable
    public static ItemStack getBackpackItem(LivingEntity livingEntity) {
        ItemStack stack;
        if (CuriosCompat.isLoaded() && (stack = CuriosItemGetter.getBackCurio(livingEntity)) != null) {
            return stack;
        }
        if (livingEntity instanceof Player) {
            return PlayerInventoryProvider.get().getBackpackFromRendered((Player) livingEntity).map(renderInfo -> {
                ItemStack backpack = renderInfo.getBackpack();
                if (!backpack.isEmpty()) {
                    return backpack;
                }
                return null;
            }).orElse(null);
        }
        return null;
    }
}