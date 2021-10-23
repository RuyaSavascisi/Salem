package com.buuz135.salem.command;

import com.buuz135.salem.world.SalemRaidSavedData;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Coordinates;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class SalemRaidCommand {

    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        LiteralCommandNode<CommandSourceStack> literalCommandNode = commandDispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("salemraid").requires((commandSourceStack) -> {
                    return commandSourceStack.hasPermission(2);
                })
                .then(Commands.argument("tier", StringArgumentType.word()).suggests((context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(SalemRaidSavedData.SalemRaidTier.values()).map(tier -> tier.name()).toList(), builder))
                .then(Commands.argument("location", Vec3Argument.vec3()).executes((commandContext) -> {
                    return raidPos((CommandSourceStack) commandContext.getSource(), ((CommandSourceStack) commandContext.getSource()).getLevel(), Vec3Argument.getCoordinates(commandContext, "location"), WorldCoordinates.current(), SalemRaidSavedData.SalemRaidTier.valueOf(StringArgumentType.getString(commandContext, "tier")));
                })).then(Commands.argument("destination", EntityArgument.entity()).executes((commandContext) -> {
                    return raidEntity((CommandSourceStack) commandContext.getSource(), EntityArgument.getEntity(commandContext, "destination"), SalemRaidSavedData.SalemRaidTier.valueOf(StringArgumentType.getString(commandContext, "tier")));
                }))))))
        );
    }

    private static int raidEntity(CommandSourceStack commandSourceStack, Entity entity, SalemRaidSavedData.SalemRaidTier tier) throws CommandSyntaxException {

        perfomrRaid(commandSourceStack, (ServerLevel) entity.level, entity.getX(), entity.getY(), entity.getZ(), tier);

        return 1;
    }

    private static int raidPos(CommandSourceStack commandSourceStack, ServerLevel serverLevel, Coordinates coordinates, @Nullable Coordinates coordinates2, SalemRaidSavedData.SalemRaidTier tier) throws CommandSyntaxException {
        Vec3 vec3 = coordinates.getPosition(commandSourceStack);
        Vec2 vec2 = coordinates2 == null ? null : coordinates2.getRotation(commandSourceStack);
        Set<ClientboundPlayerPositionPacket.RelativeArgument> set = EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class);
        if (coordinates.isXRelative()) {
            set.add(ClientboundPlayerPositionPacket.RelativeArgument.X);
        }

        if (coordinates.isYRelative()) {
            set.add(ClientboundPlayerPositionPacket.RelativeArgument.Y);
        }

        if (coordinates.isZRelative()) {
            set.add(ClientboundPlayerPositionPacket.RelativeArgument.Z);
        }

        if (coordinates2 == null) {
            set.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
            set.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
        } else {
            if (coordinates2.isXRelative()) {
                set.add(ClientboundPlayerPositionPacket.RelativeArgument.X_ROT);
            }

            if (coordinates2.isYRelative()) {
                set.add(ClientboundPlayerPositionPacket.RelativeArgument.Y_ROT);
            }
        }

        perfomrRaid(commandSourceStack, serverLevel, vec3.x, vec3.y, vec3.z, tier);

        return 1;
    }

    private static void perfomrRaid(CommandSourceStack commandSourceStack, ServerLevel serverLevel, double d, double e, double f, SalemRaidSavedData.SalemRaidTier tier) throws CommandSyntaxException {
        SalemRaidSavedData.getData(serverLevel).startRaid(new BlockPos(d, e, f), tier);
    }
}
