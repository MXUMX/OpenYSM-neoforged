/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 */
package rip.ysm.compat.touhoulittlemaid;

import org.openysm.geckolib3.geo.animated.AnimatedGeoBone;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import dev.architectury.injectables.annotations.ExpectPlatform;
import rip.ysm.compat.touhoulittlemaid.forge.TouhouMaidBoneProcessorImpl;

public final class TouhouMaidBoneProcessor {
    private TouhouMaidBoneProcessor() {
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Object createLocationBone(AnimatedGeoBone bone) {
        return TouhouMaidBoneProcessorImpl.createLocationBone(bone);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Object createLocationModel(AnimatedGeoModel model) {
        return TouhouMaidBoneProcessorImpl.createLocationModel(model);
    }
}

