package com.buuz135.salem.mixin;

import com.buuz135.salem.SalemContent;
import com.buuz135.salem.util.InventoryFinderUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PiglinAi.class)
public class AbstractPiglinMixin {

    @Inject(method = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isWearingGold(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private static void isWearingGold(LivingEntity entity, CallbackInfoReturnable<Boolean> info) {
        if (entity instanceof Player){
            if (!InventoryFinderUtil.findFirst((Player)entity, SalemContent.HELLISH_BARGAIN_RING.get()).isEmpty()){
                info.setReturnValue(true);
            }
        }
    }

    /**
     * @author Buuz135 Salem
     */
    @Overwrite
    private static List<ItemStack> getBarterResponseItems(Piglin piglin){
        LootTable lootTable = piglin.level().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.PIGLIN_BARTERING);
        List<ItemStack> list = lootTable.getRandomItems((new LootParams.Builder((ServerLevel)piglin.level())).withParameter(LootContextParams.THIS_ENTITY, piglin).create(LootContextParamSets.PIGLIN_BARTER));
        for (Player nearbyPlayer : piglin.level().getNearbyPlayers(TargetingConditions.forNonCombat(), piglin, new AABB(piglin.blockPosition()).inflate(5))) {
            ItemStack stack = InventoryFinderUtil.findFirst(nearbyPlayer, SalemContent.HELLISH_BARGAIN_RING.get());
            if (!stack.isEmpty()){
                list.addAll(lootTable.getRandomItems((new LootParams.Builder((ServerLevel)piglin.level())).withParameter(LootContextParams.THIS_ENTITY, piglin).create(LootContextParamSets.PIGLIN_BARTER)));
                break;
            }
        }
        return list;
    }
}
