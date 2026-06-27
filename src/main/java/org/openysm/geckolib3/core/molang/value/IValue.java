package org.openysm.geckolib3.core.molang.value;

import org.openysm.OpenYSM;
import org.openysm.molang.runtime.ExpressionEvaluator;
import org.openysm.molang.runtime.binding.ValueConversions;

public interface IValue {
    Object evalUnsafe(ExpressionEvaluator<?> evaluator);

    default float evalAsFloat(ExpressionEvaluator<?> evaluator) {
        return ValueConversions.asFloat(evalSafe(evaluator));
    }

    default int evalAsInt(ExpressionEvaluator<?> evaluator) {
        return ValueConversions.asInt(evalSafe(evaluator));
    }

    default boolean evalAsBoolean(ExpressionEvaluator<?> evaluator) {
        return ValueConversions.asBoolean(evalSafe(evaluator));
    }

    default Object evalSafe(ExpressionEvaluator<?> evaluator) {
        try {
            return evalUnsafe(evaluator);
        } catch (Throwable th) {
            OpenYSM.LOGGER.debug("Failed to evaluate molang expression.", th);
            return null;
        }
    }
}