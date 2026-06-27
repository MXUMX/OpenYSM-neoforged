/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.neoforged.api.distmarker.Dist
 *  net.neoforged.bus.api.SubscribeEvent
 *  net.neoforged.fml.ModLoader
 *  net.neoforged.fml.ModLoadingStage
 *  net.neoforged.fml.ModLoadingWarning
 *  net.neoforged.fml.common.Mod$EventBusSubscriber
 *  net.neoforged.fml.common.Mod$EventBusSubscriber$Bus
 *  net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
 *  net.neoforged.fml.loading.LoadingModList
 *  net.neoforged.neoforgespi.language.IModInfo
 *  org.apache.commons.lang3.tuple.Pair
 */
package org.openysm.forge;

import org.openysm.OpenYSM;
import org.openysm.client.ClientModelManager;
import org.openysm.client.compat.acceleratedrendering.AcceleratedRenderingCompat;
import org.openysm.client.compat.bettercombat.BetterCombatCompat;
import org.openysm.client.compat.carryon.CarryOnCompat;
import org.openysm.client.compat.cosmeticarmorreworked.CosmeticArmorCompat;
import org.openysm.client.compat.create.CreateCompat;
import org.openysm.client.compat.curios.CuriosCompat;
import org.openysm.client.compat.elytraslot.ElytraSlotCompat;
import org.openysm.client.compat.firstperson.FirstPersonCompat;
import org.openysm.client.compat.gun.swarfare.SWarfareCompat;
import org.openysm.client.compat.gun.tacz.TacCompat;
import org.openysm.client.compat.immersiveaircraft.ImmersiveAirCraftCompat;
import org.openysm.client.compat.immersivemelodies.ImmersiveMelodiesCompat;
import org.openysm.client.compat.ironsspellbooks.SpellbooksCompat;
import org.openysm.client.compat.oculus.OculusCompat;
import org.openysm.client.compat.optifine.OptiFineDetector;
import org.openysm.client.compat.parcool.ParcoolCompat;
import org.openysm.client.compat.playeranimator.PlayerAnimatorCompat;
import org.openysm.client.compat.realcamera.RealCameraCompat;
import org.openysm.client.compat.sbackpack.SBackpackCompat;
import org.openysm.client.compat.simplehats.SimpleHatsHelper;
import org.openysm.client.compat.simpleplanes.SimplePlanesCompat;
import org.openysm.client.compat.slashblade.SlashBladeCompat;
import org.openysm.client.compat.swem.SWEMCompat;
import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import java.util.Optional;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.tuple.Pair;
public final class ForgeClientSetupHooks {
    private ForgeClientSetupHooks() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        if (!OpenYSM.isAvailable()) {
            return;
        }
        event.enqueueWork(() -> {
            CuriosCompat.init();
            FirstPersonCompat.init();
            RealCameraCompat.init();
            PlayerAnimatorCompat.init();
            BetterCombatCompat.init();
            OculusCompat.init();
            AcceleratedRenderingCompat.init();
            OptiFineDetector.init();
            CosmeticArmorCompat.init();
            ElytraSlotCompat.init();
            TacCompat.init();
            SWarfareCompat.init();
            TouhouLittleMaidCompat.init();
            CarryOnCompat.init();
            ParcoolCompat.init();
            SlashBladeCompat.init();
            SWEMCompat.init();
            CreateCompat.init();
            SBackpackCompat.init();
            SimpleHatsHelper.init();
            ImmersiveMelodiesCompat.init();
            SpellbooksCompat.init();
            SimplePlanesCompat.init();
            ImmersiveAirCraftCompat.init();
            ForgeClientSetupHooks.showInCompatibleMod(SBackpackCompat.getInCompatibleInfo());
            ForgeClientSetupHooks.showInCompatibleMod(ParcoolCompat.getInCompatibleInfo());
            ForgeClientSetupHooks.showInCompatibleMod("epicfight", "Epic Fight");
            ClientModelManager.loadDefaultModel();
        });
    }

    private static void showInCompatibleMod(Optional<Pair<String, String>> optional) {
        optional.ifPresent(pair -> OpenYSM.LOGGER.warn("Incompatible optional mod version: {} {}", pair.getKey(), pair.getValue()));
    }

    private static void showInCompatibleMod(String str, String str2) {
        OpenYSM.LOGGER.warn("Incompatible optional mod detected: {}", str2);
    }
}
