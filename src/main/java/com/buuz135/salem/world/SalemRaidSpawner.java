package com.buuz135.salem.world;

import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.Arrays;
import java.util.List;

public class SalemRaidSpawner {

    private int ticksUntilSpawn = 0;

    public void tick(ServerLevel serverLevel) {
        var random = serverLevel.random;
        --this.ticksUntilSpawn;
        if (ticksUntilSpawn <= 0){
            this.ticksUntilSpawn += (60 + random.nextInt(60)) * 20;
            if (!(serverLevel.getSkyDarken() < 5 && serverLevel.dimensionType().hasSkyLight())) {
                for(Player playerentity : serverLevel.getPlayers(serverPlayer -> true)) {
                    if (!playerentity.isSpectator()) {
                        BlockPos blockpos = playerentity.blockPosition();
                        if (!serverLevel.dimensionType().hasSkyLight() || blockpos.getY() >= serverLevel.getSeaLevel() && serverLevel.canSeeSky(blockpos)) {
                            DifficultyInstance difficultyinstance = serverLevel.getCurrentDifficultyAt(blockpos);
                            if (difficultyinstance.isHarderThan(random.nextFloat() * 3.0F)) {
                                ServerStatsCounter serverstatisticsmanager = ((ServerPlayer)playerentity).getStats();
                                int j = Mth.clamp(serverstatisticsmanager.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
                                if (random.nextInt(j) >= 72000) {
                                    BlockPos blockpos1 = blockpos.east(-40 + random.nextInt(81)).south(-40 + random.nextInt(81));
                                    while (blockpos.closerThan(blockpos1, 26)){
                                        blockpos1 = blockpos.east(-30 + random.nextInt(61)).south(-30 + random.nextInt(61));
                                    }
                                    BlockState blockstate = serverLevel.getBlockState(blockpos1);
                                    FluidState fluidstate = serverLevel.getFluidState(blockpos1);
                                    if (NaturalSpawner.isValidEmptySpawnBlock(serverLevel, blockpos1, blockstate, fluidstate, EntityType.ZOMBIE)) {
                                        List<SalemRaidTier> tiers = Arrays.asList(SalemRaidTier.values()).subList(0, Math.min((int) (difficultyinstance.getEffectiveDifficulty() + 1.5), SalemRaidTier.values().length));
                                        if (tiers.size() > 0){
                                            SalemRaidSavedData.getData(serverLevel).startRaid(blockpos1, tiers.get(random.nextInt(tiers.size())));
                                        } else {
                                            SalemRaidSavedData.getData(serverLevel).startRaid(blockpos1, SalemRaidTier.COMMON);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}