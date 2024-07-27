package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = Salem.MODID, bus = EventBusSubscriber.Bus.GAME)
public class DeathlyChargersFeetItem extends TrinketItem implements Equipable {

    public DeathlyChargersFeetItem() {
        super(Rarity.RARE, SalemRaidTier.EPIC);
    }

    @Override
    public ItemAttributeModifiers getDefaultAttributeModifiers(ItemStack stack) {
        var modifiers = super.getDefaultAttributeModifiers(stack);
        modifiers = modifiers.withModifierAdded(Attributes.MOVEMENT_SPEED,
                new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Salem.MODID, "deathly_chargers"), 0.1 + stack.getOrDefault(SalemContent.DataComp.TIME, 0L) * 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                EquipmentSlotGroup.ANY
                );
        return modifiers;
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.FEET;
    }

    @SubscribeEvent
    public static void playerTickEvent(PlayerTickEvent.Pre event){
        var player = event.getEntity();
        ItemStack deathlyChargers = InventoryFinderUtil.findFirst(player, SalemContent.DEATHLY_CHARGERS_FEET.asItem());
        if (!deathlyChargers.isEmpty()) {
            if (player.level().getGameTime() % 40 == 0){
                if (!deathlyChargers.has(SalemContent.DataComp.TIME)){
                    deathlyChargers.set(SalemContent.DataComp.TIME, 0L);
                }
                long speed = deathlyChargers.getOrDefault(SalemContent.DataComp.TIME, 0L) + 1;
                if (!player.isSprinting()){
                    speed = -1;
                }
                deathlyChargers.set(SalemContent.DataComp.TIME, speed);
            }
            if (player.isSprinting()){
                long speed = deathlyChargers.getOrDefault(SalemContent.DataComp.TIME, 0L);
                if (speed > 0 && player.level() instanceof ServerLevel serverLevel){
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME, player.getX(), player.getY() + 0.1, player.getZ(), 2, 0.2,0.1,0.2,0);
                    if (speed > 5) serverLevel.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, player.getX(), player.getY() + 0.25, player.getZ(), 1, 0.2,0.1,0.2,0);
                }
                if (speed > 15){
                    player.setRemainingFireTicks(20);
                }
            }else{
                deathlyChargers.set(SalemContent.DataComp.TIME, 0L);
            }
        }
    }
}
