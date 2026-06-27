/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.BooleanUtils
 *  org.apache.commons.lang3.math.NumberUtils
 */
package rip.ysm.gui.molang;

import org.openysm.OpenYSM;
import org.openysm.config.ServerConfig;
import org.openysm.geckolib3.core.AnimatableEntity;
import org.openysm.geckolib3.resource.GeckoLibCache;
import org.openysm.molang.parser.ParseException;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.C2SRequestExecuteMolangPacket;
import java.util.function.Consumer;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import rip.ysm.gui.Option;
import rip.ysm.gui.molang.LiveOption;

public final class MolangOption {
    private MolangOption() {
    }

    public static Option<Boolean> ofBoolean(String title, String description, AnimatableEntity<?> animatable, String expr) {
        boolean[] cache = new boolean[]{false};
        MolangOption.evaluate(animatable, expr, s -> {
            cache[0] = MolangOption.toFloat(s) > 0.0f;
        });
        return new LiveOption<Boolean>(title, description, () -> cache[0], v -> {
            cache[0] = v;
            MolangOption.execute(animatable, expr + "=" + (v != false ? "1" : "0"));
        });
    }

    public static Option<Double> ofDouble(String title, String description, AnimatableEntity<?> animatable, String expr) {
        double[] cache = new double[]{0.0};
        MolangOption.evaluate(animatable, expr, s -> {
            cache[0] = MolangOption.toFloat(s);
        });
        return new LiveOption<Double>(title, description, () -> cache[0], v -> {
            cache[0] = v;
            MolangOption.execute(animatable, expr + "=" + v);
        });
    }

    public static Option<Integer> ofIndex(String title, String description, AnimatableEntity<?> animatable, String readExpr, String[] writeExprs) {
        int[] cache = new int[]{0};
        MolangOption.evaluate(animatable, readExpr, s -> {
            cache[0] = Math.round(MolangOption.toFloat(s));
        });
        return new LiveOption<Integer>(title, description, () -> cache[0], v -> {
            int idx;
            cache[0] = idx = Math.max(0, Math.min(writeExprs.length - 1, v));
            MolangOption.execute(animatable, writeExprs[idx]);
        });
    }

    private static float toFloat(String s) {
        if (s == null || "null".equals(s)) {
            return 0.0f;
        }
        if (NumberUtils.isParsable((String)s)) {
            return Float.parseFloat(s);
        }
        Boolean b = BooleanUtils.toBooleanObject((String)s);
        return b != null && b != false ? 1.0f : 0.0f;
    }

    private static void evaluate(AnimatableEntity<?> animatable, String expr, Consumer<String> consumer) {
        try {
            animatable.executeExpression(GeckoLibCache.parseSimpleExpression(expr), true, false, consumer);
        }
        catch (ParseException e) {
            OpenYSM.LOGGER.error((Object)e);
        }
    }

    private static void execute(AnimatableEntity<?> animatable, String expr) {
        try {
            animatable.executeExpression(GeckoLibCache.parseSimpleExpression(expr), true, false, null);
            if (!GeckoLibCache.isRoamingVariableAssignment(expr) && NetworkHandler.isClientConnected() && !((Boolean)ServerConfig.LOW_BANDWIDTH_USAGE.get()).booleanValue()) {
                NetworkHandler.sendToServer(new C2SRequestExecuteMolangPacket(expr, animatable.getEntity().getId()));
            }
        }
        catch (ParseException e) {
            OpenYSM.LOGGER.error((Object)e);
        }
    }
}

