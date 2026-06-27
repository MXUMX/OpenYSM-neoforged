package org.openysm.capability;

import org.openysm.client.entity.GeckoProjectileEntity;
import org.openysm.molang.runtime.Int2FloatOpenHashMapStruct;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import net.minecraft.world.entity.projectile.Projectile;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ProjectileCapability extends GeckoProjectileEntity {

    @Nullable
    private Int2FloatOpenHashMapStruct floatProperties;

    public ProjectileCapability(Projectile projectile) {
        super(projectile);
    }

    public void updateModelId(String str) {
        setModelId(str);
        markModelInitialized();
    }

    public void setFloatProperties(Int2FloatOpenHashMap int2FloatOpenHashMap) {
        if (int2FloatOpenHashMap != null) {
            this.floatProperties = new Int2FloatOpenHashMapStruct(int2FloatOpenHashMap);
        } else {
            this.floatProperties = null;
        }
    }

    @Override
    public void setupAnim(float seekTime, boolean isFirstPerson) {
        super.setupAnim(seekTime, isFirstPerson);
        getEvaluationContext().setRoamingProperties(this.floatProperties);
    }
}