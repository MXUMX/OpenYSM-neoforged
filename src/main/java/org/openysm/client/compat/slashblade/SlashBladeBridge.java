package org.openysm.client.compat.slashblade;

import org.openysm.client.entity.LivingAnimatable;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import org.openysm.geckolib3.core.processor.IBone;
import org.openysm.geckolib3.util.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public final class SlashBladeBridge {
    private static final ResourceLocation BLADE_OBJ = ResourceLocation.fromNamespaceAndPath("slashblade", "model/blade.obj");
    private static final ResourceLocation BLADE_TEXTURE = ResourceLocation.fromNamespaceAndPath("slashblade", "model/blade.png");
    private static final double SLASHBLADE_FRAMES_PER_TICK = 1.5;
    private static final List<LegacyDrawnFrameRange> LEGACY_DRAWN_FRAME_RANGES = List.of(
            frameRange(202.5, 281.25), frameRange(500.0, 571.25), frameRange(800.0, 877.5),
            frameRange(900.0, 1008.75), frameRange(700.0, 763.75), frameRange(710.0, 765.0),
            frameRange(405.0, 452.5), frameRange(1817.25, 1858.5), frameRange(1830.75, 1872.0),
            frameRange(1103.75, 1126.25), frameRange(1200.0, 1228.75), frameRange(1303.75, 1326.25),
            frameRange(1400.0, 1431.25), frameRange(1500.0, 1531.25), frameRange(2008.75, 2053.75),
            frameRange(1607.5, 1658.75), frameRange(1701.25, 1715.0), frameRange(2101.25, 2137.5),
            frameRange(725.0, 760.0), frameRange(2207.5, 2282.5)
    );
    private static final Set<String> LEGACY_DRAWN_FALLBACK_ANIMATIONS = Set.of(
            "slashblade:combo_a3", "slashblade:combo_a3_end", "slashblade:combo_a3_end2",
            "slashblade:combo_a4", "slashblade:combo_a4ex", "slashblade:combo_a4ex_end",
            "slashblade:combo_a4_ex_end", "slashblade:combo_a5ex", "slashblade:combo_b1",
            "slashblade:combo_b1_end", "slashblade:combo_b1_end2", "slashblade:combo_b2",
            "slashblade:combo_b3", "slashblade:combo_b4", "slashblade:combo_b5",
            "slashblade:combo_b6", "slashblade:combo_b7", "slashblade:combo_b7_end",
            "slashblade:combo_b_end", "slashblade:combo_b_end2", "slashblade:combo_c",
            "slashblade:aerial_cleave_landing", "slashblade:aerial_cleave_loop",
            "slashblade:aerial_rave_a1", "slashblade:aerial_rave_a1_end",
            "slashblade:aerial_rave_a2", "slashblade:aerial_rave_a2_end",
            "slashblade:aerial_rave_a3", "slashblade:aerial_rave_b3",
            "slashblade:aerial_rave_b4", "slashblade:rapid_slash",
            "slashblade:rapid_slash_end", "slashblade:upperslash",
            "slashblade:upperslash_jump", "slashblade:upperslash_jump_end",
            "slashblade:rising_star", "slashblade:rising_star_end",
            "slashblade:wave_edge_vertical", "slashblade:drive_vertical",
            "slashblade:sakura_end_left", "slashblade:sakura_end_right",
            "slashblade:sakura_end_finish", "slashblade:drive_horizontal",
            "slashblade:circle_slash", "slashblade:circle_slash_end",
            "slashblade:void_slash", "slashblade:void_slash_sheath",
            "slashblade_addon:water_drive", "slashblade_addon:spiral_edge",
            "slashblade_addon:fire_spiral"
    );

    private static Reflection reflection;
    private static boolean unavailable;

    private SlashBladeBridge() {
    }

    public static boolean isAvailable() {
        if (unavailable) {
            return false;
        }
        try {
            ensureReflection();
            return true;
        } catch (LinkageError | ReflectiveOperationException | RuntimeException ignored) {
            unavailable = true;
            return false;
        }
    }

    public static boolean isSlashBlade(ItemStack stack) {
        if (stack == null || stack.isEmpty() || unavailable) {
            return false;
        }
        try {
            return getBladeState(stack).isPresent();
        } catch (LinkageError | ReflectiveOperationException | RuntimeException ignored) {
            unavailable = true;
            return false;
        }
    }

    public static String getComboAnimationName(AnimationEvent<? extends LivingAnimatable<?>> event) {
        if (event == null || event.getAnimatable() == null) {
            return StringPool.EMPTY;
        }
        return getComboAnimationName(event.getAnimatable().getEntity());
    }

    public static String getComboAnimationName(IContext<? extends LivingEntity> context) {
        return context == null ? StringPool.EMPTY : getComboAnimationName(context.entity());
    }

    public static String getComboAnimationName(LivingEntity entity) {
        return getComboAnimationState(entity).animationName();
    }

    public static ComboAnimationState getComboAnimationState(LivingEntity entity) {
        if (entity == null || unavailable) {
            return ComboAnimationState.EMPTY;
        }
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        if (mainHand.isEmpty()) {
            return ComboAnimationState.EMPTY;
        }
        try {
            Optional<?> state = getBladeState(mainHand);
            if (state.isEmpty()) {
                return ComboAnimationState.EMPTY;
            }
            Object comboTicks = reflection.peekCurrentComboStateTicks.invoke(state.get(), entity);
            if (!(comboTicks instanceof Map.Entry<?, ?> entry) || entry.getValue() == null) {
                return ComboAnimationState.EMPTY;
            }
            int ticks = entry.getKey() instanceof Number number ? number.intValue() : 0;
            int startFrame = getComboStartFrame(entry.getValue());
            String rawName = entry.getValue().toString();
            return new ComboAnimationState(normalizeComboName(rawName, entity), rawName, ticks, startFrame);
        } catch (LinkageError | ReflectiveOperationException | RuntimeException ignored) {
            unavailable = true;
            return ComboAnimationState.EMPTY;
        }
    }

    public static void renderOnEntity(LivingEntity entity, AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, float partialTick) {
        if (stack.isEmpty() || unavailable) {
            return;
        }
        try {
            Optional<?> bladeState = getBladeState(stack);
            if (bladeState.isEmpty()) {
                return;
            }
            List<IBone> leftWaistBones = model.leftWaistBones();
            List<IBone> bladeBones = model.bladeBones();
            List<IBone> sheathBones = model.sheathBones();
            List<IBone> leftHandBones = model.leftHandBones();
            List<IBone> rightHandBones = model.rightHandBones();
            List<IBone> effectiveLeftWaistBones = leftWaistBones.isEmpty() ? leftHandBones : leftWaistBones;

            if (bladeBones.isEmpty() || sheathBones.isEmpty() || leftWaistBones.isEmpty()) {
                List<IBone> effectiveBladeBones = bladeBones.isEmpty() ? rightHandBones : bladeBones;
                List<IBone> effectiveSheathBones = sheathBones.isEmpty() ? leftHandBones : sheathBones;
                renderBladeLegacyFallback(entity, bladeState.get(), model, poseStack, bufferSource, packedLight, stack, partialTick, effectiveLeftWaistBones, effectiveBladeBones, effectiveSheathBones);
                return;
            }
            renderBladeWithBones(bladeState.get(), poseStack, bufferSource, packedLight, stack, effectiveLeftWaistBones, bladeBones, sheathBones);
        } catch (LinkageError | ReflectiveOperationException | RuntimeException ignored) {
            unavailable = true;
        }
    }

    public static void renderRightWaist(AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) {
        if (stack.isEmpty() || unavailable) {
            return;
        }
        try {
            if (getBladeState(stack).isEmpty()) {
                return;
            }
            poseStack.pushPose();
            try {
                List<IBone> rightWaistBones = model.rightWaistBones();
                if (!rightWaistBones.isEmpty()) {
                    RenderUtils.prepMatrixForLocator(poseStack, rightWaistBones);
                } else {
                    poseStack.translate(0.25, 1.25, 0.0);
                    poseStack.mulPose(Axis.XP.rotationDegrees(5.0f));
                }
                poseStack.translate(0.0, 0.0, -0.7);
                poseStack.scale(0.01f, 0.01f, 0.01f);
                poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
                renderBladeOnly(poseStack, bufferSource, packedLight, stack);
            } finally {
                poseStack.popPose();
            }
        } catch (LinkageError | ReflectiveOperationException | RuntimeException ignored) {
            unavailable = true;
        }
    }

    public static void renderBladeOnly(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack) throws ReflectiveOperationException {
        Optional<?> bladeState = getBladeState(stack);
        if (bladeState.isEmpty()) {
            return;
        }
        Object state = bladeState.get();
        ResourceLocation texture = getTexture(state);
        Object model = getBladeModel(state);
        renderBladeAndSheath(stack, model, getBladePart(state), texture, poseStack, bufferSource, packedLight);
    }

    public static boolean hasAnimation(AnimationEvent<? extends LivingAnimatable<?>> event, String animationName) {
        return event != null && animationName != null && !animationName.isBlank() && event.getAnimatable().getAnimation(animationName) != null;
    }

    private static void renderBladeWithBones(Object bladeState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, List<IBone> leftWaistBones, List<IBone> bladeBones, List<IBone> sheathBones) throws ReflectiveOperationException {
        ResourceLocation texture = getTexture(bladeState);
        Object model = getBladeModel(bladeState);
        String bladePart = getBladePart(bladeState);
        if (isVisibleLocator(leftWaistBones)) {
            renderBladeAndSheathAtLocator(leftWaistBones, stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        }
        if (isVisibleLocator(bladeBones)) {
            renderBladeAtLocator(bladeBones, stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        }
        if (isVisibleLocator(sheathBones)) {
            renderSheathAtLocator(sheathBones, stack, model, texture, poseStack, bufferSource, packedLight);
        }
    }

    private static void renderBladeLegacyFallback(LivingEntity entity, Object bladeState, AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, float partialTick, List<IBone> leftWaistBones, List<IBone> bladeBones, List<IBone> sheathBones) throws ReflectiveOperationException {
        ResourceLocation texture = getTexture(bladeState);
        Object bladeModel = getBladeModel(bladeState);
        String bladePart = getBladePart(bladeState);
        if (isBladeDrawnForLegacy(entity, partialTick) && !bladeBones.isEmpty()) {
            renderSheathAtLocator(sheathBones.isEmpty() ? leftWaistBones : sheathBones, stack, bladeModel, texture, poseStack, bufferSource, packedLight);
            renderBladeAtLocator(bladeBones, stack, bladeModel, bladePart, texture, poseStack, bufferSource, packedLight);
            return;
        }
        renderBladeOnWaist(model, poseStack, bufferSource, packedLight, stack, leftWaistBones, bladeState, texture, bladeModel, bladePart);
    }

    private static void renderBladeOnWaist(AnimatedGeoModel model, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ItemStack stack, List<IBone> leftWaistBones, Object bladeState, ResourceLocation texture, Object bladeModel, String bladePart) throws ReflectiveOperationException {
        poseStack.pushPose();
        try {
            if (!leftWaistBones.isEmpty()) {
                RenderUtils.prepMatrixForLocator(poseStack, leftWaistBones);
            } else {
                poseStack.translate(-0.25, 1.25, 0.0);
                poseStack.mulPose(Axis.XP.rotationDegrees(20.0f));
            }
            poseStack.translate(0.0, 0.0, -0.7);
            poseStack.scale(0.01f, 0.01f, 0.01f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            renderSheath(stack, bladeModel, texture, poseStack, bufferSource, packedLight);
            renderBlade(stack, bladeModel, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBladeAndSheath(ItemStack stack, Object model, String bladePart, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        renderBlade(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        renderSheath(stack, model, texture, poseStack, bufferSource, packedLight);
    }

    private static void renderBladeAndSheathAtLocator(List<IBone> locatorBones, ItemStack stack, Object model, String bladePart, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        try {
            RenderUtils.prepMatrixForLocator(poseStack, locatorBones);
            poseStack.translate(0.0, 0.025, -0.6);
            poseStack.scale(0.01f, 0.01f, 0.01f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            renderBladeAndSheath(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBladeAtLocator(List<IBone> locatorBones, ItemStack stack, Object model, String bladePart, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        try {
            RenderUtils.prepMatrixForLocator(poseStack, locatorBones);
            poseStack.translate(0.0, 0.035, 0.0);
            poseStack.scale(0.01f, 0.01f, 0.01f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.XP.rotationDegrees(180.0f));
            renderBlade(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderSheathAtLocator(List<IBone> locatorBones, ItemStack stack, Object model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        if (locatorBones.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        try {
            RenderUtils.prepMatrixForLocator(poseStack, locatorBones);
            poseStack.translate(0.0, 0.025, -0.6);
            poseStack.scale(0.01f, 0.01f, 0.01f);
            poseStack.mulPose(Axis.YP.rotationDegrees(-90.0f));
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0f));
            renderSheath(stack, model, texture, poseStack, bufferSource, packedLight);
        } finally {
            poseStack.popPose();
        }
    }

    private static void renderBlade(ItemStack stack, Object model, String bladePart, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        renderOverrided(stack, model, bladePart, texture, poseStack, bufferSource, packedLight);
        renderOverridedLuminous(stack, model, bladePart + "_luminous", texture, poseStack, bufferSource, packedLight);
    }

    private static void renderSheath(ItemStack stack, Object model, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        renderOverrided(stack, model, "sheath", texture, poseStack, bufferSource, packedLight);
        renderOverridedLuminous(stack, model, "sheath_luminous", texture, poseStack, bufferSource, packedLight);
    }

    private static boolean isVisibleLocator(List<IBone> bones) {
        if (bones.isEmpty()) {
            return false;
        }
        IBone bone = bones.get(bones.size() - 1);
        return bone.getScaleX() != 0.0f || bone.getScaleY() != 0.0f || bone.getScaleZ() != 0.0f;
    }

    private static boolean isBladeDrawnForLegacy(LivingEntity entity, float partialTick) {
        ComboAnimationState animationState = getComboAnimationState(entity);
        if (animationState.startFrame() < 0) {
            return LEGACY_DRAWN_FALLBACK_ANIMATIONS.contains(animationState.animationName()) || LEGACY_DRAWN_FALLBACK_ANIMATIONS.contains(animationState.rawName());
        }
        double frame = animationState.startFrame() + Math.max(0.0, animationState.ticks() + partialTick) * SLASHBLADE_FRAMES_PER_TICK;
        for (LegacyDrawnFrameRange range : LEGACY_DRAWN_FRAME_RANGES) {
            if (range.contains(frame)) {
                return true;
            }
        }
        return false;
    }

    private static String normalizeComboName(String comboName, LivingEntity entity) {
        return switch (comboName) {
            case "slashblade:combo_a4_ex" -> "slashblade:combo_a4ex";
            case "slashblade:judgement_cut" -> entity.onGround() ? comboName : "slashblade:judgement_cut_slash_air";
            case "slashblade:judgement_cut_slash_just2" -> entity.onGround() ? comboName : "slashblade:judgement_cut_slash_air_just2";
            default -> comboName;
        };
    }

    private static Optional<?> getBladeState(ItemStack stack) throws ReflectiveOperationException {
        ensureReflection();
        return (Optional<?>) reflection.bladeStateOf.invoke(null, stack);
    }

    private static int getComboStartFrame(Object comboLocation) throws ReflectiveOperationException {
        Object registry = reflection.comboStateRegistry.get(null);
        if (!(registry instanceof Registry<?> comboRegistry) || !(comboLocation instanceof ResourceLocation location)) {
            return -1;
        }
        Object comboState = comboRegistry.get(location);
        if (comboState == null) {
            return -1;
        }
        return (Integer) reflection.comboStateGetStartFrame.invoke(comboState);
    }

    private static ResourceLocation getTexture(Object bladeState) throws ReflectiveOperationException {
        Optional<?> texture = (Optional<?>) reflection.getTexture.invoke(bladeState);
        return texture.map(ResourceLocation.class::cast).orElse(BLADE_TEXTURE);
    }

    private static Object getBladeModel(Object bladeState) throws ReflectiveOperationException {
        Optional<?> modelLocation = (Optional<?>) reflection.getModel.invoke(bladeState);
        Object manager = reflection.getBladeModelManager.invoke(null);
        return reflection.getBladeModel.invoke(manager, modelLocation.isPresent() ? modelLocation.get() : BLADE_OBJ);
    }

    private static String getBladePart(Object bladeState) throws ReflectiveOperationException {
        return (Boolean) reflection.isBroken.invoke(bladeState) ? "blade_damaged" : "blade";
    }

    private static void renderOverrided(ItemStack stack, Object model, String target, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        invokeRender(reflection.renderOverrided, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void renderOverridedLuminous(ItemStack stack, Object model, String target, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        invokeRender(reflection.renderOverridedLuminous, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void invokeRender(Method method, ItemStack stack, Object model, String target, ResourceLocation texture, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) throws ReflectiveOperationException {
        method.invoke(null, stack, model, target, texture, poseStack, bufferSource, packedLight);
    }

    private static void ensureReflection() throws ReflectiveOperationException {
        if (reflection != null) {
            return;
        }
        Class<?> bladeStateAccess = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess");
        Class<?> slashBladeState = Class.forName("mods.flammpfeil.slashblade.capability.slashblade.ISlashBladeState");
        Class<?> comboStateRegistry = Class.forName("mods.flammpfeil.slashblade.registry.ComboStateRegistry");
        Class<?> comboState = Class.forName("mods.flammpfeil.slashblade.registry.combo.ComboState");
        Class<?> bladeModelManager = Class.forName("mods.flammpfeil.slashblade.client.renderer.model.BladeModelManager");
        Class<?> wavefrontObject = Class.forName("mods.flammpfeil.slashblade.client.renderer.model.obj.WavefrontObject");
        Class<?> bladeRenderState = Class.forName("mods.flammpfeil.slashblade.client.renderer.util.BladeRenderState");
        Field registry = comboStateRegistry.getField("REGISTRY");
        reflection = new Reflection(
                bladeStateAccess.getMethod("of", ItemStack.class),
                slashBladeState.getMethod("peekCurrentComboStateTicks", LivingEntity.class),
                registry,
                comboState.getMethod("getStartFrame"),
                slashBladeState.getMethod("getTexture"),
                slashBladeState.getMethod("getModel"),
                slashBladeState.getMethod("isBroken"),
                bladeModelManager.getMethod("getInstance"),
                bladeModelManager.getMethod("getModel", ResourceLocation.class),
                bladeRenderState.getMethod("renderOverrided", ItemStack.class, wavefrontObject, String.class, ResourceLocation.class, PoseStack.class, MultiBufferSource.class, Integer.TYPE),
                bladeRenderState.getMethod("renderOverridedLuminous", ItemStack.class, wavefrontObject, String.class, ResourceLocation.class, PoseStack.class, MultiBufferSource.class, Integer.TYPE)
        );
    }

    private static LegacyDrawnFrameRange frameRange(double startInclusive, double endExclusive) {
        return new LegacyDrawnFrameRange(startInclusive, endExclusive);
    }

    public record ComboAnimationState(String animationName, String rawName, int ticks, int startFrame) {
        private static final ComboAnimationState EMPTY = new ComboAnimationState(StringPool.EMPTY, StringPool.EMPTY, 0, -1);
    }

    private record LegacyDrawnFrameRange(double startInclusive, double endExclusive) {
        private boolean contains(double frame) {
            return startInclusive <= frame && frame < endExclusive;
        }
    }

    private record Reflection(Method bladeStateOf, Method peekCurrentComboStateTicks, Field comboStateRegistry, Method comboStateGetStartFrame, Method getTexture, Method getModel, Method isBroken, Method getBladeModelManager, Method getBladeModel, Method renderOverrided, Method renderOverridedLuminous) {
    }
}
