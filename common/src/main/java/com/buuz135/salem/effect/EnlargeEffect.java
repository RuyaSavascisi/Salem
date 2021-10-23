package com.buuz135.salem.effect;

import com.buuz135.salem.SalemMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.phys.AABB;

public class EnlargeEffect extends MobEffect {


    public EnlargeEffect(MobEffectCategory mobEffectCategory, int i) {
        super(mobEffectCategory, i);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int i) {
        super.removeAttributeModifiers(livingEntity, attributeMap, i);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int i) {
        super.addAttributeModifiers(livingEntity, attributeMap, i);


    }
}
