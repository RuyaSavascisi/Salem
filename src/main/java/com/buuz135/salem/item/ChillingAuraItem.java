package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)

public class ChillingAuraItem extends TrinketItem{

    public ChillingAuraItem() {
        super(Rarity.UNCOMMON, SalemRaidTier.RARE);
    }

    @SubscribeEvent
    public static void livingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof LivingEntity livingEntity){
            ItemStack chillingAura = InventoryFinderUtil.findFirst(player, SalemContent.CHILLING_AURA_NECKLACE.asItem());
            if (!chillingAura.isEmpty()) {
                livingEntity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
                if (player.level() instanceof ServerLevel serverLevel){
                    serverLevel.playSound(null, player.position().x, player.position().y, player.position().z, SoundEvents.PLAYER_HURT_FREEZE, SoundSource.PLAYERS, 1f, 1);
                }
            }
        }
    }
}
