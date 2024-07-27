package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)
public class BoneShieldItem extends TrinketItem{

    public BoneShieldItem() {
        super(Rarity.COMMON, SalemRaidTier.COMMON);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        if (stack.has(SalemContent.DataComp.TIME)){
            long time = stack.getOrDefault(SalemContent.DataComp.TIME, 0L);
            tooltipComponents.add(Component.literal("Negates Damage: ").withStyle(ChatFormatting.GOLD).append(Component.literal("" + (int) getCurrentDamageProtection(time)).withStyle(ChatFormatting.WHITE)));
            tooltipComponents.add(Component.literal("Stored Time: ").withStyle(ChatFormatting.GOLD).append(Component.literal(format((int) (time / (60*60))) + ":" + format((int) (time % (60*60) / 60)) + ":" + format((int) (time % (60*60) % 60))).withStyle(ChatFormatting.WHITE)));
        }
    }

    private static double getCurrentDamageProtection(long time){
        return Math.sqrt(time / 30D);
    }

    private static double getCurrentTimeForProtection(double damage){
        return damage * damage * 30;
    }

    private String format(int value){
        return (value < 10 ? "0" : "") + value;
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event){
        var player = event.getEntity();
        if (player.level().getGameTime() % 20 == 0){
            ItemStack boneShield = InventoryFinderUtil.findFirst(player, SalemContent.BONE_SHIELD.asItem());
            if (!boneShield.isEmpty()) {
                boneShield.set(SalemContent.DataComp.TIME, boneShield.getOrDefault(SalemContent.DataComp.TIME, 0L) + 1);
            }
        }
    }

    @SubscribeEvent
    public static void livingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && event.getEntity().level() instanceof ServerLevel serverLevel){
            ItemStack boneShield = InventoryFinderUtil.findFirst(player, SalemContent.BONE_SHIELD.asItem());
            if (!boneShield.isEmpty()) {
                long time = boneShield.getOrDefault(SalemContent.DataComp.TIME, 0L);
                double currentProtection = getCurrentDamageProtection(time);
                double protectedDamage = Math.min(currentProtection, event.getOriginalDamage());
                long leftOverTime = (long) getCurrentTimeForProtection(Math.max(currentProtection - protectedDamage, 0));
                if (protectedDamage >= 0.5){
                    serverLevel.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 0.5f, 1);
                    serverLevel.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.SKELETON_AMBIENT, SoundSource.PLAYERS, 0.5f, 1);
                    boneShield.set(SalemContent.DataComp.TIME, leftOverTime);
                    event.setNewDamage((float) (event.getOriginalDamage() - protectedDamage));
                }
            }
        }
    }

}
