/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 */
package rip.ysm.compat.simplehats.forge;

import org.openysm.client.compat.simplehats.SimpleHatsHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public final class SimpleHatsHelperImpl {
    private SimpleHatsHelperImpl() {
    }

    public static boolean isLoaded() {
        return SimpleHatsHelper.isLoaded();
    }

    public static ItemStack getHatItem(LivingEntity livingEntity) {
        return SimpleHatsHelper.getHatItem(livingEntity);
    }
}

