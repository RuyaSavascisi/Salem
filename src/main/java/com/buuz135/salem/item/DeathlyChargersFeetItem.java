package com.buuz135.salem.item;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.SalemRaidTier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class DeathlyChargersFeetItem extends SalemItem {

    public DeathlyChargersFeetItem() {
        super(new Properties().fireResistant().stacksTo(1).rarity(Rarity.RARE), SalemRaidTier.EPIC);
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
}
