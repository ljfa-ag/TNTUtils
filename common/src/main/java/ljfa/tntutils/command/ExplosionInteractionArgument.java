package ljfa.tntutils.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import ljfa.tntutils.TNTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable.EnumCodec;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class ExplosionInteractionArgument implements ArgumentType<ExplosionInteraction> {
	public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(TNTUtils.MOD_ID, "explosion_interaction");

	private static final Collection<String> VALUES = Arrays.stream(ExplosionInteraction.values())
			.map(ExplosionInteraction::getSerializedName)
			.toList();

	private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(
			object -> Component.translatableEscape("tntutils.argument.interaction.invalid", object));

	@Override
	public ExplosionInteraction parse(StringReader reader) throws CommandSyntaxException {
		var string = reader.readUnquotedString();
		@SuppressWarnings("deprecation")
		var value = ((EnumCodec<ExplosionInteraction>) ExplosionInteraction.CODEC).byName(string);
		if(value == null)
			throw ERROR_INVALID.createWithContext(reader, string);
		else
			return value;
	}

	@Override
	public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
		return SharedSuggestionProvider.suggest(VALUES, builder);
	}

	@Override
	public Collection<String> getExamples() {
		return VALUES;
	}

	public static ExplosionInteraction getInteraction(CommandContext<CommandSourceStack> context, String name) throws CommandSyntaxException {
		return context.getArgument(name, ExplosionInteraction.class);
	}
}
