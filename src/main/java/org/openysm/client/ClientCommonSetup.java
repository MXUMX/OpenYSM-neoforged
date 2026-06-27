package org.openysm.client;

import org.openysm.client.compat.touhoulittlemaid.TouhouMaidCompat;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ClientCommonSetup {
    private ClientCommonSetup() {
    }

    public static void nativeInit() {
        TouhouMaidCompat.init();
        ClientModelManager.loadDefaultModel();
    }
}
