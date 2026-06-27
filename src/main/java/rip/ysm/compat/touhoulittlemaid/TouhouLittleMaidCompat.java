/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.Item
 */
package rip.ysm.compat.touhoulittlemaid;

import org.openysm.client.animation.molang.TLMBinding;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.model.ModelResourceBundle;
import org.openysm.client.model.PlayerModelBundle;
import org.openysm.geckolib3.core.enums.PlayState;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import rip.ysm.compat.touhoulittlemaid.forge.TouhouLittleMaidCompatImpl;

public final class TouhouLittleMaidCompat {
    private TouhouLittleMaidCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return TouhouLittleMaidCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidEntity(Entity entity) {
        return TouhouLittleMaidCompatImpl.isMaidEntity(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidRideable(Entity entity) {
        return TouhouLittleMaidCompatImpl.isMaidRideable(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isSimplePlanesEntity(Entity entity) {
        return TouhouLittleMaidCompatImpl.isSimplePlanesEntity(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isImmersiveAircraftEntity(Entity entity) {
        return TouhouLittleMaidCompatImpl.isImmersiveAircraftEntity(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidItem(Item item) {
        return TouhouLittleMaidCompatImpl.isMaidItem(item);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static String getMaidEntityId(Entity entity) {
        return TouhouLittleMaidCompatImpl.getMaidEntityId(entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidSitting(LivingEntity livingEntity) {
        return TouhouLittleMaidCompatImpl.isMaidSitting(livingEntity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerMaidAnimStates(TLMBinding tlmBinding) {
        TouhouLittleMaidCompatImpl.registerMaidAnimStates(tlmBinding);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static PlayState handleMaidInteraction(AnimationEvent<LivingAnimatable<?>> event, LivingEntity livingEntity, Entity entity) {
        return TouhouLittleMaidCompatImpl.handleMaidInteraction(event, livingEntity, entity);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidChatAvailable() {
        return TouhouLittleMaidCompatImpl.isMaidChatAvailable();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void openMaidChat() {
        TouhouLittleMaidCompatImpl.openMaidChat();
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Object buildControllers(PlayerModelBundle modelBundle, ModelResourceBundle resourceBundle) {
        return TouhouLittleMaidCompatImpl.buildControllers(modelBundle, resourceBundle);
    }
}
