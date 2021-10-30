package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class NigthmarishWingsItem extends TrinketItem {

    public NigthmarishWingsItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        TickEvent.PLAYER_POST.register(player -> {
            ItemStack stack = InventoryFinderUtil.findFirst(player, this);
            if (!stack.isEmpty()) {
                if (player.isFallFlying() || stack.getOrCreateTag().getInt("Flying") > 0) {
                    player.startFallFlying();
                    stack.getOrCreateTag().putInt("Flying", stack.getOrCreateTag().getInt("Flying") + 1);
                }
                if (player.isOnGround() || player.isInWater()){
                    stack.getOrCreateTag().putInt("Flying", 0);
                    player.stopFallFlying();
                }
            }
        });

    }


}
