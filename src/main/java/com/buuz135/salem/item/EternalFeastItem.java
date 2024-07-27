package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.mixin.IZombieVillagerMixin;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)
public class EternalFeastItem extends TrinketItem{

    public EternalFeastItem() {
        super(Rarity.COMMON, SalemRaidTier.COMMON);
    }

    @SubscribeEvent
    public static void livingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof Zombie zombie){
            ItemStack eternalFeast = InventoryFinderUtil.findFirst(player, SalemContent.ETERNAL_FEAST_BELT.asItem());
            if (!eternalFeast.isEmpty()) {
                if (player.level().random.nextDouble() < .5d){
                    player.getFoodData().setFoodLevel(20);
                    player.getFoodData().setSaturation(20);
                }
                if (zombie instanceof ZombieVillager zombieVillager && zombieVillager.level() instanceof ServerLevel serverLevel){
                    ((IZombieVillagerMixin)zombieVillager).callStartConverting(player.getUUID(), 1);
                }
            }
        }
    }
}
