package com.buuz135.salem.mixin;

import com.buuz135.salem.Salem;
import com.buuz135.salem.SalemContent;
import net.minecraft.client.renderer.entity.CatRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CatRenderer.class)
public class CatRenderedMixin {

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cat;)Lnet/minecraft/resources/ResourceLocation;", at = @At("HEAD"), cancellable = true)
    public void getTextureLocation(Cat cat, CallbackInfoReturnable<ResourceLocation> cir) {
        var attribute = cat.getAttribute(Attributes.SCALE);
        if (attribute.hasModifier(SalemContent.Effect.ENLARGE_ATTRIBUTE)){
            cir.setReturnValue(ResourceLocation.fromNamespaceAndPath(Salem.MODID, "textures/entity/salem.png"));
        }
    }
}
