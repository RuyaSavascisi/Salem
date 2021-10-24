package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class ChillingAuraItem extends TrinketItem{

    public ChillingAuraItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && damageSource.getEntity() instanceof LivingEntity){
                ItemStack chillingAura = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!chillingAura.isEmpty()) {
                    ((LivingEntity) damageSource.getEntity()).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
                }
            }
            return EventResult.pass();
        });
    }


}
