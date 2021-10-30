package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemMod;
import com.buuz135.salem.effect.EnlargeEffect;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V")
    public void setupRotations(LivingEntity livingEntity, PoseStack poseStack, float f, float g, float h, CallbackInfo info) {
        if (livingEntity.getAttributes().hasAttribute(SalemMod.ENLARGE_ATTRIBUTE.get()) && livingEntity.getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue() != 1){
            float value = (float) livingEntity.getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue();
            poseStack.scale(value, value, value);
        }
    }
}
