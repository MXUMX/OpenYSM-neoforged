/*
 * Decompiled with CFR 0.152.
 */
package rip.ysm.compat.touhoulittlemaid.forge;

import org.openysm.client.compat.touhoulittlemaid.TouhouMaidBoneProcessor;
import org.openysm.geckolib3.geo.animated.AnimatedGeoBone;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;

public final class TouhouMaidBoneProcessorImpl {
    private TouhouMaidBoneProcessorImpl() {
    }

    public static Object createLocationBone(AnimatedGeoBone bone) {
        return TouhouMaidBoneProcessor.createLocationBone(bone);
    }

    public static Object createLocationModel(AnimatedGeoModel model) {
        return TouhouMaidBoneProcessor.createLocationModel(model);
    }
}

