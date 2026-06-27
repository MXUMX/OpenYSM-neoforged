package org.openysm.client;

import org.openysm.capability.ClientLazyCapability;
import org.openysm.capability.PlayerCapability;
import org.openysm.capability.ProjectileCapability;
import org.openysm.capability.VehicleCapability;
import org.openysm.forge.capability.ProjectileCapabilityProvider;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class ClientAttachmentFactory {
    private ClientAttachmentFactory() {
    }

    public static PlayerCapability createPlayer(Player player) {
        return new PlayerCapability(player);
    }

    public static ProjectileCapability createProjectile(Projectile projectile) {
        return new ProjectileCapability(projectile);
    }

    public static VehicleCapability createVehicle(Entity entity) {
        return new VehicleCapability(entity);
    }

    public static ClientLazyCapability createClientLazy(Entity entity) {
        @Nullable ProjectileCapabilityProvider projectileProvider = entity instanceof Projectile projectile ? new ProjectileCapabilityProvider(projectile) : null;
        return new ClientLazyCapability(new VehicleCapabilityProvider(entity), projectileProvider);
    }
}
