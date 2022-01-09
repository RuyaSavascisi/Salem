package com.buuz135.salem.fabric;

import com.buuz135.salem.SalemModClient;
import net.fabricmc.api.ClientModInitializer;

public class SalemModClientFabric implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new SalemModClient();
    }
}
