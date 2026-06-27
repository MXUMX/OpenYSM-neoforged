package org.openysm.network.message;

import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import org.openysm.forge.capability.VehicleModelCapabilityProvider;
import org.openysm.client.compat.touhoulittlemaid.TouhouMaidCompat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record C2SCompleteFeedbackPacket(FeedbackData feedbackData) {

    public static void encode(C2SCompleteFeedbackPacket message, FriendlyByteBuf buf) {
        FeedbackData.writeToBuf(message.feedbackData, buf);
    }

    public static C2SCompleteFeedbackPacket decode(FriendlyByteBuf buf) {
        return new C2SCompleteFeedbackPacket(FeedbackData.readFromBuf(buf, false));
    }

    public static void handle(C2SCompleteFeedbackPacket message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        if (context.getDirection().getReceptionSide().isServer() && context.getSender() != null) {
            ServerPlayer sender = context.getSender();
            context.enqueueWork(() -> {
                handleOnServer(message, sender.serverLevel());
            });
        }
        context.setPacketHandled(true);
    }

    public static void handleOnServer(C2SCompleteFeedbackPacket message, ServerLevel serverLevel) {
        Entity entity = serverLevel.getEntity(message.feedbackData.flags());
        if (TouhouMaidCompat.isMaidEntity(entity)) {
            TouhouMaidCompat.applyFeedback(entity, message.feedbackData);
        } else if (entity instanceof ServerPlayer serverPlayer) {
            org.openysm.capability.YSMCapabilities.get(serverPlayer, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(cap -> {
                cap.applyFeedback(serverPlayer, message.feedbackData);
                if (serverPlayer.getVehicle() != null && serverPlayer.getVehicle().getFirstPassenger() == serverPlayer) {
                    org.openysm.capability.YSMCapabilities.get(serverPlayer.getVehicle(), VehicleModelCapabilityProvider.VEHICLE_MODEL_CAP).ifPresent(vehicleCap -> {
                        cap.getMolangVars().ifPresent(map -> vehicleCap.setModel(cap.getModelId(), map));
                    });
                }
            });
        }
    }
}
