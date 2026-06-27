package org.openysm.mixin.client;

import org.openysm.capability.PlayerCapability;
import org.openysm.client.renderer.CustomPlayerRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.event.RenderLivingEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(targets = "mods.flammpfeil.slashblade.event.client.UserPoseOverrider", remap = false)
public abstract class SlashBladeUserPoseOverriderMixin {
    @Inject(method = "onRenderPlayerEventPre", at = @At("HEAD"), cancellable = true, require = 0)
    private void openysm$skipYsmPlayerPoseOverride(RenderLivingEvent.Pre<?, ?> event, CallbackInfo ci) {
        if (event.isCanceled() || isOpenYsmRenderer(event.getRenderer()) || isOpenYsmModelActive(event.getEntity())) {
            ci.cancel();
        }
    }

    private static boolean isOpenYsmRenderer(LivingEntityRenderer<?, ?> renderer) {
        return renderer instanceof CustomPlayerRenderer;
    }

    private static boolean isOpenYsmModelActive(LivingEntity entity) {
        return entity instanceof Player player && PlayerCapability.get(player).map(PlayerCapability::isModelActive).orElse(false);
    }
}
