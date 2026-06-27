/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 */
package rip.ysm.compat.curios.forge;

import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.geckolib3.core.molang.binding.ContextBinding;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public final class CuriosCompatImpl {
    private CuriosCompatImpl() {
    }

    public static boolean isLoaded() {
        return CuriosCompat.isLoaded();
    }

    public static boolean hasItemInSlot(LivingEntity livingEntity, String str, ReferenceOpenHashSet<Item> set) {
        return CuriosCompat.hasItemInSlot(livingEntity, str, set);
    }

    public static boolean hasTaggedItemInSlot(LivingEntity livingEntity, String str, List<TagKey<Item>> list) {
        return CuriosCompat.hasTaggedItemInSlot(livingEntity, str, list);
    }

    public static boolean hasNoTaggedItemInSlot(LivingEntity entity, String str, List<TagKey<Item>> list) {
        return CuriosCompat.hasNoTaggedItemInSlot(entity, str, list);
    }

    public static void registerCuriosItems(ContextBinding binding) {
        CuriosCompat.registerCuriosItems(binding);
    }
}

