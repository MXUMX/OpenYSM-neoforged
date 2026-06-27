/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  org.apache.commons.lang3.tuple.Pair
 */
package rip.ysm.compat.sbackpack.forge;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.compat.sbackpack.SBackpackCompat;
import java.util.Optional;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;

public final class SBackpackCompatImpl {
    private SBackpackCompatImpl() {
    }

    public static boolean isLoaded() {
        return SBackpackCompat.isLoaded();
    }

    public static void setupRenderLayers() {
        SBackpackCompat.setupRenderLayers();
    }

    public static Optional<Pair<String, String>> getInCompatibleInfo() {
        return SBackpackCompat.getInCompatibleInfo();
    }

    public static void registerControllerFunctions(CtrlBinding binding) {
        SBackpackCompat.registerControllerFunctions(binding);
    }

    public static ItemStack getBackpackItem(LivingEntity livingEntity) {
        return SBackpackCompat.getBackpackItem(livingEntity);
    }
}

