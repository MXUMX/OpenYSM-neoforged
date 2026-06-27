package org.openysm.client.compat.touhoulittlemaid;

import org.openysm.client.compat.touhoulittlemaid.event.MaidInteractionEvent;
import org.openysm.network.message.FeedbackData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.fml.ModList;

public class TouhouMaidCompat {

    private static final String MOD_ID = "touhou_little_maid";

    public static boolean isLoaded() {
        return ModList.get().isLoaded(MOD_ID);
    }

    public static void init() {
        if (isLoaded()) {
            NeoForge.EVENT_BUS.register(new MaidInteractionEvent());
        }
    }

    public static boolean isMaidEntity(Entity entity) {
        if (isLoaded()) {
            return TouhouMaidModelHandler.isMaidEntity(entity);
        }
        return false;
    }

    public static void handleProjectileOwner(Projectile projectile, Entity entity) {
        if (isLoaded()) {
            TouhouMaidModelHandler.applyProjectileModelFromMaid(projectile, entity);
        }
    }

    public static void registerAnimationRoulette(Entity entity, String str, int i) {
        if (isLoaded()) {
            TouhouMaidModelHandler.activateRouletteAnimation(entity, str, i);
        }
    }

    public static void applyFeedback(Entity entity, FeedbackData message) {
        if (isLoaded()) {
            TouhouMaidModelHandler.handleMaidFeedback(entity, message);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void playMaidAnimation(Entity entity, String str) {
        if (isLoaded()) {
            TouhouMaidModelHandler.executeMaidMolang(entity, str);
        }
    }
}