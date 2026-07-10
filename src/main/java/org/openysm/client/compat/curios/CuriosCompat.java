package org.openysm.client.compat.curios;

import org.openysm.geckolib3.core.molang.binding.ContextBinding;
import org.openysm.molang.runtime.Function;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.neoforged.fml.loading.LoadingModList;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class CuriosCompat {

    private static final String MOD_ID = "curios";
    private static final String CURIOS_API_CLASS = "top.theillusivec4.curios.api.CuriosApi";

    private static boolean IS_LOADED;
    private static Method getCuriosInventory;

    public static void init() {
        IS_LOADED = LoadingModList.get().getModFileById(MOD_ID) != null;
    }

    public static boolean isLoaded() {
        return IS_LOADED;
    }

    /**
     * Retrieves a Curios inventory without linking against either of Curios' incompatible
     * getCuriosInventory return types. Curios 5 returns LazyOptional while Curios 9 returns
     * java.util.Optional; both expose the same ICuriosItemHandler API once unwrapped.
     */
    public static Optional<ICuriosItemHandler> getCuriosInventory(LivingEntity livingEntity) {
        if (!IS_LOADED) {
            return Optional.empty();
        }
        try {
            if (getCuriosInventory == null) {
                getCuriosInventory = Class.forName(CURIOS_API_CLASS).getMethod("getCuriosInventory", LivingEntity.class);
            }
            Object inventory = getCuriosInventory.invoke(null, livingEntity);
            return unwrapInventory(inventory);
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return Optional.empty();
        }
    }

    private static Optional<ICuriosItemHandler> unwrapInventory(Object inventory) {
        if (inventory == null) {
            return Optional.empty();
        }
        if (inventory instanceof Optional<?> optional) {
            return asCuriosItemHandler(optional);
        }
        try {
            Object resolved = inventory.getClass().getMethod("resolve").invoke(inventory);
            return resolved instanceof Optional<?> optional ? asCuriosItemHandler(optional) : Optional.empty();
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return Optional.empty();
        }
    }

    private static Optional<ICuriosItemHandler> asCuriosItemHandler(Optional<?> inventory) {
        return inventory.filter(ICuriosItemHandler.class::isInstance).map(ICuriosItemHandler.class::cast);
    }

    public static boolean hasItemInSlot(LivingEntity livingEntity, String str, ReferenceOpenHashSet<Item> set) {
        return getCuriosInventory(livingEntity).flatMap(handler -> handler.getStacksHandler(str)).map(handler -> CuriosBinding.findInSlot(handler, itemStack -> set.isEmpty() || set.contains(itemStack.getItem())) != null).orElse(false);
    }

    public static boolean hasTaggedItemInSlot(LivingEntity livingEntity, String str, List<TagKey<Item>> list) {
        return getCuriosInventory(livingEntity).flatMap(handler -> handler.getStacksHandler(str)).map(handler -> CuriosBinding.findInSlot(handler, itemStack -> {
            for (TagKey<Item> itemTagKey : list) {
                if (itemStack.is(itemTagKey)) {
                    return true;
                }
            }
            return false;
        }) != null).orElse(false);
    }

    public static boolean hasNoTaggedItemInSlot(LivingEntity entity, String str, List<TagKey<Item>> list) {
        return getCuriosInventory(entity).flatMap(handler -> handler.getStacksHandler(str)).map(handler -> CuriosBinding.findInSlot(handler, itemStack -> {
            for (TagKey<Item> itemTagKey : list) {
                if (!itemStack.is(itemTagKey)) {
                    return false;
                }
            }
            return true;
        }) != null).orElse(false);
    }

    public static void registerCuriosItems(ContextBinding binding) {
        if (IS_LOADED) {
            CuriosBinding.registerBindings(binding);
        } else {
            registerDummyBindings(binding);
        }
    }

    private static void registerDummyBindings(ContextBinding binding) {
        binding.function("has_any_curios", Function.NOOP);
        binding.function("has_any_curios_with_all_tags", Function.NOOP);
        binding.function("has_any_curios_with_any_tag", Function.NOOP);
        binding.livingEntityVar("dump_curios", context -> {
            context.logWarning("Curios not installed.");
            return null;
        });
    }
}
