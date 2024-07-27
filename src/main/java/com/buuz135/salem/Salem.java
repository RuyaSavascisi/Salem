package com.buuz135.salem;

import com.buuz135.salem.command.SalemRaidCommand;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

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
            }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public Salem(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);
        SalemContent.ITEMS.register(modEventBus);
        SalemContent.DataComp.DATA_COMPONENTS.register(modEventBus);
        SalemContent.Effect.EFFECT.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(new CommonEvents());


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    @SubscribeEvent
    public void onRegisterCommand(RegisterCommandsEvent event)
    {
        SalemRaidCommand.register(event.getDispatcher());
    }

}
