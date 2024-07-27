package com.buuz135.salem.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.level.Level;

import java.util.function.Function;

public enum SalemRaidTier{

    COMMON(level -> {
        if (level.random.nextBoolean()){
            return new Zombie(level);
        } else {
            return new Skeleton(EntityType.SKELETON, level);
        }
    }, new TagKey[]{SalemTags.RAID_ADD_COMMON}, 2),
    RARE(level -> {
        if (level.random.nextDouble() > 2/3D){
            return new PiglinBrute(EntityType.PIGLIN_BRUTE, level);
        } else if (level.random.nextDouble() > 1/3D){
            return new Stray(EntityType.STRAY, level);
        }
        return new Husk(EntityType.HUSK, level);
    }, new TagKey[]{ SalemTags.RAID_ADD_RARE,SalemTags.RAID_ADD_COMMON}, 3),
    EPIC(level -> {
        if (level.random.nextDouble() > 2/3D){
            return new Ravager(EntityType.RAVAGER, level);
        } else if (level.random.nextDouble() > 1/3D){
            return new WitherSkeleton(EntityType.WITHER_SKELETON, level);
        }
        return new Breeze(EntityType.BREEZE, level);
    }, new TagKey[]{SalemTags.RAID_ADD_EPIC, SalemTags.RAID_ADD_COMMON, SalemTags.RAID_ADD_RARE}, 4),
    LEGENDARY(level -> {
        return new Cat(EntityType.CAT, level);
    }, new TagKey[]{SalemTags.RAID_ADD_LEGENDARY, SalemTags.RAID_ADD_COMMON, SalemTags.RAID_ADD_RARE, SalemTags.RAID_ADD_EPIC}, 8);

    private final Function<ServerLevel, Mob> bossSupplier;
    private final TagKey<EntityType<?>>[] spawns;
    private final int tier;

    SalemRaidTier(Function<ServerLevel, Mob> bossSupplier, TagKey<EntityType<?>>[] spawns, int tier) {
        this.bossSupplier = bossSupplier;
        this.spawns = spawns;
        this.tier = tier;
    }

    public EntityType<?> getRandomSpawn(Level level){
        if (level.random.nextBoolean() || this.spawns.length == 1){
            return level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE).getRandomElementOf(this.spawns[0], level.random).get().value();
        }
        return level.registryAccess().registryOrThrow(Registries.ENTITY_TYPE).getRandomElementOf(this.spawns[level.random.nextInt(this.spawns.length)], level.random).get().value();
    }

    public Function<ServerLevel, Mob> getBossSupplier() {
        return bossSupplier;
    }

    public TagKey<EntityType<?>>[] getSpawns() {
        return spawns;
    }

    public int getTier() {
        return tier;
    }
}
