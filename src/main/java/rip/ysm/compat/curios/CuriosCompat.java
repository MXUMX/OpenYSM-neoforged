/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet
 *  net.minecraft.tags.TagKey
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 */
package rip.ysm.compat.curios;

import org.openysm.geckolib3.core.molang.binding.ContextBinding;
import dev.architectury.injectables.annotations.ExpectPlatform;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.List;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import rip.ysm.compat.curios.forge.CuriosCompatImpl;

public final class CuriosCompat {
    private CuriosCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return CuriosCompatImpl.isLoaded();
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean hasItemInSlot(LivingEntity livingEntity, String str, ReferenceOpenHashSet<Item> set) {
        return CuriosCompatImpl.hasItemInSlot(livingEntity, str, set);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean hasTaggedItemInSlot(LivingEntity livingEntity, String str, List<TagKey<Item>> list) {
        return CuriosCompatImpl.hasTaggedItemInSlot(livingEntity, str, list);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean hasNoTaggedItemInSlot(LivingEntity entity, String str, List<TagKey<Item>> list) {
        return CuriosCompatImpl.hasNoTaggedItemInSlot(entity, str, list);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerCuriosItems(ContextBinding binding) {
        CuriosCompatImpl.registerCuriosItems(binding);
    }
}
