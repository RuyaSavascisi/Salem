package com.buuz135.salem.item;

import com.buuz135.salem.SalemMod;
import com.buuz135.salem.world.SalemRaidSavedData;
import com.google.common.collect.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class TrinketItem extends Item {

    public static ListMultimap<SalemRaidSavedData.SalemRaidTier, TrinketItem> TRINKETS = MultimapBuilder.hashKeys().arrayListValues().build();

    public TrinketItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(new Properties().stacksTo(1).tab(SalemMod.TAB).rarity(rarity));
        TRINKETS.put(raidTier, this);
    }
}
