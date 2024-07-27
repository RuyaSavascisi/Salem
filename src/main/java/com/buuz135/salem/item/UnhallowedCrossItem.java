package com.buuz135.salem.item;

import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class UnhallowedCrossItem extends TrinketItem{

    public UnhallowedCrossItem() {
        super(Rarity.EPIC, SalemRaidTier.LEGENDARY);
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("Stored Totems: ").withStyle(ChatFormatting.GOLD).append(Component.literal("" +  getAmount(stack)).withStyle(ChatFormatting.WHITE)));
    }

    public static int getAmount(ItemStack stack){
        return stack.getOrDefault(SalemContent.DataComp.AMOUNT, 0);
    }

    public static void setAmount(ItemStack stack, int amount){
        stack.set(SalemContent.DataComp.AMOUNT, amount);
    }
}
