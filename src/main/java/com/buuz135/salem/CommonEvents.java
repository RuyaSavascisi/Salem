package com.buuz135.salem;


import com.buuz135.salem.world.SalemRaidSavedData;
import com.buuz135.salem.world.SalemRaidSpawner;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class CommonEvents {

    public static HashMap<String, SalemRaidSpawner> RAID_SPAWNER = new LinkedHashMap<>();

    @SubscribeEvent
    public void serverTick(LevelTickEvent.Pre event){
        if (event.getLevel() instanceof ServerLevel serverLevel){
            SalemRaidSavedData.getData(serverLevel).tick();
            serverLevel.getEntities(EntityTypeTest.forClass(Mob.class), mob -> mob.hasEffect(SalemContent.Effect.SPAWN_EFFECT)).forEach(CommonEvents::salemSpawnCheck);
            if (Config.ENABLE_RANDOM_RAIDS.get()) RAID_SPAWNER.computeIfAbsent(event.getLevel().dimension().location().toString(), s -> new SalemRaidSpawner()).tick(serverLevel);
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
