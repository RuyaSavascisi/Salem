package com.buuz135.salem.item;

import com.buuz135.salem.util.InventoryFinderUtil;
import com.buuz135.salem.world.SalemRaidSavedData;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class NigthmarishWingsItem extends TrinketItem {

    public NigthmarishWingsItem(SalemRaidSavedData.SalemRaidTier raidTier, Rarity rarity) {
        super(raidTier, rarity);
        TickEvent.PLAYER_POST.register(player -> {
            ItemStack stack = InventoryFinderUtil.findFirst(player, this);
            if (!stack.isEmpty()) {
                if (player.isFallFlying() || stack.getOrCreateTag().getInt("Flying") > 0) {
                    player.startFallFlying();
                    stack.getOrCreateTag().putInt("Flying", stack.getOrCreateTag().getInt("Flying") + 1);
                }
                if (player.isOnGround() || player.isInWater()){
                    stack.getOrCreateTag().putInt("Flying", 0);
                    player.stopFallFlying();
                }
            }
        });
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (minecraft.player == null) return;
            ItemStack stack = InventoryFinderUtil.findFirst(minecraft.player, this);
            if (!stack.isEmpty()) {
                if (minecraft.player.input.jumping && minecraft.player.getDeltaMovement().y < 0 && !minecraft.player.isOnGround() && !minecraft.player.isSwimming() && !minecraft.player.getAbilities().flying && !minecraft.player.isPassenger() && !minecraft.player.onClimbable()) {
                    minecraft.player.connection.send(new ServerboundPlayerCommandPacket(minecraft.player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
                }
            }
        });
    }


}
