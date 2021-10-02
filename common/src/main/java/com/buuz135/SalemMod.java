package com.buuz135;

import com.buuz135.item.TrinketItem;
import com.google.common.base.Suppliers;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

import java.util.function.Supplier;

public class SalemMod {
    public static final String MOD_ID = "salem";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "salem_tab"), () ->
            new ItemStack(SalemMod.TOME_OF_THE_DAMNED.get()));
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> TOME_OF_THE_DAMNED = ITEMS.register("tome_of_the_damned", () -> new TrinketItem(Rarity.EPIC));
    public static final RegistrySupplier<Item> UNHALLOWED_CROSS = ITEMS.register("unhallowed_cross", () -> new TrinketItem(Rarity.EPIC));
    public static final RegistrySupplier<Item> NIGHTMARISH_WINGS = ITEMS.register("nightmarish_wings", () -> new TrinketItem(Rarity.RARE));
    public static final RegistrySupplier<Item> DEAHTLY_CHARGERS = ITEMS.register("deathly_chargers", () -> new TrinketItem(Rarity.RARE));
    public static final RegistrySupplier<Item> CHILLING_AURA = ITEMS.register("chilling_aura", () -> new TrinketItem(Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> SCORCHING_AURA = ITEMS.register("scorching_aura", () -> new TrinketItem(Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> HELLISH_BARGAIN = ITEMS.register("hellish_bargain", () -> new TrinketItem(Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> BONE_SHIELD = ITEMS.register("bone_shield", () -> new TrinketItem(Rarity.COMMON));
    public static final RegistrySupplier<Item> ETERNAL_FEAST = ITEMS.register("eternal_feast", () -> new TrinketItem(Rarity.COMMON));


    public static void init() {
        ITEMS.register();
        

    }
}
