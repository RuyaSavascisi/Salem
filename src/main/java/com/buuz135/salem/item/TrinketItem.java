package com.buuz135.salem.item;

import com.buuz135.salem.util.SalemRaidTier;
import com.buuz135.salem.world.SalemRaidSavedData;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TrinketItem extends SalemItem{

    public static ListMultimap<SalemRaidTier, TrinketItem> TRINKETS = MultimapBuilder.hashKeys().arrayListValues().build();

    private final SalemRaidTier tier;

    public TrinketItem(Rarity rarity, SalemRaidTier tier) {
        super(new Properties().stacksTo(1).rarity(rarity));
        this.tier = tier;
        TRINKETS.put(tier, this);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable(getDescriptionId()+".tooltip").withStyle(ChatFormatting.GRAY));
    }

}
