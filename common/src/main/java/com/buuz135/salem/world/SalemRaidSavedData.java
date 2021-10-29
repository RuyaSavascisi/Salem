package com.buuz135.salem.world;

import com.buuz135.salem.SalemMod;
import com.buuz135.salem.item.TrinketItem;
import com.buuz135.salem.util.BlockUtil;
import com.google.common.collect.Sets;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.Tag;
import net.minecraft.world.BossEvent;
import net.minecraft.world.Containers;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.scores.PlayerTeam;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public class SalemRaidSavedData extends SavedData {

    //TODO Raid Equipment
    //TODO Config to disable natural raid spawns

    private static String ID = "SalemRaids";

    private final ServerLevel level;
    private HashMap<UUID, SalemRaid> raids = new HashMap<>();

    public SalemRaidSavedData(ServerLevel level) {
        this.level = level;
    }

    public static SalemRaidSavedData getData(ServerLevel level){
        return level.getDataStorage().computeIfAbsent(compoundTag -> load(level, compoundTag), () -> new SalemRaidSavedData(level), ID);
    }

    private static SalemRaidSavedData load(ServerLevel level, CompoundTag compoundTag){
        SalemRaidSavedData salemRaidSavedData = new SalemRaidSavedData(level);
        for (String raids : compoundTag.getCompound("Raids").getAllKeys()) {
            SalemRaid raid = new SalemRaid(UUID.fromString(raids), SalemRaidTier.COMMON);
            raid.load(level, compoundTag.getCompound("Raids").getCompound(raids));
            salemRaidSavedData.raids.put(UUID.fromString(raids), raid);
        }
        return salemRaidSavedData;
    }

    public void startRaid(BlockPos pos, SalemRaidTier tier){
        SalemRaid raid = new SalemRaid(UUID.randomUUID(), tier);
        raid.createBoss(this.level, pos);
        for (int i = 0; i < tier.tier; i++) {
            int finalI = i;
            new Thread(() -> {
                try {
                    Thread.sleep(((long) 1000 / 20) * 10 * finalI);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.level.getServer().execute(() -> {
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT, this.level);
                    lightningBolt.setVisualOnly(true);
                    BlockPos r = BlockUtil.getRandomSurfaceNearby(this.level, pos, 20);
                    lightningBolt.setPos(r.getX(), r.getY(), r.getZ());
                    this.level.addFreshEntity(lightningBolt);
                });
            }).start();
        }
        raids.put(raid.uuid, raid);
        this.setDirty();
    }

    public void tick(){
        new ArrayList<>(raids.values()).forEach(salemRaid -> {
            if (!salemRaid.active){
                raids.remove(salemRaid.uuid);
                this.setDirty();
            }
        });
        raids.values().forEach(salemRaid -> salemRaid.tick(level,this));
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        CompoundTag raidsCompound = new CompoundTag();
        raids.forEach((uuid, salemRaid) -> {
            CompoundTag tag = new CompoundTag();
            salemRaid.save(tag);
            raidsCompound.put(uuid.toString(), tag);
        });
        compoundTag.put("Raids", raidsCompound);
        return compoundTag;
    }

    private static class SalemRaid  {

        public static int PLEB_TIMER_MAX = 200;
        private static final Component RAID_NAME_COMPONENT = new TranslatableComponent("event.salem.raid.spawn");
        private static final Component RAID_NAME_COMPONENT_CLEAR = new TranslatableComponent("event.salem.raid.clear");
        private static final Component RAID_NAME_COMPONENT_ATTACK = new TranslatableComponent("event.salem.raid.damage");


        private UUID uuid;
        private Mob boss;
        private List<LivingEntity> plebs;
        private SalemRaidTier salemRaidTier;
        private int plebSpawningTimer;
        private final ServerBossEvent raidEvent;
        private boolean active;
        private int maxPlebs = 1;
        private CompoundTag originalNBT;
        private boolean hasRemoved;

        public SalemRaid(UUID uuid, SalemRaidTier salemRaidTier) {
            this.active = true;
            this.uuid = uuid;
            this.plebs = new ArrayList<>();
            this.salemRaidTier = salemRaidTier;
            this.plebSpawningTimer = 100;
            this.raidEvent = new ServerBossEvent(RAID_NAME_COMPONENT, BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
            this.hasRemoved = false;
        }

        public Entity createBoss(ServerLevel level, BlockPos pos){
            boss = salemRaidTier.bossSupplier.apply(level);
            boss.addEffect(new MobEffectInstance(SalemMod.SALEM_SPAWN.get(), Integer.MAX_VALUE, 0, true, false));
            boss.addEffect(new MobEffectInstance(SalemMod.ENLARGE_SPAWN.get(), Integer.MAX_VALUE, 6 + salemRaidTier.tier, true, false));
            boss.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, Integer.MAX_VALUE, 20 + salemRaidTier.tier * 2, true, false));
            boss.setHealth(boss.getMaxHealth());
            boss.setPos(pos.getX(), pos.getY() - 4, pos.getZ());
            if (boss instanceof AbstractPiglin){
                ((AbstractPiglin) boss).setImmuneToZombification(true);
            }
            boss.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.MOB_SUMMONED, null, null);
            PlayerTeam team =  level.getScoreboard().addPlayerTeam(boss.getStringUUID().substring(0, 15));
            level.getScoreboard().addPlayerToTeam(boss.getStringUUID(), team);
            level.addFreshEntity(boss);
            return boss;
        }

        public void tick(ServerLevel level, SalemRaidSavedData salemRaidSavedData){
            if (this.boss == null){
                this.boss = (Mob) level.getEntity(originalNBT.getUUID("Boss"));
                this.plebs = new ArrayList<>();
                for (String allKey : originalNBT.getCompound("Plebs").getAllKeys()) {
                    Entity entity = level.getEntity(UUID.fromString(allKey));
                    if (entity instanceof LivingEntity){
                        plebs.add((LivingEntity) entity);
                    }
                }
                this.maxPlebs = this.plebs.size();
                return;
            }
            if (this.boss.level.getGameTime() % 20 == 0){
                updatePlayers();
                updateProgress();
                if (this.boss.level.isDay()){
                    this.boss.remove(Entity.RemovalReason.DISCARDED);
                    this.hasRemoved = true;
                }
                salemRaidSavedData.setDirty();
            }
            if (!this.boss.isAlive()){
                if (!hasRemoved){
                    ItemStack stack = new ItemStack(TrinketItem.TRINKETS.get(this.salemRaidTier).get(level.random.nextInt(TrinketItem.TRINKETS.get(this.salemRaidTier).size())));
                    Containers.dropItemStack(level, this.boss.getX(), this.boss.getY(), this.boss.getZ(), stack);
                }
                stop();
                salemRaidSavedData.setDirty();
            }
            plebs.removeIf(entity -> !entity.isAlive());
            if (plebs.size() <= 5){ //Check if there aren't a lot of plebs so we can spawn more
                this.plebSpawningTimer++;
            }
            if (this.plebSpawningTimer > PLEB_TIMER_MAX){ //Spawn more plebs
                for (int i = 0; i < 7 + boss.level.random.nextInt(7 + salemRaidTier.tier); i++) {
                    spawnPleb();
                }
                this.plebSpawningTimer = 0;
                this.maxPlebs = this.plebs.size();
                salemRaidSavedData.setDirty();
            }

            //Check for resistance
            int resistanceAmount = getResistance();
            if (resistanceAmount == 0){
                this.boss.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            } else {
                this.boss.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, Integer.MAX_VALUE, resistanceAmount, true, false));
            }
        }

        public UUID getUuid() {
            return uuid;
        }

        public void spawnPleb(){
            Entity entity = this.salemRaidTier.getRandomSpawn(this.boss.level).create(this.boss.level);
            if (entity instanceof LivingEntity){
                ((LivingEntity) entity).addEffect(new MobEffectInstance(SalemMod.SALEM_SPAWN.get(), Integer.MAX_VALUE, 0, true, false));
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, Integer.MAX_VALUE, 1, true, false));
                ((LivingEntity) entity).setHealth(((LivingEntity) entity).getMaxHealth());
            }
            BlockPos pos = BlockUtil.getRandomSurfaceNearby(boss.level, boss.blockPosition(), 5);
            entity.setPos(pos.getX(), pos.getY() - 2, pos.getZ());

            this.boss.level.addFreshEntity(entity);
            if (entity instanceof Mob){
                ((Mob) entity).finalizeSpawn((ServerLevelAccessor) this.boss.level, this.boss.level.getCurrentDifficultyAt(entity.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
            }
            if (entity instanceof AbstractPiglin){
                ((AbstractPiglin) entity).setImmuneToZombification(true);
            }
            if (entity instanceof Hoglin){
                ((Hoglin) entity).setImmuneToZombification(true);
            }
            this.boss.level.getScoreboard().addPlayerToTeam(boss.getStringUUID(), (PlayerTeam) boss.getTeam());
            plebs.add((LivingEntity) entity);
        }

        private Predicate<ServerPlayer> validPlayer() {
            return (serverPlayer) -> {
                BlockPos blockPos = serverPlayer.blockPosition();
                return serverPlayer.isAlive() && this.boss.blockPosition().closerThan(blockPos, 64);
            };
        }

        private void updatePlayers() {
            Set<ServerPlayer> set = Sets.newHashSet(this.raidEvent.getPlayers());
            List<ServerPlayer> list = ((ServerLevel) this.boss.level).getPlayers(this.validPlayer());
            Iterator var3 = list.iterator();

            ServerPlayer serverPlayer2;
            while(var3.hasNext()) {
                serverPlayer2 = (ServerPlayer)var3.next();
                if (!set.contains(serverPlayer2)) {
                    this.raidEvent.addPlayer(serverPlayer2);
                }
            }

            var3 = set.iterator();

            while(var3.hasNext()) {
                serverPlayer2 = (ServerPlayer)var3.next();
                if (!list.contains(serverPlayer2)) {
                    this.raidEvent.removePlayer(serverPlayer2);
                }
            }

        }

        public int getResistance(){
            return Math.max(this.plebs.size() - 5, 0);
        }

        private void updateProgress(){
            if (!this.boss.hasEffect(SalemMod.SALEM_SPAWN.get())){
                if (getResistance() == 0){
                    this.raidEvent.setName(RAID_NAME_COMPONENT_ATTACK);
                    this.raidEvent.setProgress(this.boss.getHealth() / this.boss.getMaxHealth());
                    this.raidEvent.setColor(BossEvent.BossBarColor.RED);
                } else {
                    this.raidEvent.setName(RAID_NAME_COMPONENT_CLEAR);
                    this.raidEvent.setProgress(getResistance() / (float) Math.max(this.maxPlebs - 5, 1));
                    this.raidEvent.setColor(BossEvent.BossBarColor.YELLOW);
                }
            }

        }

        public List<LivingEntity> getPlebs() {
            return plebs;
        }

        public LivingEntity getBoss() {
            return boss;
        }

        public void stop() {
            this.active = false;
            this.plebs.forEach(entity -> entity.remove(Entity.RemovalReason.DISCARDED));
            this.raidEvent.removeAllPlayers();
        }

        public void save(CompoundTag tag){
            tag.putUUID("UUID", uuid);
            tag.putString("Tier", salemRaidTier.name());
            tag.putUUID("Boss", boss.getUUID());
            CompoundTag plebs = new CompoundTag();
            this.plebs.forEach(livingEntity -> {
                plebs.putUUID(livingEntity.getStringUUID(), livingEntity.getUUID());
            });
            tag.put("Plebs", plebs);
            tag.putBoolean("Active", this.active);
        }

        public void load(ServerLevel level, CompoundTag tag){
            this.originalNBT = tag;
            this.uuid = tag.getUUID("UUID");
            this.salemRaidTier = SalemRaidTier.valueOf(tag.getString("Tier"));
            this.active = tag.getBoolean("Active");
        }
    }

    public enum SalemRaidTier{
        COMMON(level -> {
            if (level.random.nextBoolean()){
                return new Zombie(level);
            } else {
                return new Skeleton(EntityType.SKELETON, level);
            }
        }, new Tag.Named[]{SalemMod.RAID_ADD_COMMON}, 2), RARE(level -> {
            if (level.random.nextDouble() > 2/3D){
                return new PiglinBrute(EntityType.PIGLIN_BRUTE, level);
            } else if (level.random.nextDouble() > 1/3D){
                return new Stray(EntityType.STRAY, level);
            }
            return new Husk(EntityType.HUSK, level);
        }, new Tag.Named[]{ SalemMod.RAID_ADD_RARE,SalemMod.RAID_ADD_COMMON}, 3), EPIC(level -> {
            if (level.random.nextDouble() > 2/3D){
                return new Ravager(EntityType.RAVAGER, level);
            } else if (level.random.nextDouble() > 1/3D){
                return new WitherSkeleton(EntityType.WITHER_SKELETON, level);
            }
            return new Phantom(EntityType.PHANTOM, level);
        }, new Tag.Named[]{ SalemMod.RAID_ADD_EPIC, SalemMod.RAID_ADD_COMMON, SalemMod.RAID_ADD_RARE}, 4), LEGENDARY(level -> {
            return new Cat(EntityType.CAT, level);
        }, new Tag.Named[]{SalemMod.RAID_ADD_LEGENDARY, SalemMod.RAID_ADD_COMMON, SalemMod.RAID_ADD_RARE, SalemMod.RAID_ADD_EPIC}, 8);

        private final Function<ServerLevel, Mob> bossSupplier;
        private final Tag.Named<EntityType<?>>[] spawns;
        private final int tier;

        SalemRaidTier(Function<ServerLevel, Mob> bossSupplier, Tag.Named<EntityType<?>>[] spawns, int tier) {
            this.bossSupplier = bossSupplier;
            this.spawns = spawns;
            this.tier = tier;
        }

        public EntityType<?> getRandomSpawn(Level level){
            if (level.random.nextBoolean() || this.spawns.length == 1){
                return this.spawns[0].getRandomElement(level.random);
            }
            return this.spawns[level.random.nextInt(this.spawns.length)].getRandomElement(level.random);
        }
    }

}
