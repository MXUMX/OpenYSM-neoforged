package org.openysm.util.accessors;

public interface ProjectileStateAccessor {
    boolean isInGround();

    int getInGroundTime();

    String getOwnerItemId();
}