package com.buuz135.salem;

import com.buuz135.salem.item.DeathlyChargersFeetItem;
import com.buuz135.salem.mob_effects.SalemMobEffects;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class SalemContent {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Salem.MODID);


    public static DeferredItem<Item> DEATHLY_CHARGERS_FEET = ITEMS.registerItem("deathly_chargers_feet", properties -> new DeathlyChargersFeetItem());


    public static class DataComp{

        public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Salem.MODID);


        public static Supplier<DataComponentType<Long>> TIME = register("time", () -> -1L, op -> op.persistent(Codec.LONG));

    }

    public static class Effect {

        public static final DeferredRegister<MobEffect> EFFECT = DeferredRegister.create(Registries.MOB_EFFECT, Salem.MODID);
        public static ResourceLocation ENLARGE_ATTRIBUTE = ResourceLocation.fromNamespaceAndPath(Salem.MODID, "enlarge");

        public static DeferredHolder<MobEffect, MobEffect> ENLARGE_EFFECT = EFFECT.register("enlarge", () -> new SalemMobEffects(MobEffectCategory.BENEFICIAL, 3402751)
                .addAttributeModifier(
                        Attributes.SCALE, ENLARGE_ATTRIBUTE, 0.2F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                ));
        public static DeferredHolder<MobEffect, MobEffect> SPAWN_EFFECT = EFFECT.register("spawn", () -> new SalemMobEffects(MobEffectCategory.BENEFICIAL, 3402751));
    }


    private static <T> ComponentSupplier<T> register(String name, Supplier<T> defaultVal, UnaryOperator<DataComponentType.Builder<T>> op) {
        var registered = DataComp.DATA_COMPONENTS.register(name, () -> op.apply(DataComponentType.builder()).build());
        return new ComponentSupplier<>(registered, defaultVal);
    }

    public static class ComponentSupplier<T> implements Supplier<DataComponentType<T>> {
        private final Supplier<DataComponentType<T>> type;
        private final Supplier<T> defaultSupplier;

        public ComponentSupplier(Supplier<DataComponentType<T>> type, Supplier<T> defaultSupplier) {
            this.type = type;
            this.defaultSupplier = Suppliers.memoize(defaultSupplier::get);
        }

        public T get(ItemStack stack) {
            return stack.getOrDefault(type, defaultSupplier.get());
        }

        @Override
        public DataComponentType<T> get() {
            return type.get();
        }
    }

}
