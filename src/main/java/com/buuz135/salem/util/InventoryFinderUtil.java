package com.buuz135.salem.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class InventoryFinderUtil {

    public static List<InventoryFinderUtil> FINDERS = new ArrayList<>();

    static {
        FINDERS.add(new InventoryFinderUtil((player, item) -> {
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.getItem().equals(item)){
                    return itemStack;
                }
            }
            return ItemStack.EMPTY;
        }));
    }

    public static ItemStack findFirst(Player player, Item item){
        for (InventoryFinderUtil finder : FINDERS) {
            ItemStack stack = finder.getItemFinder().apply(player, item);
            if (!stack.isEmpty()){
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private final BiFunction<Player, Item, ItemStack> itemFinder;

    public InventoryFinderUtil(BiFunction<Player, Item, ItemStack>  itemFinder) {
        this.itemFinder = itemFinder;
    }

    public BiFunction<Player, Item, ItemStack>  getItemFinder() {
        return itemFinder;
    }
}
