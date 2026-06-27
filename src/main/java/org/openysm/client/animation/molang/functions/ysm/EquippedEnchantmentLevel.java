package org.openysm.client.animation.molang.functions.ysm;

import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorHelper;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.entity.LivingEntityFunction;
import org.openysm.geckolib3.util.MolangUtils;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;

public class EquippedEnchantmentLevel extends LivingEntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<LivingEntity>> context, ArgumentCollection arguments) {
        EquipmentSlot slotType = MolangUtils.parseSlotType(context.entity(), arguments.getAsString(context, 0));
        if (slotType == null) {
            return null;
        }
        ItemStack stack = CosmeticArmorHelper.getArmorItem(context.entity().entity(), slotType);
        if (stack.isEmpty()) {
            return 0;
        }
        int enchantmentLevel = 0;
        for (int i = 1; i < arguments.size(); i++) {
            ResourceLocation id = arguments.getResourceLocation(context, 1);
            if (id != null) {
                for (Holder<Enchantment> enchantment : stack.getEnchantments().keySet()) {
                    ResourceLocation key = ForgeRegistries.ENCHANTMENTS.getKey(enchantment.value());
                    if (id.equals(key)) {
                        enchantmentLevel += stack.getEnchantments().getLevel(enchantment);
                    }
                }
            }
        }
        return enchantmentLevel;
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size >= 2;
    }
}
