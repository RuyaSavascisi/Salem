package com.buuz135.item;

import com.buuz135.SalemMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;

public class TrinketItem extends Item {

    public TrinketItem(Rarity rarity) {
        super(new Properties().stacksTo(1).tab(SalemMod.TAB).rarity(rarity));
    }
}
