package com.buuz135.salem.forge.datagen;

import com.buuz135.salem.item.TrinketItem;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(DataGenerator generator, String modid, ExistingFileHelper existingFileHelper) {
        super(generator, modid, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        TrinketItem.TRINKETS.values().forEach(trinketItem -> {
            getBuilder(trinketItem.getRegistryName().getPath())
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", modLoc("item/" + trinketItem.getRegistryName().getPath()));

        });
    }
}
