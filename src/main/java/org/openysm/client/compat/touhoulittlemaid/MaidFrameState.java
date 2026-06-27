package org.openysm.client.compat.touhoulittlemaid;

import org.openysm.client.entity.LivingEntityFrameState;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;

public class MaidFrameState extends LivingEntityFrameState<EntityMaid> {
    public MaidFrameState(EntityMaid entityMaid) {
        super(entityMaid);
    }

    @Override
    public void reset() {
        super.reset();
    }
}