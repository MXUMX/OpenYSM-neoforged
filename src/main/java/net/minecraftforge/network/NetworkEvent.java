package net.minecraftforge.network;

import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public final class NetworkEvent {
    private NetworkEvent() {
    }

    public static final class Context {
        private final IPayloadContext context;

        public Context(IPayloadContext context) {
            this.context = context;
        }

        public CompletableFuture<Void> enqueueWork(Runnable runnable) {
            return this.context.enqueueWork(runnable);
        }

        public ServerPlayer getSender() {
            return this.context.player() instanceof ServerPlayer serverPlayer ? serverPlayer : null;
        }

        public void setPacketHandled(boolean handled) {
        }

        public Connection getNetworkManager() {
            return this.context.connection();
        }

        public void reply(CustomPacketPayload payload) {
            this.context.reply(payload);
        }

        public NetworkDirection getDirection() {
            return this.context.flow() == PacketFlow.CLIENTBOUND ? NetworkDirection.PLAY_TO_CLIENT : NetworkDirection.PLAY_TO_SERVER;
        }
    }
}
