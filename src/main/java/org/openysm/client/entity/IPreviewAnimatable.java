package org.openysm.client.entity;

import org.openysm.client.animation.AnimationTracker;
import org.jetbrains.annotations.NotNull;

public interface IPreviewAnimatable {
    @NotNull
    AnimationTracker getAnimationStateMachine();

    void setCustomAnimationActive(boolean active);
}