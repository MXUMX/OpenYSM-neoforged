package org.openysm.client.animation.molang;

import org.openysm.client.compat.touhoulittlemaid.TouhouLittleMaidCompat;
import org.openysm.geckolib3.core.molang.binding.ContextBinding;
import org.openysm.util.data.LazySupplier;

public class TLMBinding extends ContextBinding {

    public static final LazySupplier<TLMBinding> INSTANCE = new LazySupplier<>(TLMBinding::new);

    public TLMBinding() {
        TouhouLittleMaidCompat.registerMaidAnimStates(this);
    }
}