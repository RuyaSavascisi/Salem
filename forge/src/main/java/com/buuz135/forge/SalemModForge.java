package com.buuz135.forge;

import com.buuz135.SalemMod;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SalemMod.MOD_ID)
public class SalemModForge {
    public SalemModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SalemMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        SalemMod.init();
    }
}
