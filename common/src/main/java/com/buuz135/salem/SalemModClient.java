package com.buuz135.salem;

import com.buuz135.salem.util.InventoryFinderUtil;
import dev.architectury.event.events.client.ClientTickEvent;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.item.ItemStack;


public class SalemModClient {

    public SalemModClient() {
        init();
    }

    @Environment(EnvType.CLIENT)
    private void init(){
        ClientTickEvent.CLIENT_POST.register(minecraft -> {
            if (minecraft.player == null) return;
            ItemStack stack = InventoryFinderUtil.findFirst(minecraft.player, SalemMod.NIGHTMARISH_WINGS.get());
            if (!stack.isEmpty()) {
                if (minecraft.player.input.jumping && minecraft.player.getDeltaMovement().y < 0 && !minecraft.player.isOnGround() && !minecraft.player.isSwimming() && !minecraft.player.getAbilities().flying && !minecraft.player.isPassenger() && !minecraft.player.onClimbable()) {
                    minecraft.player.connection.send(new ServerboundPlayerCommandPacket(minecraft.player, ServerboundPlayerCommandPacket.Action.START_FALL_FLYING));
                }
            }
        });
    }

}
