package com.buuz135.salem.item;

import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ScorchingAuraItem extends TrinketItem{

    public ScorchingAuraItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && damageSource.getEntity() instanceof LivingEntity){
                ItemStack scorchingAura = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!scorchingAura.isEmpty()) {
                    damageSource.getEntity().setSecondsOnFire(2);
                }
            }
            return EventResult.pass();
        });
    }


}
