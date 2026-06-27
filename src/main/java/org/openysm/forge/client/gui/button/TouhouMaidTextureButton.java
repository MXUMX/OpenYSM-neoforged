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
import org.openysm.client.gui.button.TextureButton;
import org.openysm.client.model.ModelAssembly;
import org.openysm.util.ComponentUtil;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.network.NetworkHandler;
import com.github.tartaricacid.touhoulittlemaid.network.message.YsmMaidModelMessage;
import net.minecraft.network.chat.Component;

public class TouhouMaidTextureButton
extends TextureButton {
    private final EntityMaid maid;
    private final int index;
    private String textureId;
    private String textureName;
    private Component displayComponent;

    public TouhouMaidTextureButton(int x, int y, PlayerPreviewEntity previewEntity, EntityMaid entityMaid, int textureIndex, ModelAssembly modelAssembly) {
        super(x, y, previewEntity, modelAssembly);
        this.maid = new EntityMaid(entityMaid.level());
        this.maid.setIsYsmModel(true);
        this.maid.setNoAi(true);
        this.index = entityMaid.getId();
        org.openysm.capability.YSMCapabilities.get(entityMaid, MaidCapabilityProvider.MAID_CAP).ifPresent(cap -> {
            this.textureId = cap.getModelId();
            ModelAssembly modelAssembly2 = cap.getModelAssembly();
            this.displayComponent = ComponentUtil.getDisplayName(modelAssembly2, this.textureId);
            this.textureName = modelAssembly2.getAnimationBundle().getTextures().getKeyAt(textureIndex);
            this.maid.setYsmModel(this.textureId, this.textureName, this.displayComponent);
            previewEntity.initModelWithTexture(this.textureId, this.textureName);
        });
    }

    @Override
    public void onPress() {
        this.maid.setYsmModel(this.textureId, this.textureName, this.displayComponent);
        NetworkHandler.CHANNEL.sendToServer((Object)new YsmMaidModelMessage(this.index, this.textureId, this.textureName, this.displayComponent));
    }
}
