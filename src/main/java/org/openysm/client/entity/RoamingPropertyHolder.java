package org.openysm.client.entity;

import org.openysm.molang.runtime.Struct;
import org.jetbrains.annotations.Nullable;

public interface RoamingPropertyHolder {
    @Nullable
    Struct getPropertyContainer();
}