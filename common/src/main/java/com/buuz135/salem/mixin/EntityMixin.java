package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemMod;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow private AABB bb;

    @Shadow private float eyeHeight;

    /**
     * @author Buuz135
     */
    @Inject(method = "Lnet/minecraft/world/entity/Entity;getBoundingBox()Lnet/minecraft/world/phys/AABB;", at = @At("HEAD"), cancellable = true)
    public final void getBoundingBox(CallbackInfoReturnable<AABB> info) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity){
            AABB bounding = this.bb;
            if (((LivingEntity)self).getAttributes().hasAttribute(SalemMod.ENLARGE_ATTRIBUTE.get()) && ((LivingEntity)self).getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue() != 1){
                float value = (float) ((LivingEntity)self).getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue();
                bounding = bounding.inflate(Math.abs(this.bb.getXsize() * (1 - value)) /2D, Math.abs(this.bb.getYsize() * (1 - value)) /2D, Math.abs(this.bb.getZsize() * (1 - value)) /2D).move(0, Math.abs(this.bb.getYsize() * (1 - value)) /2D, 0);
                info.setReturnValue(bounding);
            }
        }
    }

    @Inject(method = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F", at = @At("HEAD"), cancellable = true)
    public final void getEyeHeight(CallbackInfoReturnable<Float> info) {
        Entity self = (Entity) (Object) this;
        if (self instanceof LivingEntity){
            if (((LivingEntity)self).getAttributes() != null && ((LivingEntity)self).getAttributes().hasAttribute(SalemMod.ENLARGE_ATTRIBUTE.get()) && ((LivingEntity)self).getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue() != 1){
                float value = (float) ((LivingEntity)self).getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue();
                info.setReturnValue(this.eyeHeight * value);
            }
        }
    }
}
