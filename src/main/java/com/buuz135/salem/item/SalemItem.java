package com.buuz135.salem.item;

import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.world.item.Item;

public class SalemItem extends Item {

    private final SalemRaidTier tier;

    public SalemItem(Properties properties, SalemRaidTier tier) {
        super(properties);
        this.tier = tier;
    }

}
