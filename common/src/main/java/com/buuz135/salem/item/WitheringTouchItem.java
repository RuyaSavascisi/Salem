package com.buuz135.salem.item;

import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class WitheringTouchItem extends TrinketItem{

    public WitheringTouchItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && damageSource.equals(DamageSource.WITHER)){
                ItemStack witherTouch = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!witherTouch.isEmpty()) {
                    return EventResult.interruptFalse();
                }
            }
            if (damageSource.getEntity() instanceof Player){
                ItemStack witherTouch = InventoryFinderUtil.findFirst((Player) damageSource.getEntity(), this);
                if (!witherTouch.isEmpty()) {
                    livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
                }
            }
            return EventResult.pass();
        });
    }


}
