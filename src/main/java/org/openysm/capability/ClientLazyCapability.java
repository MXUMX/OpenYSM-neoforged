package org.openysm.capability;

import org.openysm.forge.capability.ProjectileCapabilityProvider;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import org.jetbrains.annotations.Nullable;

public class ClientLazyCapability {
    private final VehicleCapabilityProvider entityRenderProvider;
    @Nullable
    private final ProjectileCapabilityProvider projectileAnimProvider;

    public ClientLazyCapability(VehicleCapabilityProvider capabilityProvider, @Nullable ProjectileCapabilityProvider projectileCapabilityProvider) {
        this.entityRenderProvider = capabilityProvider;
        this.projectileAnimProvider = projectileCapabilityProvider;
    }

    public VehicleCapabilityProvider getEntityRenderProvider() {
        return this.entityRenderProvider;
    }

    @Nullable
    public ProjectileCapabilityProvider getProjectileAnimProvider() {
        return this.projectileAnimProvider;
    }
}
