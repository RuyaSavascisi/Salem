package com.buuz135.salem.util;

import com.buuz135.salem.Salem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SalemTags {

    public static TagKey<EntityType<?>> RAID_ADD_COMMON = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Salem.MODID, "salem_raid_add_common"));
    public static TagKey<EntityType<?>> RAID_ADD_RARE = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Salem.MODID, "salem_raid_add_rare"));
    public static TagKey<EntityType<?>> RAID_ADD_EPIC = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Salem.MODID, "salem_raid_add_epic"));
    public static TagKey<EntityType<?>> RAID_ADD_LEGENDARY = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Salem.MODID, "salem_raid_add_legendary"));
}
