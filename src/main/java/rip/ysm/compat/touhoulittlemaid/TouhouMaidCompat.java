/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.Projectile
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.api.distmarker.OnlyIn
 */
package rip.ysm.compat.touhoulittlemaid;

import org.openysm.network.message.FeedbackData;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import rip.ysm.compat.touhoulittlemaid.forge.TouhouMaidCompatImpl;

public final class TouhouMaidCompat {
    private TouhouMaidCompat() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isLoaded() {
        return TouhouMaidCompatImpl.isLoaded();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void init() {
        TouhouMaidCompatImpl.init();
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static boolean isMaidEntity(Entity entity) {
        return TouhouMaidCompatImpl.isMaidEntity(entity);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void handleProjectileOwner(Projectile projectile, Entity entity) {
        TouhouMaidCompatImpl.handleProjectileOwner(projectile, entity);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void registerAnimationRoulette(Entity entity, String str, int i) {
        TouhouMaidCompatImpl.registerAnimationRoulette(entity, str, i);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void applyFeedback(Entity entity, FeedbackData message) {
        TouhouMaidCompatImpl.applyFeedback(entity, message);
    }

    /*
     * WARNING - void declaration
     */
    @OnlyIn(value=Dist.CLIENT)
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void playMaidAnimation(Entity entity, String str) {
        TouhouMaidCompatImpl.playMaidAnimation(entity, str);
    }
}
