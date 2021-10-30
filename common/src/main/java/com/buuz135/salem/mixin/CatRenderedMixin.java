package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemMod;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatRenderer.class)
public class CatRenderedMixin {

    @Inject(method = "Lnet/minecraft/client/renderer/entity/CatRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getTextureLocation(Entity entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (entity instanceof Cat && ((Cat)entity).getAttributes().hasAttribute(SalemMod.ENLARGE_ATTRIBUTE.get()) && ((Cat)entity).getAttributes().getInstance(SalemMod.ENLARGE_ATTRIBUTE.get()).getValue() != 1){
            cir.setReturnValue(new ResourceLocation(SalemMod.MOD_ID, "textures/entity/salem.png"));
        }
    }
}
