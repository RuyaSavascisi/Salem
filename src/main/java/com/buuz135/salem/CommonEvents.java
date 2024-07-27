package com.buuz135.salem;

import com.buuz135.salem.util.InventoryFinderUtil;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
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
}
