package org.openysm.client.compat.simplehats;

import fonnymunkey.simplehats.common.item.HatItem;
import io.wispforest.accessories.api.AccessoriesCapability;
import io.wispforest.accessories.api.EquipmentChecking;
import io.wispforest.accessories.api.slot.SlotEntryReference;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class HatAccessoriesGetter {

    @Nullable
    public static ItemStack getHeadAccessory(LivingEntity livingEntity) {
        return AccessoriesCapability.getOptionally(livingEntity)
                .map(HatAccessoriesGetter::getHeadAccessory)
                .orElse(null);
    }

    @Nullable
    private static ItemStack getHeadAccessory(AccessoriesCapability capability) {
        SlotEntryReference reference = getRenderableHat(capability, EquipmentChecking.COSMETICALLY_OVERRIDABLE);
        if (reference == null) {
            reference = getRenderableHat(capability, EquipmentChecking.ACCESSORIES_ONLY);
        }
        return reference != null ? reference.stack() : null;
    }

    @Nullable
    private static SlotEntryReference getRenderableHat(AccessoriesCapability capability, EquipmentChecking checking) {
        SlotEntryReference reference = capability.getFirstEquipped(stack -> stack.getItem() instanceof HatItem, checking);
        if (reference == null || reference.reference() == null || reference.reference().slotContainer() == null) {
            return null;
        }
        return reference.reference().slotContainer().shouldRender(reference.reference().slot()) ? reference : null;
    }
}
