/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.tuple.Pair
 */
package rip.ysm.compat.sbackpack;

import org.openysm.client.animation.molang.CtrlBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import rip.ysm.compat.sbackpack.forge.SBackpackCompatImpl;

public final class SBackpackCompat {
    private SBackpackCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SBackpackCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void setupRenderLayers() {
        SBackpackCompatImpl.setupRenderLayers();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        return SBackpackCompatImpl.getInCompatibleInfo();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerControllerFunctions(CtrlBinding binding) {
        SBackpackCompatImpl.registerControllerFunctions(binding);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static ItemStack getBackpackItem(LivingEntity livingEntity) {
        return SBackpackCompatImpl.getBackpackItem(livingEntity);
    }
}

