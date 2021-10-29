package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TomeOfTheDamnedItem extends TrinketItem{

    public TomeOfTheDamnedItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && damageSource.getEntity() instanceof LivingEntity){
                ItemStack tome = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!tome.isEmpty()) {
                    List<LivingEntity> entityList = livingEntity.level.getEntitiesOfClass(LivingEntity.class, AABB.unitCubeFromLowerCorner(livingEntity.position()).inflate(5));
                    entityList.removeIf(entity -> entity.getId() == damageSource.getEntity().getId() || entity instanceof Player);
                    if (entityList.size() > 0){
                        entityList.get(livingEntity.level.random.nextInt(entityList.size())).doHurtTarget(((LivingEntity)damageSource.getEntity()));
                    }
                }
            }
            return EventResult.pass();
        });
    }


}
