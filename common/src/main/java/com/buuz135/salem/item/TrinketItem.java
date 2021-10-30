package com.buuz135.salem.item;

import com.buuz135.salem.SalemMod;
import com.buuz135.salem.world.SalemRaidSavedData;
import com.google.common.collect.*;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class TrinketItem extends Item {

    public static ListMultimap<SalemRaidSavedData.SalemRaidTier, TrinketItem> TRINKETS = MultimapBuilder.hashKeys().arrayListValues().build();

    public TrinketItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(new Properties().stacksTo(1).tab(SalemMod.TAB).rarity(rarity));
        TRINKETS.put(raidTier, this);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(new TranslatableComponent(getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }
}
