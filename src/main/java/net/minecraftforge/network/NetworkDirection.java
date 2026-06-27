package net.minecraftforge.network;

public enum NetworkDirection {
    PLAY_TO_CLIENT,
    PLAY_TO_SERVER;

    public ReceptionSide getReceptionSide() {
        return this == PLAY_TO_CLIENT ? ReceptionSide.CLIENT : ReceptionSide.SERVER;
    }

    public enum ReceptionSide {
        CLIENT,
        SERVER;

        public boolean isClient() {
            return this == CLIENT;
        }

        public boolean isServer() {
            return this == SERVER;
        }
    }
}
