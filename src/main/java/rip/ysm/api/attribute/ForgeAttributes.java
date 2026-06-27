/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  org.jetbrains.annotations.Nullable
 */
package rip.ysm.api.attribute;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.jetbrains.annotations.Nullable;
import rip.ysm.api.attribute.forge.ForgeAttributesImpl;

public final class ForgeAttributes {
    private ForgeAttributes() {
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute blockReach() {
        return ForgeAttributesImpl.blockReach();
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute entityReach() {
        return ForgeAttributesImpl.entityReach();
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute swimSpeed() {
        return ForgeAttributesImpl.swimSpeed();
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute entityGravity() {
        return ForgeAttributesImpl.entityGravity();
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute stepHeightAddition() {
        return ForgeAttributesImpl.stepHeightAddition();
    }

    @ExpectPlatform
    @Nullable
    @ExpectPlatform.Transformed
    public static Attribute nametagDistance() {
        return ForgeAttributesImpl.nametagDistance();
    }

    public static double getValue(LivingEntity entity, @Nullable Attribute attribute, double defaultValue) {
        if (attribute == null) {
            return defaultValue;
        }
        Holder<Attribute> holder = BuiltInRegistries.ATTRIBUTE.wrapAsHolder(attribute);
        return entity.getAttributes().hasAttribute(holder) ? entity.getAttributeValue(holder) : defaultValue;
    }
}
