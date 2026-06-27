/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.Projectile
 */
package org.openysm.capability.forge;

import org.openysm.capability.ProjectileCapability;
import org.openysm.forge.capability.ProjectileCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

public final class ProjectileCapabilityImpl {
    private ProjectileCapabilityImpl() {
    }

    public static Optional<ProjectileCapability> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, ProjectileCapabilityProvider.PROJECTILE_CAP).resolve();
    }

    public static Optional<ProjectileCapability> get(Projectile projectile) {
        return org.openysm.capability.YSMCapabilities.get(projectile, ProjectileCapabilityProvider.PROJECTILE_CAP).resolve();
    }
}

