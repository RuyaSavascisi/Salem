package com.buuz135.fabric;

import com.buuz135.SalemMod;
import net.fabricmc.api.ModInitializer;

public class SalemModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SalemMod.init();
    }
}
