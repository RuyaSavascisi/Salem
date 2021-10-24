package com.buuz135.salem.item;

import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class EternalFeastItem extends TrinketItem{

    public EternalFeastItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && damageSource.getEntity() instanceof Zombie){
                ItemStack eternalFeast = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!eternalFeast.isEmpty()) {
                    if (livingEntity.level.random.nextDouble() < .5d){
                        ((Player) livingEntity).getFoodData().setFoodLevel(20);
                        ((Player) livingEntity).getFoodData().setSaturation(20);
                    }
                    if (damageSource.getEntity() instanceof ZombieVillager && damageSource.getEntity().level instanceof ServerLevel){
                        ((IZombieVillagerMixin)damageSource.getEntity()).callFinishConversion((ServerLevel) damageSource.getEntity().level);
                    }
                }
            }
            return EventResult.pass();
        });
    }


}
