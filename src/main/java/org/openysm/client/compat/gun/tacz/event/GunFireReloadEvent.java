package org.openysm.client.compat.gun.tacz.event;

import org.openysm.OpenYSM;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import com.tacz.guns.api.event.common.GunFireEvent;
import com.tacz.guns.api.event.common.GunMeleeEvent;
import com.tacz.guns.api.event.common.GunReloadEvent;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;

public class GunFireReloadEvent {
    @SubscribeEvent
    public void onGunFire(GunFireEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        LivingEntity shooter = event.getShooter();
        org.openysm.capability.YSMCapabilities.get(shooter, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            cap.setExtraRenderFlag(true);
        });
        TouhouLittleMaidCompat.syncMaidState(shooter);
    }

    @SubscribeEvent
    public void onGunMelee(GunMeleeEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        LivingEntity shooter = event.getShooter();
        org.openysm.capability.YSMCapabilities.get(shooter, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            cap.setExtraRenderFlag(true);
        });
        TouhouLittleMaidCompat.syncMaidState(shooter);
    }

    @SubscribeEvent
    public void onGunReload(GunReloadEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        LivingEntity entity = event.getEntity();
        org.openysm.capability.YSMCapabilities.get(entity, PlayerCapabilityProvider.PLAYER_CAP).ifPresent(cap -> {
            cap.setExtraRenderFlag(true);
        });
        TouhouLittleMaidCompat.syncMaidState(entity);
    }
}