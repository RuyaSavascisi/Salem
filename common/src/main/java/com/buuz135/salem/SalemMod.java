package com.buuz135.salem;

import com.buuz135.salem.command.SalemRaidCommand;
import com.buuz135.salem.effect.EnlargeEffect;
import com.buuz135.salem.effect.SalemMobEffect;
import com.buuz135.salem.item.*;
import com.buuz135.salem.world.SalemRaidSavedData;
import com.google.common.base.Suppliers;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.hooks.tags.TagHooks;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.entity.EntityTypeTest;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.function.Supplier;

public class SalemMod {
    public static final String MOD_ID = "salem";
    // We can use this if we don't want to use DeferredRegister
    public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
    // Registering a new creative tab
    public static final CreativeModeTab TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "salem_tab"), () ->
            new ItemStack(SalemMod.TOME_OF_THE_DAMNED.get()));
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);

    public static final RegistrySupplier<Item> TOME_OF_THE_DAMNED = ITEMS.register("tome_of_the_damned_charm", () -> new TomeOfTheDamnedItem(SalemRaidSavedData.SalemRaidTier.LEGENDARY,Rarity.EPIC));
    public static final RegistrySupplier<Item> UNHALLOWED_CROSS = ITEMS.register("unhallowed_cross_charm", () -> new UnhallowedCrossItem(SalemRaidSavedData.SalemRaidTier.LEGENDARY,Rarity.EPIC));
    public static final RegistrySupplier<Item> NIGHTMARISH_WINGS = ITEMS.register("nightmarish_wings_back", () -> new TrinketItem(SalemRaidSavedData.SalemRaidTier.EPIC,Rarity.RARE));
    public static final RegistrySupplier<Item> DEAHTLY_CHARGERS = ITEMS.register("deathly_chargers_feet", () -> new DeathlyChargersItem(SalemRaidSavedData.SalemRaidTier.EPIC,Rarity.RARE));
    public static final RegistrySupplier<Item> WITHERING_TOUCH = ITEMS.register("withering_touch_hand", () -> new WitheringTouchItem(SalemRaidSavedData.SalemRaidTier.EPIC,Rarity.RARE));
    public static final RegistrySupplier<Item> CHILLING_AURA = ITEMS.register("chilling_aura_necklace", () -> new ChillingAuraItem(SalemRaidSavedData.SalemRaidTier.RARE,Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> SCORCHING_AURA = ITEMS.register("scorching_aura_ring", () -> new ScorchingAuraItem(SalemRaidSavedData.SalemRaidTier.RARE,Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> HELLISH_BARGAIN = ITEMS.register("hellish_bargain_ring", () -> new TrinketItem(SalemRaidSavedData.SalemRaidTier.RARE,Rarity.UNCOMMON));
    public static final RegistrySupplier<Item> BONE_SHIELD = ITEMS.register("bone_shield_hand", () -> new BoneShieldItem(SalemRaidSavedData.SalemRaidTier.COMMON,Rarity.COMMON));
    public static final RegistrySupplier<Item> ETERNAL_FEAST = ITEMS.register("eternal_feast_belt", () -> new EternalFeastItem(SalemRaidSavedData.SalemRaidTier.COMMON, Rarity.COMMON));

    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(MOD_ID, Registry.ATTRIBUTE_REGISTRY);

    public static final RegistrySupplier<Attribute> ENLARGE_ATTRIBUTE = ATTRIBUTES.register("enlarge", () -> new RangedAttribute("salem.attribute.name.generic.enlarge", 1D, 0.1D, 1024.0D).setSyncable(true));

    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(MOD_ID, Registry.MOB_EFFECT_REGISTRY);

    public static final RegistrySupplier<MobEffect> SALEM_SPAWN = MOB_EFFECTS.register("salem_spawn", () -> new SalemMobEffect(MobEffectCategory.NEUTRAL, Color.RED.getRGB()));
    public static final RegistrySupplier<MobEffect> ENLARGE_SPAWN = MOB_EFFECTS.register("enlarge", () -> new EnlargeEffect(MobEffectCategory.NEUTRAL, Color.GREEN.getRGB())
            .addAttributeModifier(ENLARGE_ATTRIBUTE.get(), "91AEAA56-376B-4498-0135-2F7F68070635", 0.1f,  AttributeModifier.Operation.ADDITION));

    public static Tag.Named<EntityType<?>> RAID_ADD_COMMON = TagHooks.optionalEntityType(new ResourceLocation(MOD_ID, "salem_raid_add_common"));
    public static Tag.Named<EntityType<?>> RAID_ADD_RARE = TagHooks.optionalEntityType(new ResourceLocation(MOD_ID, "salem_raid_add_rare"));
    public static Tag.Named<EntityType<?>> RAID_ADD_EPIC = TagHooks.optionalEntityType(new ResourceLocation(MOD_ID, "salem_raid_add_epic"));
    public static Tag.Named<EntityType<?>> RAID_ADD_LEGENDARY = TagHooks.optionalEntityType(new ResourceLocation(MOD_ID, "salem_raid_add_legendary"));


    public static HashMap<String, SalemRaidSpawner> RAID_SPAWNER = new LinkedHashMap<>();

    public static void init() {
        ITEMS.register();
        ATTRIBUTES.register();
        MOB_EFFECTS.register();

        EntityEvent.LIVING_HURT.register((livingEntity, damageSource, v) -> {
            if (livingEntity.hasEffect(SALEM_SPAWN.get())){
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        });
        TickEvent.SERVER_LEVEL_POST.register(level -> {
            level.getEntities(EntityTypeTest.forClass(Mob.class),mob -> mob.hasEffect(SALEM_SPAWN.get())).forEach(SalemMod::salemSpawnCheck);
            SalemRaidSavedData.getData(level).tick();
            RAID_SPAWNER.computeIfAbsent(level.dimension().location().toString(), s -> new SalemRaidSpawner()).tick(level);
        });
        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandSelection) -> {
            SalemRaidCommand.register(commandDispatcher);
        });
    }

    public static void salemSpawnCheck(Mob trackedMonster){
        BlockPos pos = new BlockPos(trackedMonster.getX(), trackedMonster.getY(), trackedMonster.getZ());
        if (trackedMonster.level.getBlockState(pos).isAir()){
            trackedMonster.removeEffect(SALEM_SPAWN.get());
            trackedMonster.setNoAi(false);
        }else {
            trackedMonster.setNoAi(true);
            trackedMonster.setPos(trackedMonster.getX(), trackedMonster.getY() + 0.05, trackedMonster.getZ());
            if (trackedMonster.level instanceof ServerLevel){
                if (trackedMonster.level.random.nextBoolean()){
                    for (int i = 0; i < 5; i++) {
                        BlockPos checkedPos = new BlockPos(trackedMonster.getX(), trackedMonster.getY() + i, trackedMonster.getZ());
                        if (trackedMonster.level.getBlockState(checkedPos.above()).isAir()){
                            ((ServerLevel)trackedMonster.level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, trackedMonster.level.getBlockState(checkedPos)), trackedMonster.getX(), trackedMonster.getBlockY() + i + 1, trackedMonster.getZ(), 10,0.1,0,0.1,0);
                            if (trackedMonster.level.random.nextBoolean()) ((ServerLevel)trackedMonster.level).playSound(null, trackedMonster.getX(), trackedMonster.getBlockY() + i + 1, trackedMonster.getZ(), SoundEvents.GRASS_BREAK, SoundSource.BLOCKS,1 , 1);
                            break;
                        }
                    }
                }
            }
        }
    }
}
