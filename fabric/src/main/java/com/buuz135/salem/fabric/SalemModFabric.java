package com.buuz135.salem.fabric;

import com.buuz135.salem.SalemMod;
import net.fabricmc.api.ModInitializer;

public class SalemModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        SalemMod.init();
    }
}
