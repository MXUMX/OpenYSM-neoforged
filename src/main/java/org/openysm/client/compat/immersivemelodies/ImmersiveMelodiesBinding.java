package org.openysm.client.compat.immersivemelodies;

import org.openysm.client.animation.molang.CtrlBinding;
import org.openysm.client.entity.LivingEntityFrameState;
import immersive_melodies.client.MelodyProgress;
import immersive_melodies.client.MelodyProgressManager;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

import java.lang.reflect.Method;

public class ImmersiveMelodiesBinding {
    private static final Object NO_INSTRUMENT_METHOD = new Object();
    private static Object getInstrumentMethod;

    public static void registerControllerFunctions(CtrlBinding binding) {
        binding.livingEntityVar("im_pitch", ctx -> {
            Object tracker = ctx.geoInstance().getPositionTracker();
            if (tracker instanceof LivingEntityFrameState) {
                return ((LivingEntityFrameState<?>) tracker).getImmersiveMelodiesData().pitch;
            }
            return 0.0f;
        });
        binding.livingEntityVar("im_volume", ctx -> {
            Object tracker = ctx.geoInstance().getPositionTracker();
            if (tracker instanceof LivingEntityFrameState) {
                return ((LivingEntityFrameState<?>) tracker).getImmersiveMelodiesData().volume;
            }
            return 0.0f;
        });
        binding.livingEntityVar("im_current", ctx -> {
            Object tracker = ctx.geoInstance().getPositionTracker();
            if (tracker instanceof LivingEntityFrameState) {
                return ((LivingEntityFrameState<?>) tracker).getImmersiveMelodiesData().current;
            }
            return 0.0f;
        });
        binding.livingEntityVar("im_delta", ctx -> {
            Object tracker = ctx.geoInstance().getPositionTracker();
            if (tracker instanceof LivingEntityFrameState) {
                return ((LivingEntityFrameState<?>) tracker).getImmersiveMelodiesData().delta;
            }
            return 0L;
        });
        binding.livingEntityVar("im_time", ctx -> {
            Object tracker = ctx.geoInstance().getPositionTracker();
            if (tracker instanceof LivingEntityFrameState) {
                return ((LivingEntityFrameState<?>) tracker).getImmersiveMelodiesData().time;
            }
            return 0L;
        });
    }

    public static void updateInstrumentData(LivingEntity entity, ImmersiveMelodiesCompat.ImmersiveMelodiesData imData) {
        if (hasInstrument(entity)) {
            float frameTime = (Minecraft.getInstance().isPaused() ? 0.0f : Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(false)) + entity.tickCount;
            MelodyProgress progress = MelodyProgressManager.INSTANCE.getProgress(entity);
            progress.visualTick(frameTime);
            imData.pitch = progress.getCurrentPitch();
            imData.volume = progress.getCurrentVolume();
            imData.current = progress.getCurrent();
            imData.delta = progress.delta();
            imData.time = progress.getTime();
        }
    }

    private static boolean hasInstrument(LivingEntity entity) {
        Object method = getInstrumentMethod();
        if (method == NO_INSTRUMENT_METHOD) {
            return true;
        }
        try {
            return ((Method) method).invoke(null, entity) != null;
        } catch (ReflectiveOperationException | LinkageError ignored) {
            return false;
        }
    }

    private static Object getInstrumentMethod() {
        Object method = getInstrumentMethod;
        if (method != null) {
            return method;
        }
        try {
            Class<?> animatorClass = Class.forName("immersive_melodies.client.animation.EntityModelAnimator", false, ImmersiveMelodiesBinding.class.getClassLoader());
            method = findGetInstrument(animatorClass, LivingEntity.class);
            if (method == null) {
                method = findGetInstrument(animatorClass, Entity.class);
            }
        } catch (ClassNotFoundException | LinkageError ignored) {
            method = NO_INSTRUMENT_METHOD;
        }
        if (method == null) {
            method = NO_INSTRUMENT_METHOD;
        }
        getInstrumentMethod = method;
        return method;
    }

    private static Method findGetInstrument(Class<?> animatorClass, Class<?> parameterType) {
        try {
            return animatorClass.getMethod("getInstrument", parameterType);
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }
}
