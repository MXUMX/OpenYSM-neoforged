package org.openysm.audio;

public interface IAudioPlayer {
    void release();

    boolean isStopped();
}