package org.openysm.event.api;

import org.openysm.client.entity.CustomPlayerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;


public class SpecialPlayerRenderEvent extends Event implements ICancellableEvent {

    private final Player player;

    private final CustomPlayerEntity customPlayer;

    private final String modelId;

    @Nullable
    private ResourceLocation textureLocation;

    public SpecialPlayerRenderEvent() {
        this.player = null;
        this.customPlayer = null;
        this.modelId = null;
    }

    public SpecialPlayerRenderEvent(Player player, CustomPlayerEntity customPlayer, String str) {
        this.player = player;
        this.customPlayer = customPlayer;
        this.modelId = str;
    }

    public Player getPlayer() {
        return this.player;
    }

    public CustomPlayerEntity getCustomPlayer() {
        return this.customPlayer;
    }

    public String getModelId() {
        return this.modelId;
    }

    @Nullable
    public ResourceLocation getTextureLocation() {
        return this.textureLocation;
    }

    public void setTextureLocation(@Nullable ResourceLocation resourceLocation) {
        this.textureLocation = resourceLocation;
    }
}
