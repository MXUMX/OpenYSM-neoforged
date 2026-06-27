package org.openysm.config;

import org.openysm.OpenYSM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import rip.ysm.neoforge.compat.registries.ForgeRegistries;

public class ModSoundEvents {

    public static final DeferredRegister<SoundEvent> REGISTER = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, OpenYSM.MOD_ID);

    public static final SoundEvent CUSTOM_SOUND = createSoundEvent("custom");

    private static SoundEvent createSoundEvent(String str) {
        SoundEvent soundEventCreateFixedRangeEvent = SoundEvent.createFixedRangeEvent(ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, str), 16.0f);
        REGISTER.register(str, () -> soundEventCreateFixedRangeEvent);
        return soundEventCreateFixedRangeEvent;
    }
}