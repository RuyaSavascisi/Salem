package com.buuz135.salem.command;

import com.buuz135.salem.util.SalemRaidTier;
import com.buuz135.salem.world.SalemRaidSavedData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

public class SalemRaidCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralCommandNode<CommandSourceStack> literalCommandNode = commandDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("salemraid").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                })
                .then(Commands.argument("tier", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(SalemRaidTier.values()).map(tier -> tier.name()).toList(), builder))
                .then(Commands.argument("location", Vec3Argument.vec3()).executes((commandContext) -> {
                    return raidPos((CommandSourceStack) commandContext.getSource(), ((CommandSourceStack) commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(commandContext, "location"), WorldCoordinates.current(), SalemRaidTier.valueOf(StringArgumentType.getString(commandContext, "tier")));
                })).then(Commands.argument("destination", EntityArgument.entity()).executes((commandContext) -> {
                    return raidEntity((CommandSourceStack) commandContext.getSource(), EntityArgument.getEntity(commandContext, "destination"), SalemRaidTier.valueOf(StringArgumentType.getString(commandContext, "tier")));
                }))))))
        );
    }

    private static int raidEntity(CommandSourceStack commandSourceStack, Entity entity, SalemRaidTier tier) throws CommandSyntaxException {

        performRaid(commandSourceStack, (ServerLevel) entity.level(), entity.getX(), entity.getY(), entity.getZ(), tier);

        return 1;
    }

    private static int raidPos(CommandSourceStack commandSourceStack, ServerLevel serverLevel, Coordinates coordinates, @Nullable Coordinates coordinates2, SalemRaidTier tier) throws CommandSyntaxException {
        Vec3 vec3 = coordinates.getPosition(commandSourceStack);
        Vec2 vec2 = coordinates2 == null ? null : coordinates2.getRotation(commandSourceStack);
        Set<RelativeMovement> set = EnumSet.noneOf(RelativeMovement.class);
        if (coordinates.isXRelative()) {
            set.add(RelativeMovement.X);
        }

        if (coordinates.isYRelative()) {
            set.add(RelativeMovement.Y);
        }

        if (coordinates.isZRelative()) {
            set.add(RelativeMovement.Z);
        }

        if (coordinates2 == null) {
            set.add(RelativeMovement.X_ROT);
            set.add(RelativeMovement.Y_ROT);
        } else {
            if (coordinates2.isXRelative()) {
                set.add(RelativeMovement.X_ROT);
            }

            if (coordinates2.isYRelative()) {
                set.add(RelativeMovement.Y_ROT);
            }
        }

        performRaid(commandSourceStack, serverLevel, vec3.x, vec3.y, vec3.z, tier);

        return 1;
    }

    private static void performRaid(CommandSourceStack commandSourceStack, ServerLevel serverLevel, double d, double e, double f, SalemRaidTier tier) throws CommandSyntaxException {
        SalemRaidSavedData.getData(serverLevel).startRaid(new BlockPos((int) d, (int) e, (int) f), tier);
    }
}
