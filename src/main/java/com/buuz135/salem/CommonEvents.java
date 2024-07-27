package com.buuz135.salem;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public class CommonEvents {

    @SubscribeEvent
    public void deathlyChargersFeet(PlayerTickEvent.Pre event){
        var player = event.getEntity();
        ItemStack deathlyChargers = InventoryFinderUtil.findFirst(player, SalemContent.DEATHLY_CHARGERS_FEET.asItem());
        if (!deathlyChargers.isEmpty()) {
            if (player.level().getGameTime() % 40 == 0){
                if (!deathlyChargers.has(SalemContent.DataComp.TIME)){
                    deathlyChargers.set(SalemContent.DataComp.TIME, 0L);
                }
                long speed = deathlyChargers.getOrDefault(SalemContent.DataComp.TIME, 0L) + 1;
                if (!player.isSprinting()){
                    speed = -1;
                }
                deathlyChargers.set(SalemContent.DataComp.TIME, speed);
            }
            if (player.isSprinting()){
                long speed = deathlyChargers.getOrDefault(SalemContent.DataComp.TIME, 0L);
                if (speed > 0 && player.level() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 0.1, player.getZ(), 2, 0.2,0.1,0.2,0);
                    if (speed > 5) serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY() + 0.25, player.getZ(), 1, 0.2,0.1,0.2,0);
                }
                if (speed > 15){
                    player.setRemainingFireTicks(20);
                }
            }else{
                deathlyChargers.set(SalemContent.DataComp.TIME, 0L);
            }
        }
    }

    @SubscribeEvent
    public void serverTick(LevelTickEvent.Pre event){
        if (event.getLevel() instanceof ServerLevel serverLevel){
            SalemRaidSavedData.getData(serverLevel).tick();
            serverLevel.getEntities(EntityTypeTest.forClass(Mob.class), mob -> mob.hasEffect(SalemContent.Effect.SPAWN_EFFECT)).forEach(CommonEvents::salemSpawnCheck);
        }
    }

    @SubscribeEvent
    public void livingHurt(LivingIncomingDamageEvent event){
        if (event.getEntity().hasEffect(SalemContent.Effect.SPAWN_EFFECT)){
            event.setCanceled(true);
        }
    }

    public static void salemSpawnCheck(Mob trackedMonster){
        BlockPos pos = new BlockPos.MutableBlockPos(trackedMonster.getX(), trackedMonster.getY(), trackedMonster.getZ());
        if (trackedMonster.level().getBlockState(pos).isAir()){
            trackedMonster.removeEffect(SalemContent.Effect.SPAWN_EFFECT);
            trackedMonster.setNoAi(false);
        }else {
            trackedMonster.setNoAi(true);
            trackedMonster.setPos(trackedMonster.getX(), trackedMonster.getY() + 0.04, trackedMonster.getZ());
            if (trackedMonster.level() instanceof ServerLevel){
                if (trackedMonster.level().random.nextBoolean()){
                    for (int i = 0; i < 5; i++) {
                        BlockPos checkedPos = new BlockPos.MutableBlockPos(trackedMonster.getX(), trackedMonster.getY() + i, trackedMonster.getZ());
                        if (trackedMonster.level().getBlockState(checkedPos.above()).isAir()){
                            ((ServerLevel)trackedMonster.level()).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, trackedMonster.level().getBlockState(checkedPos)), trackedMonster.getX(), trackedMonster.getBlockY() + i + 1, trackedMonster.getZ(), 10,0.1,0,0.1,0);
                            if (trackedMonster.level().random.nextBoolean()) ((ServerLevel)trackedMonster.level()).playSound(null, trackedMonster.getX(), trackedMonster.getBlockY() + i + 1, trackedMonster.getZ(), SoundEvents.GRASS_BREAK, SoundSource.BLOCKS,0.5f , 1);
                            break;
                        }
                    }
                }
            }
        }
    }
}
