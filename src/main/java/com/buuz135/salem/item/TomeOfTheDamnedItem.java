package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import java.util.List;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)
public class TomeOfTheDamnedItem extends TrinketItem{

    public TomeOfTheDamnedItem() {
        super(Rarity.EPIC, SalemRaidTier.LEGENDARY);
    }

    @SubscribeEvent
    public static void livingHurt(LivingDamageEvent.Pre event) {
        if (event.getEntity() instanceof Player player && event.getSource().getEntity() instanceof LivingEntity damage){
            ItemStack tome = InventoryFinderUtil.findFirst(player, SalemContent.TOME_OF_THE_DAMNED_CHARM.get());
            if (!tome.isEmpty()) {
                List<LivingEntity> entityList = player.level().getEntitiesOfClass(LivingEntity.class, AABB.unitCubeFromLowerCorner(player.position()).inflate(5));
                entityList.removeIf(entity -> entity.getId() == damage.getId() || entity instanceof Player);
                if (entityList.size() > 0){
                    entityList.get(player.level().random.nextInt(entityList.size())).doHurtTarget(damage);
                }
            }
        }
    }
}
