package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class UnhallowedCrossItem extends TrinketItem{

    public UnhallowedCrossItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack held = player.getItemInHand(interactionHand);
        ItemStack totem = InventoryFinderUtil.findFirst(player, Items.TOTEM_OF_UNDYING);
        if (!totem.isEmpty() && !held.isEmpty() && getAmount(held) < 5){
            totem.shrink(1);
            setAmount(held, getAmount(held) + 1);
            return InteractionResultHolder.success(held);
        }
        return super.use(level, player, interactionHand);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        list.add(new TextComponent("Stored Totems: ").withStyle(ChatFormatting.GOLD).append(new TextComponent("" +  getAmount(itemStack)).withStyle(ChatFormatting.WHITE)));
    }

    public static int getAmount(ItemStack stack){
        return stack.getOrCreateTag().getInt("Amount");
    }

    public static void setAmount(ItemStack stack, int amount){
        stack.getOrCreateTag().putInt("Amount", amount);
    }
}
