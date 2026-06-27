package org.openysm.event;

import org.openysm.client.ClientModelManager;
import org.openysm.command.OpenYSMClientCommand;
import org.openysm.model.ServerModelManager;
import org.openysm.OpenYSM;
import org.openysm.command.RootClientCommand;
import org.openysm.command.RootCommand;
import org.openysm.geckolib3.core.builder.Animation;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
public final class CommandRegistry {

    public static final SuggestionProvider<CommandSourceStack> MODEL_IDS = SuggestionProviders.register(ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "models"), (commandContext, suggestionsBuilder) -> {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                return SharedSuggestionProvider.suggest(ServerModelManager.getServerModelInfo().keySet().stream().map(CommandRegistry::escapeIfRequired).toList(), suggestionsBuilder);
            }
            return SharedSuggestionProvider.suggest(ClientModelManager.getModelAssemblyMap().keySet().stream().map(CommandRegistry::escapeIfRequired).toList(), suggestionsBuilder);
        }
        return Suggestions.empty();
    });

    public static final SuggestionProvider<CommandSourceStack> ANIMATION_NAMES = SuggestionProviders.register(ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "animations"), (commandContext, suggestionsBuilder) -> {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                return Suggestions.empty();
            }
            Object2ReferenceMap<String, Animation> map = ClientModelManager.getLocalModelContext().getAnimationBundle().getMainAnimations();
            HashSet<String> set = Sets.newHashSet();
            set.addAll(map.keySet().stream().map(CommandRegistry::escapeIfRequired).toList());
            set.add("stop");
            return SharedSuggestionProvider.suggest(set, suggestionsBuilder);
        }
        return Suggestions.empty();
    });

    public static final SuggestionProvider<CommandSourceStack> TEXTURE_IDS = SuggestionProviders.register(ResourceLocation.fromNamespaceAndPath(OpenYSM.MOD_ID, "textures"), (commandContext, suggestionsBuilder) -> {
        if (commandContext.getSource() instanceof SharedSuggestionProvider) {
            String str = commandContext.getArgument("model_id", String.class);
            if (FMLEnvironment.dist == Dist.DEDICATED_SERVER) {
                if (ServerModelManager.getServerModelInfo().containsKey(str)) {
                    List<String> list = ServerModelManager.getServerModelInfo().get(str).getModelInfo().getTextures().stream().map(CommandRegistry::escapeIfRequired).collect(Collectors.toList());
                    list.add(0, "-");
                    return SharedSuggestionProvider.suggest(list, suggestionsBuilder);
                }
            } else if (ClientModelManager.getModelAssemblyMap().containsKey(str)) {
                List<String> list2 = ClientModelManager.getModelContext(str).map(context -> context.getAnimationBundle().getTextures().getKeys().stream().map(CommandRegistry::escapeIfRequired).collect(Collectors.toList())).orElseGet(Lists::newArrayList);
                list2.add(0, "-");
                return SharedSuggestionProvider.suggest(list2, suggestionsBuilder);
            }
        }
        return Suggestions.empty();
    });

    @SubscribeEvent
    public static void onRegisterCommand(RegisterCommandsEvent event) {
        if (!OpenYSM.isAvailable()) {
            RootCommand.registerFallbackCommands(event.getDispatcher());
            return;
        }
        OpenYSMClientCommand.registerClientCommands(event.getDispatcher());
        RootCommand.registerCommands(event.getDispatcher());
        if (FMLEnvironment.dist == Dist.CLIENT) {
            RootClientCommand.registerClientCommands(event.getDispatcher());
        }
    }

    @SubscribeEvent
    public static void onRegisterCommand(RegisterClientCommandsEvent event) {
        OpenYSMClientCommand.registerClientCommands(event.getDispatcher());
    }

    private static String escapeIfRequired(String str) {
        if (str.chars().allMatch(i -> StringReader.isAllowedInUnquotedString((char) i))) {
            return str;
        }
        return String.format("\"%s\"", str.replace("\"", "\\\"").replace("'", "\\'"));
    }
}