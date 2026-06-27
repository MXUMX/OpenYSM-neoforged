package org.openysm.command.subcommands;

import org.openysm.model.ServerModelManager;
import org.openysm.event.CommandRegistry;
import org.openysm.forge.capability.AuthModelsCapabilityProvider;
import org.openysm.forge.capability.ModelInfoCapabilityProvider;
import org.openysm.model.format.ServerModelData;
import org.openysm.util.YSMMessageFormatter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;

import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ModelCommand {

    public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation().create();

    private static final String MODEL_NAME = "model";

    private static final String LITERAL_RELOAD = "reload";

    private static final String SET_NAME = "set";

    private static final String DISABLE_NAME = "disable";

    private static final String TARGETS_NAME = "targets";

    private static final String MODEL_ID_NAME = "model_id";

    private static final String TEXTURE_ID_NAME = "texture_id";

    private static final String IGNORE_AUTH_NAME = "ignore_auth";

    private static final String PLAYERS_NAME = "players";

    private static final String ARG_VALUE = "value";

    public static LiteralArgumentBuilder<CommandSourceStack> register() {
        LiteralArgumentBuilder<CommandSourceStack> model = Commands.literal(MODEL_NAME).requires(commandSourceStack -> YSMMessageFormatter.hasCommandPermission(commandSourceStack, 2));
        model.then(Commands.literal(LITERAL_RELOAD).executes(ModelCommand::reloadAllPack));
        model.then(Commands.literal(DISABLE_NAME).then(Commands.argument(PLAYERS_NAME, EntityArgument.players()).then(Commands.argument(ARG_VALUE, BoolArgumentType.bool()).executes(ModelCommand::disableModel))));
        LiteralArgumentBuilder<CommandSourceStack> set = Commands.literal(SET_NAME);
        RequiredArgumentBuilder<CommandSourceStack, EntitySelector> targets = Commands.argument(TARGETS_NAME, EntityArgument.players());
        RequiredArgumentBuilder<CommandSourceStack, String> modelId = Commands.argument(MODEL_ID_NAME, StringArgumentType.string()).suggests(CommandRegistry.MODEL_IDS);
        RequiredArgumentBuilder<CommandSourceStack, String> textureId = Commands.argument(TEXTURE_ID_NAME, StringArgumentType.string()).suggests(CommandRegistry.TEXTURE_IDS);
        RequiredArgumentBuilder<CommandSourceStack, Boolean> ignoreAuth = Commands.argument(IGNORE_AUTH_NAME, BoolArgumentType.bool());
        model.then(set.then(targets.then(modelId.then(textureId.executes(commandContext -> setModel(commandContext, false))))));
        model.then(set.then(targets.then(modelId.then(textureId.then(ignoreAuth.executes(ModelCommand::setModelIgnoreAuth))))));
        return model;
    }

    private static int setModelIgnoreAuth(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        return setModel(context, BoolArgumentType.getBool(context, IGNORE_AUTH_NAME));
    }

    public static int setModel(CommandContext<CommandSourceStack> context, boolean ignoreAuth) throws CommandSyntaxException {
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, TARGETS_NAME);
        String modelName = StringArgumentType.getString(context, MODEL_ID_NAME);
        String textureName = StringArgumentType.getString(context, TEXTURE_ID_NAME);
        ServerModelData info = ServerModelManager.getServerModelInfo().get(modelName);
        if (info == null) {
            context.getSource().sendSuccess(() -> Component.translatable("commands.openysm.export.not_exist", modelName), true);
            return Command.SINGLE_SUCCESS;
        }
        if (Objects.equals(textureName, "-")) {
            textureName = info.getLoadedModelData().getModelProperties().getDefaultTexture();
            if (StringUtils.isBlank(textureName) || !info.getModelInfo().getTextures().contains(textureName)) {
                textureName = info.getModelInfo().getTextures().get(0);
            }
        }
        if (ServerModelManager.getServerModelInfo().get(modelName).getModelInfo().getTextures().isEmpty()) {
            return Command.SINGLE_SUCCESS;
        }
        String finalTextureName = textureName;
        if (ignoreAuth) {
            targets.forEach(player -> org.openysm.capability.YSMCapabilities.get(player, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(cap -> {
                cap.setModelAndTexture(modelName, finalTextureName);
                cap.setMandatory(true);
                context.getSource().sendSuccess(() -> Component.translatable("message.openysm.model.set.success", modelName, player.getScoreboardName()), true);
            }));
            return Command.SINGLE_SUCCESS;
        }
        targets.forEach(player -> org.openysm.capability.YSMCapabilities.get(player, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(cap -> {
            org.openysm.capability.YSMCapabilities.get(player, AuthModelsCapabilityProvider.AUTH_MODELS_CAP).ifPresent(authCap -> {
                if (!ServerModelManager.getAuthModels().contains(modelName) || authCap.containsModel(modelName)) {
                    cap.setModelAndTexture(modelName, finalTextureName);
                    cap.setMandatory(true);
                    context.getSource().sendSuccess(() -> Component.translatable("message.openysm.model.set.success", modelName, player.getScoreboardName()), true);
                    return;
                }
                context.getSource().sendSuccess(() -> Component.translatable("message.openysm.model.set.need_auth", modelName, player.getScoreboardName()), true);
            });
        }));
        return Command.SINGLE_SUCCESS;
    }

    private static int reloadAllPack(CommandContext<CommandSourceStack> context) {
        StopWatch watch = StopWatch.createStarted();
        if (!ServerModelManager.loadModels(result -> {
            if (result.getErrorMessage() != null) {
                YSMMessageFormatter.sendServerMessage(context.getSource(), YSMMessageFormatter.withPrefix(result.getErrorMessage()), true);
            }
            if (result.isSuccess()) {
                YSMMessageFormatter.sendServerMessage(context.getSource(), Component.translatable("message.openysm.model.reload.complete", Double.valueOf(watch.getTime(TimeUnit.MICROSECONDS) / 1000.0d)), true);
                watch.reset();
                watch.start();
            }
        }, data -> {
            watch.stop();
            if (!data.isEnabled()) {
                YSMMessageFormatter.sendServerMessage(context.getSource(), YSMMessageFormatter.withPrefix(data.getDisplayComponent()), true);
                return;
            }
            if (!data.getUuidComponentMap().isEmpty()) {
                for (Component component : data.getUuidComponentMap().values()) {
                    YSMMessageFormatter.sendServerMessage(context.getSource(), YSMMessageFormatter.withPrefix(component), true);
                }
                if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                    YSMMessageFormatter.sendServerMessage(context.getSource(), Component.translatable("message.openysm.model.sync.complete", Double.valueOf(watch.getTime(TimeUnit.MICROSECONDS) / 1000.0d)), true);
                }
            }
        })) {
            context.getSource().sendFailure(Component.translatable("message.openysm.model.reload.in_progress"));
            return Command.SINGLE_SUCCESS;
        }
        context.getSource().sendSuccess(() -> Component.translatable("message.openysm.model.reload.start"), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int disableModel(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String str;
        Collection<ServerPlayer> targets = EntityArgument.getPlayers(context, PLAYERS_NAME);
        boolean bool = BoolArgumentType.getBool(context, ARG_VALUE);
        if (bool) {
            str = "message.openysm.model.disable.true";
        } else {
            str = "message.openysm.model.disable.false";
        }
        String str2 = str;
        targets.forEach(player -> org.openysm.capability.YSMCapabilities.get(player, ModelInfoCapabilityProvider.MODEL_INFO_CAP).ifPresent(cap -> {
            cap.setDisabled(bool);
            context.getSource().sendSuccess(() -> Component.translatable(str2, player.getScoreboardName()), true);
        }));
        return Command.SINGLE_SUCCESS;
    }
}