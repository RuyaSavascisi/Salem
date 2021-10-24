package com.buuz135.salem.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.ZombieVillager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ZombieVillager.class)
public interface IZombieVillagerMixin {

    @Invoker
    void callFinishConversion(ServerLevel serverLevel);
}
