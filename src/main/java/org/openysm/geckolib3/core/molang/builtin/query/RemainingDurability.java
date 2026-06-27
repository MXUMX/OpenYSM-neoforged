package org.openysm.geckolib3.core.molang.builtin.query;

import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorHelper;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.util.MolangUtils;
import org.openysm.geckolib3.core.molang.funciton.entity.LivingEntityFunction;
import org.openysm.molang.runtime.ExecutionContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class RemainingDurability extends LivingEntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<LivingEntity>> context, ArgumentCollection arguments) {
        ItemStack stack = CosmeticArmorHelper.getArmorItem(context.entity().entity(), MolangUtils.parseSlotType(context.entity(), arguments.getAsString(context, 0)));
        return stack.getMaxDamage() - stack.getDamageValue();
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size == 1;
    }
}