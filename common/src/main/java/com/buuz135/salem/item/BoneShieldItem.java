package com.buuz135.salem.item;

import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class BoneShieldItem extends TrinketItem{

    public BoneShieldItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity instanceof Player && livingEntity.level instanceof ServerLevel){
                ItemStack boneShield = InventoryFinderUtil.findFirst((Player) livingEntity, this);
                if (!boneShield.isEmpty() && boneShield.getOrCreateTag().contains("Time")) {
                    long time = boneShield.getOrCreateTag().getLong("Time");
                    double currentProtection = getCurrentDamageProtection(time);
                    double protectedDamage = Math.min(currentProtection, v);
                    long leftOverTime = (long) getCurrentTimeForProtection(Math.max(currentProtection - protectedDamage, 0));
                    if (protectedDamage > 0){
                        ((ServerLevel) livingEntity.level).playSound(null, livingEntity.position().x, livingEntity.position().y, livingEntity.position().z, SoundEvents.SHIELD_BLOCK, SoundSource.PLAYERS, 1, 1);
                        boneShield.getOrCreateTag().putLong("Time", leftOverTime);
                        if (protectedDamage < v){
                            livingEntity.hurt(damageSource, (float) (v - protectedDamage));
                        }
                        return EventResult.interruptFalse();
                    }
                }
            }
            return EventResult.pass();
        });
        TickEvent.Player.PLAYER_PRE.register(player -> {
            if (player.level.getGameTime() % 20 == 0){
                ItemStack boneShield = InventoryFinderUtil.findFirst(player, this);
                if (!boneShield.isEmpty()) {
                    if (!boneShield.getOrCreateTag().contains("Time")){
                        boneShield.getOrCreateTag().putLong("Time", 0);
                    }
                    boneShield.getOrCreateTag().putLong("Time", boneShield.getOrCreateTag().getLong("Time") + 1);
                }
            }
        });
    }


    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, list, tooltipFlag);
        if (itemStack.hasTag() && itemStack.getTag().contains("Time")){
            long time = itemStack.getTag().getLong("Time");
            list.add(new TextComponent("Negates Damage: ").withStyle(ChatFormatting.GOLD).append(new TextComponent("" + (int) getCurrentDamageProtection(time)).withStyle(ChatFormatting.WHITE)));
            list.add(new TextComponent("Stored Time: ").withStyle(ChatFormatting.GOLD).append(new TextComponent(format((int) (time / (60*60))) + ":" + format((int) (time % (60*60) / 60)) + ":" + format((int) (time % (60*60) % 60))).withStyle(ChatFormatting.WHITE)));
        }
    }

    private double getCurrentDamageProtection(long time){
        return Math.sqrt(time / 60D);
    }

    private double getCurrentTimeForProtection(double damage){
        return damage * damage * 60;
    }

    private String format(int value){
        return (value < 10 ? "0" : "") + value;
    }
}
