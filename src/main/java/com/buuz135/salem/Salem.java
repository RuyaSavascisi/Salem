package com.buuz135.salem;

import com.buuz135.salem.command.SalemRaidCommand;
import com.buuz135.salem.util.InventoryFinderUtil;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Optional;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Salem.MODID)
public class Salem
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "salem";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "salem" namespace

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    // Creates a creative tab with the id "salem:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("salem_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.salem"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> SalemContent.DEATHLY_CHARGERS_FEET.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(SalemContent.DEATHLY_CHARGERS_FEET.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(SalemContent.BONE_SHIELD_HANDS.get());
                output.accept(SalemContent.CHILLING_AURA_NECKLACE.get());
                output.accept(SalemContent.ETERNAL_FEAST_BELT.get());
                output.accept(SalemContent.NIGHTMARISH_WINGS_BACK.get());
                output.accept(SalemContent.SCORCHING_AURA_RING.get());
                output.accept(SalemContent.HELLISH_BARGAIN_RING.get());
                output.accept(SalemContent.TOME_OF_THE_DAMNED_CHARM.get());
                output.accept(SalemContent.UNHALLOWED_CROSS.get());
                output.accept(SalemContent.WITHERING_TOUCH_HAND.get());
                output.accept(SalemContent.COMMON_RAID_SUMMONER.get());
                output.accept(SalemContent.RARE_RAID_SUMMONER.get());
                output.accept(SalemContent.EPIC_RAID_SUMMONER.get());
                output.accept(SalemContent.LEGENDARY_RAID_SUMMONER.get());
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Salem(IEventBus modEventBus, ModContainer modContainer)
    {
        SalemContent.ITEMS.register(modEventBus);
        SalemContent.DataComp.DATA_COMPONENTS.register(modEventBus);
        SalemContent.Effect.EFFECT.register(modEventBus);
        modEventBus.addListener(RegisterCapabilitiesEvent.class, this::registerCapabilities);

        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new CommonEvents());


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        if (FMLEnvironment.dist == Dist.CLIENT){
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        if (ModList.get().isLoaded("curios")) {
            InventoryFinderUtil.FINDERS.add(new InventoryFinderUtil((livingEntity, item) ->{
                Optional<ICuriosItemHandler> maybeCuriosInventory = CuriosApi.getCuriosInventory(livingEntity);
                return maybeCuriosInventory.map(iCuriosItemHandler -> {
                    for (ICurioStacksHandler value : iCuriosItemHandler.getCurios().values()) {
                        if (value != null) {
                            for (int i = 0; i < value.getStacks().getSlots(); i++) {
                                if (value.getStacks().getStackInSlot(i).is(item)) {
                                    return value.getStacks().getStackInSlot(i);
                                }
                            }
                        }
                    }
                    return ItemStack.EMPTY;
                }).orElse(ItemStack.EMPTY);
            }));
        }
    }

    @SubscribeEvent
    public void onRegisterCommand(RegisterCommandsEvent event)
    {
        SalemRaidCommand.register(event.getDispatcher());
    }


    public void registerCapabilities(RegisterCapabilitiesEvent event){
        if (ModList.get().isLoaded("curios")){
            event.registerItem(CuriosCapability.ITEM, (stack, unused) -> new ICurio(){

                @Override
                public ItemStack getStack() {
                    return stack;
                }


                @Override
                public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, ResourceLocation id) {
                    Multimap<Holder<Attribute>, AttributeModifier> modifierMultimap = LinkedHashMultimap.create();
                    modifierMultimap.put(Attributes.MOVEMENT_SPEED, new AttributeModifier(ResourceLocation.fromNamespaceAndPath(Salem.MODID, "deathly_chargers"), 0.1 + stack.getOrDefault(SalemContent.DataComp.TIME, 0L) * 0.1D, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
                    return modifierMultimap;
                }
            }, SalemContent.DEATHLY_CHARGERS_FEET);
        }
    }

}
