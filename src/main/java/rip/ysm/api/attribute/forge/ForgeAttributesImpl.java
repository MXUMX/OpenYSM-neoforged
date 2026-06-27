/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.neoforged.neoforge.common.NeoForgeMod
 */
package rip.ysm.api.attribute.forge;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.common.NeoForgeMod;

public final class ForgeAttributesImpl {
    private ForgeAttributesImpl() {
    }

    public static Attribute blockReach() {
        return Attributes.BLOCK_INTERACTION_RANGE.value();
    }

    public static Attribute entityReach() {
        return Attributes.ENTITY_INTERACTION_RANGE.value();
    }

    public static Attribute swimSpeed() {
        return NeoForgeMod.SWIM_SPEED.value();
    }

    public static Attribute entityGravity() {
        return Attributes.GRAVITY.value();
    }

    public static Attribute stepHeightAddition() {
        return Attributes.STEP_HEIGHT.value();
    }

    public static Attribute nametagDistance() {
        return NeoForgeMod.NAMETAG_DISTANCE.value();
    }
}
