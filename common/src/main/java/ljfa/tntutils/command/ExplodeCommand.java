package ljfa.tntutils.command;

import java.util.concurrent.CompletableFuture;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

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
        dispatcher.register(Commands.literal("explode")
                .requires(css -> css.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("pos", Vec3Argument.vec3())
                        .executes(ctx -> explode(
                                ctx.getSource(),
                                Vec3Argument.getVec3(ctx, "pos"),
                                DEFAULT_STRENGTH,
                                DEFAULT_FIRE)
                        )
                        .then(Commands.argument("strength", FloatArgumentType.floatArg(0.0f))
                                .suggests(ExplodeCommand::suggestStrength)
                                .executes(ctx -> explode(
                                        ctx.getSource(),
                                        Vec3Argument.getVec3(ctx, "pos"),
                                        FloatArgumentType.getFloat(ctx, "strength"),
                                        DEFAULT_FIRE)
                                )
                                .then(Commands.argument("fire", BoolArgumentType.bool())
                                        .executes(ctx -> explode(
                                                ctx.getSource(),
                                                Vec3Argument.getVec3(ctx, "pos"),
                                                FloatArgumentType.getFloat(ctx, "strength"),
                                                BoolArgumentType.getBool(ctx, "fire"))
                                        )
                                        .then(Commands.literal("block"  ).executes(ctx -> explode(ctx, ExplosionInteraction.BLOCK)))
                                        .then(Commands.literal("mob"    ).executes(ctx -> explode(ctx, ExplosionInteraction.MOB)))
                                        .then(Commands.literal("tnt"    ).executes(ctx -> explode(ctx, ExplosionInteraction.TNT)))
                                        .then(Commands.literal("trigger").executes(ctx -> explode(ctx, ExplosionInteraction.TRIGGER)))
                                        .then(Commands.literal("none"   ).executes(ctx -> explode(ctx, ExplosionInteraction.NONE)))
                                )
                        )
                )
        );
    }

    private static int explode(CommandSourceStack css, Vec3 pos, float strength, boolean fire) {
        css.getLevel().explode(css.getEntity(), pos.x, pos.y, pos.z, strength, fire, DEFAULT_INTERACTION);
        return Command.SINGLE_SUCCESS;
    }

    private static int explode(CommandContext<CommandSourceStack> ctx, ExplosionInteraction interaction) {
        var css = ctx.getSource();
        var pos = Vec3Argument.getVec3(ctx, "pos");
        var strength = FloatArgumentType.getFloat(ctx, "strength");
        var fire = BoolArgumentType.getBool(ctx, "fire");
        css.getLevel().explode(css.getEntity(), pos.x, pos.y, pos.z, strength, fire, interaction);
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> suggestStrength(CommandContext<CommandSourceStack> ctx, SuggestionsBuilder builder) {
        if(builder.getRemaining().isEmpty())
            return builder.suggest((int) DEFAULT_STRENGTH).buildFuture();
        else
            return Suggestions.empty();
    }
}
