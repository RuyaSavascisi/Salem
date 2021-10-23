package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AttributeSupplier.Builder.class)
public class AttributeSupplierBuilderMixin {

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier$Builder;build()Lnet/minecraft/world/entity/ai/attributes/AttributeSupplier;")
    public void build(CallbackInfoReturnable<AttributeSupplier> cir) {
        try {
            add(SalemMod.ENLARGE_ATTRIBUTE.get());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Shadow
    public AttributeSupplier.Builder add(Attribute attribute) throws IllegalAccessException {
        throw new IllegalAccessException("Mixin failed to shadow getBuilder()");
    }

}
