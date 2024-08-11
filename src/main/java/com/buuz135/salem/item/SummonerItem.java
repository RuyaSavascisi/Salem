package com.buuz135.salem.item;

import com.buuz135.salem.util.BlockUtil;
import com.buuz135.salem.util.SalemRaidTier;
import com.buuz135.salem.world.SalemRaidSavedData;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.List;

public class SummonerItem extends SalemItem{

    private final SalemRaidTier tier;

    public SummonerItem(Rarity rarity, SalemRaidTier tier) {
        super(new Properties().rarity(rarity).stacksTo(1));
        this.tier = tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.translatable("tooltip.salem.only_night").withStyle(ChatFormatting.GRAY));
        tooltipComponents.add(Component.literal(""));
        tooltipComponents.add(Component.translatable("tooltip.salem.can_reward").withStyle(ChatFormatting.GRAY));
        for (TrinketItem trinketItem : TrinketItem.TRINKETS.get(this.tier)) {
            tooltipComponents.add(Component.literal("- ").append(trinketItem.getName(new ItemStack(trinketItem))).withStyle(ChatFormatting.GRAY));
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand) {
        if (level.isNight()){
            ItemStack itemstack = player.getItemInHand(usedHand);
            player.startUsingItem(usedHand);
            return InteractionResultHolder.consume(itemstack);
        }
        if (level.dayTime() < 13000) player.playSound(SoundEvents.FIRE_EXTINGUISH, 1, 1);
        return super.use(level, player, usedHand);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BRUSH;
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 20*2;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel) {
            SalemRaidSavedData.getData(serverLevel).startRaid(BlockUtil.getRandomSurfaceNearby(serverLevel, livingEntity.blockPosition(), 16), tier);
        }
        return ItemStack.EMPTY;
    }
}
