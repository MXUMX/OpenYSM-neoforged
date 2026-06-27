package org.openysm.client.compat.simplehats;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class SimpleHatsHelper {

    private static final String MOD_ID = "simplehats";
    private static final String CURIOS_MOD_ID = "curios";
    private static final String ACCESSORIES_MOD_ID = "accessories";

    private static boolean IS_LOADED = false;
    private static boolean CURIOS_LOADED = false;
    private static boolean ACCESSORIES_LOADED = false;

    public static void init() {
        IS_LOADED = ModList.get().isLoaded(MOD_ID);
        CURIOS_LOADED = ModList.get().isLoaded(CURIOS_MOD_ID);
        ACCESSORIES_LOADED = ModList.get().isLoaded(ACCESSORIES_MOD_ID);
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    @Nullable
    public static ItemStack getHatItem(LivingEntity livingEntity) {
        if (IS_LOADED) {
            if (ACCESSORIES_LOADED) {
                return HatAccessoriesGetter.getHeadAccessory(livingEntity);
            }
            if (CURIOS_LOADED) {
                return HatCuriosGetter.getHeadCurio(livingEntity);
            }
        }
        return null;
    }
}
