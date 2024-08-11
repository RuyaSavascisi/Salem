package com.buuz135.salem;

import net.neoforged.neoforge.common.ModConfigSpec;


public class Config {

    private static String TRANSLATION_KEY_BASE = "config." + Salem.MODID + ".";

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue ENABLE_RANDOM_RAIDS = BUILDER
            .comment("Enable random raids around players similar at how phantoms spawn")
            .translation(TRANSLATION_KEY_BASE + "enable_random_raids")
            .define("ENABLE_RANDOM_RAIDS", false);

    static final ModConfigSpec SPEC = BUILDER.build();

}
