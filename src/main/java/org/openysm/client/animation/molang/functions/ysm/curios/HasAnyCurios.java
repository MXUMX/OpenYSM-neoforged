package org.openysm.client.animation.molang.functions.ysm.curios;

import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.entity.LivingEntityFunction;
import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.util.ThreadLocalItemTagSets;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;

public class HasAnyCurios extends LivingEntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<LivingEntity>> context, ArgumentCollection arguments) {
        String type = arguments.getAsString(context, 0);
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        ReferenceOpenHashSet<Item> referenceOpenHashSet = ThreadLocalItemTagSets.ITEM_SET.get();
        referenceOpenHashSet.clear();
        for (int i = 1; i < arguments.size(); i++) {
            ResourceLocation name = arguments.getResourceLocation(context, i);
            if (name == null) {
                return null;
            }
            Item item = ForgeRegistries.ITEMS.getValue(name);
            if (item != null) {
                referenceOpenHashSet.add(item);
            }
        }
        return CuriosCompat.hasItemInSlot(context.entity().entity(), type, referenceOpenHashSet);
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size > 0;
    }
}