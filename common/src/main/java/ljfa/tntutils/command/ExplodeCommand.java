package ljfa.tntutils.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;

import ljfa.tntutils.TNTUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.phys.Vec3;

public class ExplodeCommand {
	private static final float DEFAULT_STRENGTH = 4.0f;
	private static final boolean DEFAULT_FIRE = false;
	private static final ExplosionInteraction DEFAULT_INTERACTION = ExplosionInteraction.BLOCK;

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		if(TNTUtils.config().addExplodeCommand()) {
			dispatcher.register(Commands.literal("explode")
					.requires(css -> css.hasPermission(Commands.LEVEL_GAMEMASTERS))
					.then(Commands.argument("pos", Vec3Argument.vec3())
							.executes(ctx -> explode(
									ctx.getSource(),
									Vec3Argument.getVec3(ctx, "pos"),
									DEFAULT_STRENGTH,
									DEFAULT_FIRE,
									DEFAULT_INTERACTION)
							)
							.then(Commands.argument("strength", FloatArgumentType.floatArg(0.0f))
									.executes(ctx -> explode(
											ctx.getSource(),
											Vec3Argument.getVec3(ctx, "pos"),
											FloatArgumentType.getFloat(ctx, "strength"),
											DEFAULT_FIRE,
											DEFAULT_INTERACTION)
									)
									.then(Commands.argument("fire", BoolArgumentType.bool())
											.executes(ctx -> explode(
													ctx.getSource(),
													Vec3Argument.getVec3(ctx, "pos"),
													FloatArgumentType.getFloat(ctx, "strength"),
													BoolArgumentType.getBool(ctx, "fire"),
													DEFAULT_INTERACTION)
											)
											.then(Commands.argument("block_interaction", new ExplosionInteractionArgument())
													.executes(ctx -> explode(
															ctx.getSource(),
															Vec3Argument.getVec3(ctx, "pos"),
															FloatArgumentType.getFloat(ctx, "strength"),
															BoolArgumentType.getBool(ctx, "fire"),
															ExplosionInteractionArgument.getInteraction(ctx, "block_interaction"))
													)
											)
									)
							)
					)
			);
		}
	}

	private static int explode(CommandSourceStack css, Vec3 pos, float strength, boolean fire, ExplosionInteraction interaction) {
		css.getLevel().explode(css.getEntity(), pos.x, pos.y, pos.z, strength, fire, interaction);
		return Command.SINGLE_SUCCESS;
	}
}
