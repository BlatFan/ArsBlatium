package ru.blatfan.ars_blatium.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    
    public static ArsArmorSetConfig BLATIUM;
    public static ArsArmorSetConfig NLIUM;
    
    static {
        BUILDER.push("armor_config");
        BUILDER.comment("A world restart is needed after changing these config options.");
        BLATIUM = ArsArmorSetConfig.build(
            BUILDER,
            "blatium",
            new ArsArmorSetConfig.DefenseValues(6, 11, 9, 6, 5, 0),
            new ArsArmorSetConfig.ArcanistStats(325, 9, 9),
            new ArsArmorSetConfig.Capabilities(true, true, true, false, true, false, true)
        );
        
        NLIUM = ArsArmorSetConfig.build(
            BUILDER,
            "nlium",
            new ArsArmorSetConfig.DefenseValues(8, 13, 11, 8, 6, 0),
            new ArsArmorSetConfig.ArcanistStats(450, 12, 12),
            new ArsArmorSetConfig.Capabilities(true, true, true, true, true, true, true)
        );
        
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}