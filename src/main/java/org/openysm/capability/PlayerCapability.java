package org.openysm.capability;

import org.openysm.client.animation.molang.struct.RoamingStruct;
import org.openysm.client.animation.molang.struct.RoamingSyncBatch;
import org.openysm.client.compat.bettercombat.BetterCombatCompat;
import org.openysm.client.compat.firstperson.FirstPersonCompat;
import org.openysm.client.entity.PlayerEntityFrameState;
import org.openysm.client.entity.LivingAnimatable;
import org.openysm.client.model.ModelAssembly;
import org.openysm.client.entity.CustomPlayerEntity;
import org.openysm.geckolib3.geo.animated.AnimatedGeoModel;
import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.core.event.predicate.AnimationEvent;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.processor.IBone;
import org.openysm.molang.runtime.Int2FloatOpenHashMapStruct;
import org.openysm.molang.runtime.Struct;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SCompleteFeedbackPacket;
import org.openysm.network.message.C2SRequestExecuteMolangPacket;
import org.openysm.network.message.FeedbackData;
import org.openysm.forge.capability.PlayerCapabilityProvider;
import org.openysm.forge.capability.VehicleCapabilityProvider;
import it.unimi.dsi.fastutil.ints.Int2FloatMap;
import it.unimi.dsi.fastutil.ints.Int2FloatMaps;
import it.unimi.dsi.fastutil.ints.Int2FloatOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public final class PlayerCapability extends CustomPlayerEntity {

    private final Int2ReferenceOpenHashMap<MolangVarHolder> molangVarsMap;

    private int currentModelHashId;

    private Struct serverVarContainer;

    public PlayerCapability(Player player) {
        super(player, player instanceof LocalPlayer, true);
        this.molangVarsMap = new Int2ReferenceOpenHashMap<>(8);
    }

    public static LazyOptional<PlayerCapability> get(Player player) {
        return YSMCapabilities.get(player, PlayerCapabilityProvider.PLAYER_CAP);
    }

    @Override
    public PlayerEntityFrameState createPositionTracker(Player player) {
        return new PlayerEntityFrameState(player, player instanceof LocalPlayer);
    }

    @Override
    public PlayerEntityFrameState getPositionTracker() {
        return (PlayerEntityFrameState) super.getPositionTracker();
    }

    @Override
    @Nullable
    public Struct getServerVarContainer() {
        return this.serverVarContainer;
    }

    @Override
    public void onModelLoaded(ModelAssembly context) {
        super.onModelLoaded(context);
        this.currentModelHashId = getModelAssembly().getModelData().getHashId();
    }

    @Override
    public void clearModel() {
        this.currentModelHashId = 0;
        super.clearModel();
    }

    @Override
    public void setCurrentModel(AnimatedGeoModel model) {
        super.setCurrentModel(model);
        if (this.currentModelHashId == 0) {
            this.serverVarContainer = null;
            return;
        }
        MolangVarHolder varHolder = this.molangVarsMap.computeIfAbsent(this.currentModelHashId, i -> new MolangVarHolder());
        if (varHolder.currentVars == null) {
            varHolder.currentVars = new Int2FloatOpenHashMap(4);
        }
        varHolder.applyPendingDeltas();
        this.serverVarContainer = isLocalPlayerModel()
                ? new RoamingStruct(this.currentModelHashId, varHolder.currentVars)
                : new Int2FloatOpenHashMapStruct(varHolder.currentVars);
    }

    @Override
    public void reset() {
        this.serverVarContainer = null;
        super.reset();
    }

    @Override
    public void applyHeadTracking(AnimationEvent<? extends AnimatableEntity<Player>> event, boolean wasAnimEvaluated) {
        super.applyHeadTracking(event, wasAnimEvaluated);
        AnimatedGeoModel model2 = getCurrentModel();
        if (model2 != null && isLocalPlayerModel() && !event.isFirstPerson() && FirstPersonCompat.isLoaded()) {
            if (model2.allHeadBone() != null) {
                model2.allHeadBone().setHidden(FirstPersonCompat.shouldHideHead());
            }
            if (model2.viewLocatorBone() != null) {
                FirstPersonCompat.setCameraDistance(model2.viewLocatorBone().getPivotY() * getWidthScale());
            } else if (wasAnimEvaluated && !model2.headBones().isEmpty()) {
                IBone bone = model2.headBones().get(model2.headBones().size() - 1);
                FirstPersonCompat.setCameraDistance(bone == null ? 24.0f : bone.getPivotY() * getWidthScale());
            }
        }
    }

    @Override
    public void resetHeadTracking(boolean wasAnimEvaluated) {
        super.resetHeadTracking(wasAnimEvaluated);
        AnimatedGeoModel model2 = getCurrentModel();
        if (model2 != null && isLocalPlayerModel()) {
            if ((FirstPersonCompat.isLoaded() || BetterCombatCompat.isLoaded()) && model2.allHeadBone() != null) {
                model2.allHeadBone().setHidden(false);
            }
        }
    }

    public void updateMolangVars(int i, Int2FloatOpenHashMap int2FloatOpenHashMap) {
        MolangVarHolder varHolder = this.molangVarsMap.computeIfAbsent(i, i2 -> {
            return new MolangVarHolder();
        });
        if (isLocalPlayerModel()) {
            if (varHolder.currentVars == null) {
                varHolder.currentVars = int2FloatOpenHashMap;
                varHolder.applyPendingDeltas();
                if (i == this.currentModelHashId) {
                    this.serverVarContainer = new RoamingStruct(i, int2FloatOpenHashMap);
                    clearAnimationControllers();
                    return;
                }
                return;
            }
            return;
        }
        varHolder.currentVars = int2FloatOpenHashMap;
        varHolder.applyPendingDeltas();
        if (i == this.currentModelHashId) {
            this.serverVarContainer = new Int2FloatOpenHashMapStruct(int2FloatOpenHashMap);
        }
    }

    public boolean hasMolangVars(int i) {
        return this.molangVarsMap.containsKey(i);
    }

    public boolean syncRoamingAssignments(String expression) {
        if (!isLocalPlayerModel() || !NetworkHandler.isClientConnected()) {
            return false;
        }
        boolean applied = applyRoamingAssignments(expression);
        if (applied) {
            NetworkHandler.sendToServer(new C2SRequestExecuteMolangPacket(expression, this.entity.getId()));
        }
        return applied;
    }

    public boolean applyRoamingAssignments(String expression) {
        if (this.currentModelHashId == 0) {
            return false;
        }
        String[] assignments = expression.split(";");
        String[] names = new String[assignments.length];
        float[] values = new float[assignments.length];
        int count = 0;
        for (String assignment : assignments) {
            ParsedRoamingAssignment parsed = parseRoamingAssignment(assignment);
            if (parsed == null) {
                continue;
            }
            ensureRoamingContainer();
            if (this.serverVarContainer != null) {
                this.serverVarContainer.putProperty(StringPool.computeIfAbsent(parsed.name), parsed.value);
            }
            names[count] = parsed.name;
            values[count] = parsed.value;
            count++;
        }
        if (count == 0) {
            return false;
        }
        if (isLocalPlayerModel() && NetworkHandler.isClientConnected()) {
            names = compactNames(names, count);
            values = compactValues(values, count);
            NetworkHandler.sendToServer(new C2SCompleteFeedbackPacket(new FeedbackData(this.currentModelHashId, new Object2FloatArrayMap(names, values), null, this.entity.getId())));
        }
        return true;
    }

    private void ensureRoamingContainer() {
        if (isLocalPlayerModel()) {
            if (this.serverVarContainer instanceof RoamingStruct) {
                return;
            }
            MolangVarHolder varHolder = this.molangVarsMap.computeIfAbsent(this.currentModelHashId, i -> new MolangVarHolder());
            if (varHolder.currentVars == null) {
                varHolder.currentVars = new Int2FloatOpenHashMap(4);
                varHolder.applyPendingDeltas();
            }
            this.serverVarContainer = new RoamingStruct(this.currentModelHashId, varHolder.currentVars);
            return;
        }
        if (this.serverVarContainer instanceof Int2FloatOpenHashMapStruct) {
            return;
        }
        MolangVarHolder varHolder = this.molangVarsMap.computeIfAbsent(this.currentModelHashId, i -> new MolangVarHolder());
        if (varHolder.currentVars == null) {
            varHolder.currentVars = new Int2FloatOpenHashMap(4);
            varHolder.applyPendingDeltas();
        }
        this.serverVarContainer = new Int2FloatOpenHashMapStruct(varHolder.currentVars);
    }

    private String[] compactNames(String[] names, int count) {
        if (count != names.length) {
            String[] compactNames = new String[count];
            System.arraycopy(names, 0, compactNames, 0, count);
            return compactNames;
        }
        return names;
    }

    private float[] compactValues(float[] values, int count) {
        if (count != values.length) {
            float[] compactValues = new float[count];
            System.arraycopy(values, 0, compactValues, 0, count);
            return compactValues;
        }
        return values;
    }

    @Nullable
    private ParsedRoamingAssignment parseRoamingAssignment(String assignment) {
        int equalsIndex = assignment.indexOf('=');
        if (equalsIndex <= 0) {
            return null;
        }
        String left = assignment.substring(0, equalsIndex).trim();
        String right = assignment.substring(equalsIndex + 1).trim();
        String name = parseRoamingName(left);
        if (name == null || name.length() > RoamingStruct.MAX_VAR_NAME_LENGTH || right.isEmpty()) {
            return null;
        }
        Float value = evaluateRoamingValue(right);
        return value != null ? new ParsedRoamingAssignment(name, value.floatValue()) : null;
    }

    @Nullable
    private static String parseRoamingName(String expression) {
        String lower = expression.toLowerCase();
        String prefix = lower.startsWith("v.roaming.") ? "v.roaming." : lower.startsWith("variable.roaming.") ? "variable.roaming." : null;
        if (prefix == null || expression.length() == prefix.length()) {
            return null;
        }
        String name = expression.substring(prefix.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '_') {
                return null;
            }
        }
        return name;
    }

    @Nullable
    private Float evaluateRoamingValue(String expression) {
        String value = expression.trim();
        if (value.endsWith(";")) {
            value = value.substring(0, value.length() - 1).trim();
        }
        if (value.isEmpty()) {
            return null;
        }
        if ("true".equalsIgnoreCase(value)) {
            return 1.0f;
        }
        if ("false".equalsIgnoreCase(value)) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ignored) {
        }
        if (value.startsWith("!")) {
            Float inner = evaluateRoamingValue(value.substring(1));
            return inner != null ? (inner.floatValue() == 0.0f ? 1.0f : 0.0f) : null;
        }
        String compact = value.replace(" ", "");
        if (compact.startsWith("1-")) {
            Float inner = evaluateRoamingValue(compact.substring(2));
            return inner != null ? 1.0f - inner.floatValue() : null;
        }
        String roamingName = parseRoamingName(value);
        if (roamingName != null && this.serverVarContainer != null) {
            Object property = this.serverVarContainer.getProperty(StringPool.computeIfAbsent(roamingName));
            if (property instanceof Number number) {
                return number.floatValue();
            }
        }
        return null;
    }

    private void applyMolangDelta(int i, Int2FloatMap int2FloatMap) {
        if (i == this.currentModelHashId && this.entity.getVehicle() != null && this.entity.getVehicle().getFirstPassenger() == this.entity) {
            org.openysm.capability.YSMCapabilities.get(this.entity.getVehicle(), VehicleCapabilityProvider.VEHICLE_CAP).ifPresent(cap -> {
                cap.updateFloatMap(int2FloatMap);
            });
        }
    }

    public void enqueueMolangDelta(int i, Int2FloatMap int2FloatMap) {
        if (!isLocalPlayerModel() && !int2FloatMap.isEmpty()) {
            MolangVarHolder varHolder = this.molangVarsMap.computeIfAbsent(i, i2 -> {
                return new MolangVarHolder();
            });
            if (varHolder.currentVars != null) {
                varHolder.currentVars.putAll(int2FloatMap);
            } else {
                varHolder.pendingDeltas.enqueue(int2FloatMap);
            }
            applyMolangDelta(i, int2FloatMap);
        }
    }

    public void tickAnimations() {
        if (isLocalPlayerModel() && this.currentModelHashId != 0) {
            Struct struct = this.serverVarContainer;
            if (struct instanceof RoamingStruct roamingStruct) {
                if (roamingStruct.hasPendingChanges()) {
                    RoamingSyncBatch syncBatch = roamingStruct.consumePendingBoneData();
                    applyMolangDelta(syncBatch.modelHashId(), syncBatch.changedVariables());
                    String[] strArr = new String[syncBatch.changedVariables().size()];
                    float[] fArr = new float[syncBatch.changedVariables().size()];
                    int i = 0;
                    ObjectIterator it = Int2FloatMaps.fastIterable(syncBatch.changedVariables()).iterator();
                    while (it.hasNext()) {
                        Int2FloatMap.Entry entry = (Int2FloatMap.Entry) it.next();
                        String str = StringPool.getString(entry.getIntKey());
                        if (str.length() <= RoamingStruct.MAX_VAR_NAME_LENGTH) {
                            strArr[i] = str;
                            fArr[i] = entry.getFloatValue();
                        } else {
                            strArr[i] = StringPool.EMPTY;
                            fArr[i] = 0.0f;
                        }
                        i++;
                    }
                    NetworkHandler.sendToServer(new C2SCompleteFeedbackPacket(new FeedbackData(this.currentModelHashId, new Object2FloatArrayMap(strArr, fArr), null, this.entity.getId())));
                }
            }
        }
    }

    public void copyFrom(PlayerCapability playerCapability) {
        this.molangVarsMap.putAll(playerCapability.molangVarsMap);
        initModelWithTexture(playerCapability.getModelId(), playerCapability.currentTextureName);
        setForceDisabled(playerCapability.isForceDisabled());
        playerCapability.molangVarsMap.clear();
        playerCapability.serverVarContainer = null;
    }

    @Override
    @NotNull
    public LivingAnimatable<Player>.TexturedModelWrapper buildRenderShape(ModelAssembly modelAssembly, boolean isActive) {
        return new TexturedModelWrapper(modelAssembly, isActive, true, true, 600);
    }

    private static class MolangVarHolder {

        public volatile Int2FloatOpenHashMap currentVars;

        public final ObjectArrayFIFOQueue<Int2FloatMap> pendingDeltas = new ObjectArrayFIFOQueue<>(4);

        private MolangVarHolder() {
        }

        public void applyPendingDeltas() {
            while (!this.pendingDeltas.isEmpty()) {
                this.currentVars.putAll(this.pendingDeltas.dequeue());
            }
        }
    }

    private record ParsedRoamingAssignment(String name, float value) {
    }
}
