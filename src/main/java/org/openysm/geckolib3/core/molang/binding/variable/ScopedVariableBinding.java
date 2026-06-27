package org.openysm.geckolib3.core.molang.binding.variable;

import org.openysm.geckolib3.core.molang.context.IContext;
import org.openysm.geckolib3.core.molang.util.StringPool;
import org.openysm.geckolib3.core.molang.binding.ResetVariable;
import org.openysm.molang.runtime.AssignableVariable;
import org.openysm.molang.runtime.ExecutionContext;
import org.openysm.molang.runtime.binding.ObjectBinding;
import it.unimi.dsi.fastutil.ints.Int2ReferenceOpenHashMap;
import org.jetbrains.annotations.NotNull;


@SuppressWarnings("MapOrSetKeyShouldOverrideHashCodeEquals")
public class ScopedVariableBinding implements ObjectBinding, ResetVariable {

    private final Int2ReferenceOpenHashMap<ScopedVariable> variableMap = new Int2ReferenceOpenHashMap<>();

    @Override
    public Object getProperty(String name) {
        return variableMap.computeIfAbsent(StringPool.computeIfAbsent(name), ScopedVariable::new);
    }

    @Override
    public void reset() {
        this.variableMap.clear();
    }

    private record ScopedVariable(int name) implements AssignableVariable {
        @Override
        @SuppressWarnings("unchecked")
        public Object evaluate(final @NotNull ExecutionContext<?> context) {
            return ((IContext<Object>) context.entity()).scopedStorage().getScoped(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public void assign(@NotNull ExecutionContext<?> context, Object value) {
            ((IContext<Object>) context.entity()).scopedStorage().setScoped(name, value);
        }
    }
}