package com.buuz135.salem.item;

import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.gameevent.GameEvent;

public class NightmarishWingsItem extends TrinketItem{

    public NightmarishWingsItem() {
        super(Rarity.RARE, SalemRaidTier.EPIC);
    }

    @Override
    public boolean canElytraFly(ItemStack stack, LivingEntity entity) {
        return true;
    }

}
