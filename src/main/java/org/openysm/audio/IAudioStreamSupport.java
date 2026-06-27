package org.openysm.audio;

import net.minecraft.client.sounds.AudioStream;

public interface IAudioStreamSupport extends AudioStream {
    boolean isClosed();
}