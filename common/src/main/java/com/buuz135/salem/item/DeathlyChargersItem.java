package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class DeathlyChargersItem extends TrinketItem{

    public DeathlyChargersItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        TickEvent.Player.PLAYER_PRE.register(player -> {
            ItemStack deathlyChargers = InventoryFinderUtil.findFirst(player, this);
            if (!deathlyChargers.isEmpty()) {
                if (player.level.getGameTime() % 40 == 0){
                    if (!deathlyChargers.getOrCreateTag().contains("Time")){
                        deathlyChargers.getOrCreateTag().putLong("Time", 0);
                    }
                    long speed = deathlyChargers.getOrCreateTag().getLong("Time") + 1;
                    if (!player.isSprinting()){
                        speed = -1;
                    }
                    deathlyChargers.getOrCreateTag().putLong("Time", speed);
                }
                if (player.isSprinting()){
                    long speed = deathlyChargers.getOrCreateTag().getLong("Time");
                    player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, (int) speed, true, false));
                    if (speed > 10){
                        player.setSecondsOnFire(1);
                    }
                }else{
                    deathlyChargers.getOrCreateTag().putLong("Time", -1);
                }
            }
        });
    }


}
