package net.minecraftforge.network;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.simple.SimpleChannel;

public final class NetworkRegistry {
    private static final List<SimpleChannel> CHANNELS = new ArrayList<>();

    private NetworkRegistry() {
    }

    public static SimpleChannel newSimpleChannel(ResourceLocation name, Supplier<String> version, Predicate<String> clientAcceptedVersions, Predicate<String> serverAcceptedVersions) {
        SimpleChannel channel = new SimpleChannel(name, version.get());
        CHANNELS.add(channel);
        return channel;
    }

    public static List<SimpleChannel> channels() {
        return CHANNELS;
    }
}
