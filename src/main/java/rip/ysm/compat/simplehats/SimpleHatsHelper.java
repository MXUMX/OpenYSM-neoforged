/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.simplehats;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import rip.ysm.compat.simplehats.forge.SimpleHatsHelperImpl;

public final class SimpleHatsHelper {
    private SimpleHatsHelper() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return SimpleHatsHelperImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static ItemStack getHatItem(LivingEntity livingEntity) {
        return SimpleHatsHelperImpl.getHatItem(livingEntity);
    }
}

