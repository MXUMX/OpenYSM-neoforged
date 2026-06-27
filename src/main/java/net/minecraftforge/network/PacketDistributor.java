package net.minecraftforge.network;

import java.util.function.Supplier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

public final class PacketDistributor {
    public static final PacketDistributor PLAYER = new PacketDistributor(Target.PLAYER);
    public static final PacketDistributor ALL = new PacketDistributor(Target.ALL);
    public static final PacketDistributor TRACKING_ENTITY = new PacketDistributor(Target.TRACKING_ENTITY);
    public static final PacketDistributor TRACKING_ENTITY_AND_SELF = new PacketDistributor(Target.TRACKING_ENTITY_AND_SELF);

    private final Target target;

    private PacketDistributor(Target target) {
        this.target = target;
    }

    public PacketTarget noArg() {
        return new PacketTarget(this.target, null);
    }

    public PacketTarget with(Supplier<?> supplier) {
        return new PacketTarget(this.target, supplier == null ? null : supplier.get());
    }

    public record PacketTarget(Target target, Object value) {
        public ServerPlayer player() {
            return (ServerPlayer) this.value;
        }

        public Entity entity() {
            return (Entity) this.value;
        }
    }

    public enum Target {
        PLAYER,
        ALL,
        TRACKING_ENTITY,
        TRACKING_ENTITY_AND_SELF
    }
}
