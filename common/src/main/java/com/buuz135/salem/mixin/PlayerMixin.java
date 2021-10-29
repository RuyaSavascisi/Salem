package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemMod;
import com.buuz135.salem.util.InventoryFinderUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public class PlayerMixin {


    @Inject(method = "Lnet/minecraft/world/entity/player/Player;tryToStartFallFlying()Z", at = @At("HEAD"), cancellable = true)
    public void tryToStartFallFlying(CallbackInfoReturnable<Boolean> info) {
        Player self =  (Player)(Object)this;
        if (!self.isOnGround() && !self.isFallFlying() && !self.isInWater() && !self.hasEffect(MobEffects.LEVITATION)) {
            ItemStack wings = InventoryFinderUtil.findFirst(self, SalemMod.NIGHTMARISH_WINGS.get());
            if (!wings.isEmpty()) {
                self.startFallFlying();
                info.setReturnValue(true);
            }
        }
    }

}
