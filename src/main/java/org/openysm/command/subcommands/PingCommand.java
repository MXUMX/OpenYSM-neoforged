package org.openysm.command.subcommands;

import org.openysm.OpenYSM;
import org.openysm.network.NetworkHandler;
import org.openysm.network.message.S2CVersionCheckPacket;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

public class PingCommand {

    private static final String PING_NAME = "ping";

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        return Commands.literal(PING_NAME).executes(PingCommand::executePing);
    }

    private static int executePing(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer playerOrException = context.getSource().getPlayerOrException();
        playerOrException.sendSystemMessage(Component.translatable("message.openysm.client.ping_result", ModList.get().getModFileById(OpenYSM.MOD_ID).versionString()));
        if (!NetworkHandler.isPlayerConnected(playerOrException)) {
            NetworkHandler.sendToClientPlayer(new S2CVersionCheckPacket(), playerOrException);
            return Command.SINGLE_SUCCESS;
        }
        return Command.SINGLE_SUCCESS;
    }
}