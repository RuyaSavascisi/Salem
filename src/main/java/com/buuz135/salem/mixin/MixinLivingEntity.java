package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
   This was taken from Ars Nouveau https://github.com/baileyholl/Ars-Nouveau
 */

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @ModifyExpressionValue(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;canElytraFly(Lnet/minecraft/world/entity/LivingEntity;)Z",
                    remap = false
            )
    )
    public boolean elytraOverride(boolean original) {
        return original || !InventoryFinderUtil.findFirst((LivingEntity)(Object)this, SalemContent.NIGHTMARISH_WINGS_BACK.asItem()).isEmpty();
    }

    @ModifyExpressionValue(
            method = "updateFallFlying",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;elytraFlightTick(Lnet/minecraft/world/entity/LivingEntity;I)Z",
                    remap = false
            )
    )
    public boolean eytraValidOverride(boolean original) {
        return  original || !InventoryFinderUtil.findFirst((LivingEntity)(Object)this, SalemContent.NIGHTMARISH_WINGS_BACK.asItem()).isEmpty();
    }
}
