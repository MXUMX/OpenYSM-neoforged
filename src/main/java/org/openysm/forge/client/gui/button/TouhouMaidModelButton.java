/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid
 *  com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler
 *  com.github.tartaricacid.touhoulittlemaid.network.message.YsmMaidModelMessage
 *  net.minecraft.network.chat.Component
 */
package org.openysm.client.gui.button;

import org.openysm.client.compat.touhoulittlemaid.capability.MaidCapabilityProvider;
import org.openysm.client.entity.PlayerPreviewEntity;
import org.openysm.client.gui.button.ModelButton;
import org.openysm.client.model.ModelAssembly;
import org.openysm.util.ComponentUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.YsmMaidModelMessage;
import net.minecraft.network.chat.Component;

public class TouhouMaidModelButton
extends ModelButton {
    private final EntityMaid maid;

    public TouhouMaidModelButton(int x, int y, boolean isAuthLocked, PlayerPreviewEntity previewEntity, ModelAssembly modelAssembly, EntityMaid entityMaid) {
        super(x, y, isAuthLocked, previewEntity, modelAssembly);
        this.maid = entityMaid;
    }

    @Override
    public void onPress() {
        if (this.isStarred) {
            return;
        }
        Component component = ComponentUtil.getDisplayName(this.renderContext, this.modelIdHolder.getModelId());
        org.openysm.capability.YSMCapabilities.get(this.maid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
            cap.setYsmModel(this.modelIdHolder.getModelId(), this.modelIdHolder.getCurrentTextureName());
            NetworkHandler.CHANNEL.sendToServer((Object)new YsmMaidModelMessage(this.maid.getId(), this.modelIdHolder.getModelId(), this.modelIdHolder.getCurrentTextureName(), component));
        });
    }
}
