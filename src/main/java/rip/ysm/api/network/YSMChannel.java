/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  dev.architectury.injectables.annotations.ExpectPlatform
 *  dev.architectury.injectables.annotations.ExpectPlatform$Transformed
 *  net.minecraft.network.FriendlyByteBuf
 *  net.minecraft.network.protocol.Packet
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 */
package rip.ysm.api.network;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import rip.ysm.api.network.PacketContext;
import rip.ysm.api.network.PacketDirection;
import rip.ysm.api.network.forge.YSMChannelImpl;

public final class YSMChannel {
    private YSMChannel() {
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void init(ResourceLocation channelId, String version) {
        YSMChannelImpl.init(channelId, version);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static <T> void register(int discriminator, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, PacketContext> handler, PacketDirection direction) {
        YSMChannelImpl.register(discriminator, type, encoder, decoder, handler, direction);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void sendToServer(Object packet) {
        YSMChannelImpl.sendToServer(packet);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void sendToClientPlayer(Object packet, ServerPlayer player) {
        YSMChannelImpl.sendToClientPlayer(packet, player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void sendToAll(Object packet) {
        YSMChannelImpl.sendToAll(packet);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void sendToTrackingEntity(Object packet, Entity entity) {
        YSMChannelImpl.sendToTrackingEntity(packet, entity);
    }

    /*
     * WARNING - void declaration
     */
    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static void sendToTrackingEntityAndSelf(Object packet, Player player) {
        YSMChannelImpl.sendToTrackingEntityAndSelf(packet, player);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Packet<?> toClientboundPacket(Object packet) {
        return YSMChannelImpl.toClientboundPacket(packet);
    }

    @ExpectPlatform
    @ExpectPlatform.Transformed
    public static Packet<?> toServerboundPacket(Object packet) {
        return YSMChannelImpl.toServerboundPacket(packet);
    }
}
