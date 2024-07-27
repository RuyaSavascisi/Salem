package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemContent;
import com.buuz135.salem.item.UnhallowedCrossItem;
import com.buuz135.salem.util.InventoryFinderUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class TotemLivingEntityMixin {

    @Inject(method = "Lnet/minecraft/world/entity/LivingEntity;checkTotemDeathProtection(Lnet/minecraft/world/damagesource/DamageSource;)Z", at = @At("HEAD"), cancellable = true)
    private  void checkTotemDeathProtection(DamageSource damageSource, CallbackInfoReturnable<Boolean> info) {
        LivingEntity instance = (LivingEntity) (Object) this;
        if (instance instanceof Player){
            ItemStack unhallowedCross = InventoryFinderUtil.findFirst((Player)instance, SalemContent.UNHALLOWED_CROSS.get());
            if (!unhallowedCross.isEmpty()){
                if (!damageSource.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    if (UnhallowedCrossItem.getAmount(unhallowedCross) > 0) {
                        if (instance instanceof ServerPlayer) {
                            ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
                            serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                        }
                        this.setHealth(1.0F);
                        this.removeAllEffects();
                        this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 900, 1));
                        this.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 100, 1));
                        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 800, 0));
                        instance.level().broadcastEntityEvent(instance, (byte)35);
                        info.setReturnValue(true);
                        UnhallowedCrossItem.setAmount(unhallowedCross ,UnhallowedCrossItem.getAmount(unhallowedCross) -1);
                    }
                }
            }
        }
    }

    @Shadow
    public void setHealth(float health){

    };

    @Shadow
    public boolean removeAllEffects(){
        return false;
    };

    @Shadow
    public boolean addEffect(MobEffectInstance instance){
        return false;
    };

}
