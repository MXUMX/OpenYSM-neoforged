package org.openysm.client.renderer;

import org.openysm.OpenYSM;
import org.openysm.client.compat.sbackpack.SBackpackCompat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.bus.api.SubscribeEvent;
public class RendererManager {

    private static CustomPlayerRenderer playerRenderer;

    private static ProjectileRenderer projectileRenderer;

    private static HandItemRenderer handRenderer;

    private static VehicleRenderer vehicleRenderer;

    private static void initRenderers(ResourceManager resourceManager) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRendererProvider.Context context = new EntityRendererProvider.Context(entityRenderDispatcher, Minecraft.getInstance().getItemRenderer(), Minecraft.getInstance().getBlockRenderer(), entityRenderDispatcher.getItemInHandRenderer(), resourceManager, Minecraft.getInstance().getEntityModels(), Minecraft.getInstance().font);
        playerRenderer = new CustomPlayerRenderer(context);
        projectileRenderer = new ProjectileRenderer(context);
        handRenderer = new HandItemRenderer();
        vehicleRenderer = new VehicleRenderer(context);
        SBackpackCompat.setupRenderLayers();
    }

    @SubscribeEvent
    public static void onAddReloadListener(AddReloadListenerEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        event.addListener((ResourceManagerReloadListener) RendererManager::initRenderers);
    }

    public static CustomPlayerRenderer getPlayerRenderer() {
        if (playerRenderer == null) {
            initRenderers(Minecraft.getInstance().getResourceManager());
        }
        return playerRenderer;
    }

    public static ProjectileRenderer getProjectileRenderer() {
        if (projectileRenderer == null) {
            initRenderers(Minecraft.getInstance().getResourceManager());
        }
        return projectileRenderer;
    }

    public static HandItemRenderer getHandRenderer() {
        if (handRenderer == null) {
            initRenderers(Minecraft.getInstance().getResourceManager());
        }
        return handRenderer;
    }

    public static VehicleRenderer getVehicleRenderer() {
        if (vehicleRenderer == null) {
            initRenderers(Minecraft.getInstance().getResourceManager());
        }
        return vehicleRenderer;
    }
}