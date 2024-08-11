package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)
public class WitheringTouchItem extends TrinketItem{

    public WitheringTouchItem() {
        super(Rarity.UNCOMMON, SalemRaidTier.RARE);
    }

    @SubscribeEvent
    public static void livingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && event.getSource().is(DamageTypes.WITHER)){
            ItemStack witherTouch = InventoryFinderUtil.findFirst(player, SalemContent.WITHERING_TOUCH_HAND.get());
            if (!witherTouch.isEmpty()) {
                event.setNewDamage(0);
            }
        }
        if (event.getSource().getEntity() instanceof Player player){
            ItemStack witherTouch = InventoryFinderUtil.findFirst(player, SalemContent.WITHERING_TOUCH_HAND.get());
            if (!witherTouch.isEmpty()) {
                event.getEntity().addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            }
        }
    }
}
