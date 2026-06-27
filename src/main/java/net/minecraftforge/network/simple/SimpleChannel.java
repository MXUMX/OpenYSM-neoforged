package net.minecraftforge.network.simple;

import io.netty.buffer.Unpooled;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;

public final class SimpleChannel {
    private final ResourceLocation name;
    private final String version;
    private final Map<Integer, MessageRegistration<?>> registrations = new HashMap<>();
    private CustomPacketPayload.Type<LegacyPayload> payloadType;

    public SimpleChannel(ResourceLocation name, String version) {
        this.name = name;
        this.version = version;
    }

    public <T> void registerMessage(int discriminator, Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler, Optional<NetworkDirection> direction) {
        this.registrations.put(discriminator, new MessageRegistration<>(type, encoder, decoder, handler, direction.orElse(null)));
    }

    public void registerPayload(RegisterPayloadHandlersEvent event) {
        this.payloadType = new CustomPacketPayload.Type<>(this.name);
        StreamCodec<RegistryFriendlyByteBuf, LegacyPayload> codec = StreamCodec.of((buf, payload) -> payload.write(buf), buf -> LegacyPayload.read(this.payloadType, buf));
        PayloadRegistrar registrar = event.registrar(this.version).optional();
        registrar.playBidirectional(this.payloadType, codec, this::handlePayload);
    }

    public void sendToServer(Object message) {
        PacketDistributor.sendToServer(this.toPayload(message));
    }

    public void send(net.minecraftforge.network.PacketDistributor.PacketTarget target, Object message) {
        LegacyPayload payload = this.toPayload(message);
        switch (target.target()) {
            case PLAYER -> PacketDistributor.sendToPlayer(target.player(), payload);
            case ALL -> PacketDistributor.sendToAllPlayers(payload);
            case TRACKING_ENTITY -> PacketDistributor.sendToPlayersTrackingEntity(target.entity(), payload);
            case TRACKING_ENTITY_AND_SELF -> PacketDistributor.sendToPlayersTrackingEntityAndSelf(target.entity(), payload);
        }
    }

    public void reply(Object message, NetworkEvent.Context context) {
        context.reply(this.toPayload(message));
    }

    public Packet<?> toClientboundPacket(Object message) {
        return new ClientboundCustomPayloadPacket(this.toPayload(message));
    }

    public Packet<?> toServerboundPacket(Object message) {
        return new ServerboundCustomPayloadPacket(this.toPayload(message));
    }

    public Packet<?> toVanillaPacket(Object message, NetworkDirection direction) {
        return direction == NetworkDirection.PLAY_TO_SERVER ? this.toServerboundPacket(message) : this.toClientboundPacket(message);
    }

    private LegacyPayload toPayload(Object message) {
        for (Map.Entry<Integer, MessageRegistration<?>> entry : this.registrations.entrySet()) {
            MessageRegistration<?> registration = entry.getValue();
            if (registration.type.isInstance(message)) {
                FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
                registration.encodeUnchecked(message, buf);
                byte[] data = new byte[buf.readableBytes()];
                buf.readBytes(data);
                return new LegacyPayload(this.payloadType, entry.getKey(), data);
            }
        }
        throw new IllegalArgumentException("Unregistered packet type: " + message.getClass().getName());
    }

    private void handlePayload(LegacyPayload payload, IPayloadContext context) {
        MessageRegistration<?> registration = this.registrations.get(payload.discriminator);
        if (registration == null) {
            return;
        }
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(payload.data));
        Object message = registration.decoder.apply(buf);
        NetworkEvent.Context forgeContext = new NetworkEvent.Context(context);
        registration.handleUnchecked(message, () -> forgeContext);
    }

    private record LegacyPayload(CustomPacketPayload.Type<LegacyPayload> type, int discriminator, byte[] data) implements CustomPacketPayload {
        private static LegacyPayload read(CustomPacketPayload.Type<LegacyPayload> type, FriendlyByteBuf buf) {
            int discriminator = buf.readVarInt();
            byte[] data = buf.readByteArray();
            return new LegacyPayload(type, discriminator, data);
        }

        private void write(FriendlyByteBuf buf) {
            buf.writeVarInt(this.discriminator);
            buf.writeByteArray(this.data);
        }
    }

    private record MessageRegistration<T>(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        @SuppressWarnings("unchecked")
        private void encodeUnchecked(Object message, FriendlyByteBuf buf) {
            this.encoder.accept((T) message, buf);
        }

        @SuppressWarnings("unchecked")
        private void handleUnchecked(Object message, Supplier<NetworkEvent.Context> context) {
            this.handler.accept((T) message, context);
        }
    }
}
