package org.openysm.client.animation.molang.functions.ysm.curios;

import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.funciton.entity.LivingEntityFunction;
import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.util.ThreadLocalItemTagSets;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import org.apache.commons.lang3.StringUtils;

public class HasAnyCuriosWithAnyTag extends LivingEntityFunction {
    @Override
    public Object eval(ExecutionContext<IContext<LivingEntity>> context, ArgumentCollection arguments) {
        String type = arguments.getAsString(context, 0);
        if (StringUtils.isEmpty(type)) {
            return null;
        }
        ReferenceArrayList<TagKey<Item>> referenceArrayList = ThreadLocalItemTagSets.TAG_KEY_LIST.get();
        referenceArrayList.size(arguments.size() - 1);
        for (int i = 1; i < arguments.size(); i++) {
            ResourceLocation tag = arguments.getResourceLocation(context, i);
            if (tag == null) {
                return null;
            }
            referenceArrayList.set(i - 1, TagKey.create(Registries.ITEM, tag));
        }
        return CuriosCompat.hasTaggedItemInSlot(context.entity().entity(), type, referenceArrayList);
    }

    @Override
    public boolean validateArgumentSize(int size) {
        return size > 1;
    }
}