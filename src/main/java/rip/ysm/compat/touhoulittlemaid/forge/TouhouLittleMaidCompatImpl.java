/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 */
package rip.ysm.compat.touhoulittlemaid.forge;

import org.openysm.client.animation.molang.TLMBinding;
import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.model.ModelResourceBundle;
import org.openysm.client.model.PlayerModelBundle;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;

public final class TouhouLittleMaidCompatImpl {
    private TouhouLittleMaidCompatImpl() {
    }

    public static boolean isLoaded() {
        return TouhouLittleMaidCompat.isLoaded();
    }

    public static boolean isMaidEntity(Entity entity) {
        return TouhouLittleMaidCompat.isMaidEntity(entity);
    }

    public static boolean isMaidRideable(Entity entity) {
        return TouhouLittleMaidCompat.isMaidRideable(entity);
    }

    public static boolean isSimplePlanesEntity(Entity entity) {
        return TouhouLittleMaidCompat.isSimplePlanesEntity(entity);
    }

    public static boolean isImmersiveAircraftEntity(Entity entity) {
        return TouhouLittleMaidCompat.isImmersiveAircraftEntity(entity);
    }

    public static boolean isMaidItem(Item item) {
        return TouhouLittleMaidCompat.isMaidItem(item);
    }

    public static String getMaidEntityId(Entity entity) {
        return TouhouLittleMaidCompat.getMaidEntityId(entity);
    }

    public static boolean isMaidSitting(LivingEntity livingEntity) {
        return TouhouLittleMaidCompat.isMaidSitting(livingEntity);
    }

    public static void registerMaidAnimStates(TLMBinding tlmBinding) {
        TouhouLittleMaidCompat.registerMaidAnimStates(tlmBinding);
    }

    public static PlayState handleMaidInteraction(AnimationEvent<LivingAnimatable<?>> event, LivingEntity livingEntity, Entity entity) {
        return TouhouLittleMaidCompat.handleMaidInteraction(event, livingEntity, entity);
    }

    public static boolean isMaidChatAvailable() {
        return TouhouLittleMaidCompat.isMaidChatAvailable();
    }

    public static void openMaidChat() {
        TouhouLittleMaidCompat.openMaidChat();
    }

    public static Object buildControllers(PlayerModelBundle modelBundle, ModelResourceBundle resourceBundle) {
        return TouhouLittleMaidCompat.buildControllers(modelBundle, resourceBundle);
    }
}

