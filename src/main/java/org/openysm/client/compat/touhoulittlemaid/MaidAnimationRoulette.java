package org.openysm.client.compat.touhoulittlemaid;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.client.gui.AnimationRouletteScreen;
import org.openysm.client.model.ModelAssembly;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class MaidAnimationRoulette {
    public static boolean canOpenRoulette() {
        LocalPlayer localPlayer = Minecraft.getInstance().player;
        if (localPlayer == null) {
            return false;
        }
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (!(hitResult instanceof EntityHitResult)) {
            return false;
        }
        Entity entity = ((EntityHitResult) hitResult).getEntity();
        if (entity instanceof EntityMaid entityMaid) {
            if (!entityMaid.isYsmModel()) {
                return false;
            }
            return localPlayer.getUUID().equals(entityMaid.getOwnerUUID());
        }
        return false;
    }

    public static void openRouletteScreen() {
        HitResult hitResult = Minecraft.getInstance().hitResult;
        if (!(hitResult instanceof EntityHitResult)) {
            return;
        }
        Entity entity = ((EntityHitResult) hitResult).getEntity();
        if (entity instanceof EntityMaid) {
            org.openysm.capability.YSMCapabilities.get(entity, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
                ModelAssembly modelAssembly = cap.getModelAssembly();
                if (modelAssembly != null && !modelAssembly.getModelData().getModelProperties().getExtraAnimation().isEmpty()) {
                    if (Minecraft.getInstance().screen == null) {
                        Minecraft.getInstance().setScreen(new AnimationRouletteScreen(cap.getModelId(), modelAssembly, cap));
                    } else if (Minecraft.getInstance().screen instanceof AnimationRouletteScreen) {
                        Minecraft.getInstance().setScreen(null);
                    }
                }
            });
        }
    }
}