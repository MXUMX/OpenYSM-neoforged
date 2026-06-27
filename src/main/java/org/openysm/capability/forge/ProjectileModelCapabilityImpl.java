/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.projectile.Projectile
 */
package org.openysm.capability.forge;

import org.openysm.capability.ProjectileModelCapability;
import org.openysm.forge.capability.ProjectileModelCapabilityProvider;
import java.util.Optional;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;

public final class ProjectileModelCapabilityImpl {
    private ProjectileModelCapabilityImpl() {
    }

    public static Optional<ProjectileModelCapability> get(Entity entity) {
        return org.openysm.capability.YSMCapabilities.get(entity, ProjectileModelCapabilityProvider.PROJECTILE_MODEL).resolve();
    }

    public static Optional<ProjectileModelCapability> get(Projectile projectile) {
        return org.openysm.capability.YSMCapabilities.get(projectile, ProjectileModelCapabilityProvider.PROJECTILE_MODEL).resolve();
    }
}

