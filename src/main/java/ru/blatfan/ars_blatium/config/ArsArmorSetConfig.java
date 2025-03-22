package ru.blatfan.ars_blatium.config;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraftforge.common.ForgeConfigSpec;
import ru.blatfan.ars_blatium.ArsBlatium;

import java.util.HashMap;
import java.util.UUID;

public record ArsArmorSetConfig(
    String name,
    ForgeConfigSpec.IntValue head,
    ForgeConfigSpec.IntValue chest,
    ForgeConfigSpec.IntValue legs,
    ForgeConfigSpec.IntValue boots,
    ForgeConfigSpec.IntValue toughness,
    ForgeConfigSpec.DoubleValue knockback,
    ForgeConfigSpec.IntValue maxMana,
    ForgeConfigSpec.DoubleValue manaRegen,
    ForgeConfigSpec.DoubleValue spellPower,
    ForgeConfigSpec.BooleanValue preventDrowning,
    ForgeConfigSpec.BooleanValue preventKinetic,
    ForgeConfigSpec.BooleanValue preventFire,
    ForgeConfigSpec.BooleanValue preventDragonsBreath,
    ForgeConfigSpec.BooleanValue preventWither,
    ForgeConfigSpec.BooleanValue preventLevitation,
    ForgeConfigSpec.BooleanValue preventFallDamage
) {
    public int getDefenseBySlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> head().get();
            case CHEST -> chest().get();
            case LEGS -> legs().get();
            case FEET -> boots().get();
            default -> throw new IllegalStateException("Unexpected value: " + slot);
        };
    }
    
    public static final HashMap<EquipmentSlot, UUID> ARMOR_MODIFIER_UUID_PER_TYPE = Util.make(new HashMap<>(), (map) -> {
        map.put(EquipmentSlot.FEET, UUID.fromString("845DB27C-C624-495F-8C9F-6020A9A58B6B"));
        map.put(EquipmentSlot.LEGS, UUID.fromString("D8499B04-0E66-4726-AB29-64469D734E0D"));
        map.put(EquipmentSlot.CHEST, UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E"));
        map.put(EquipmentSlot.HEAD, UUID.fromString("2AD3F246-FEE1-4E67-B886-69FD380BB150"));
    });
    
    public Multimap<Attribute, AttributeModifier> buildAttributeMap(ArmorItem item) {
        EquipmentSlot slot = item.getEquipmentSlot();
        
        int defense = getDefenseBySlot(slot);
        int toughness = toughness().get();
        double knockback = knockback().get();
        double maxMana = maxMana().get();
        double manaRegen = manaRegen().get();
        double spellPower = spellPower().get();
        
        ResourceLocation modifier = ArsBlatium.prefix("armor." + name() + "." + slot.getName());
        
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = new ImmutableMultimap.Builder<>();
        
        UUID uuid = ARMOR_MODIFIER_UUID_PER_TYPE.get(item.getEquipmentSlot());
        
        if (defense != 0) {
            builder.put(Attributes.ARMOR, new AttributeModifier(uuid, modifier.toString(), defense, AttributeModifier.Operation.ADDITION));
        }
        if (toughness != 0) {
            builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, modifier.toString(), toughness, AttributeModifier.Operation.ADDITION));
        }
        if (knockback != 0) {
            builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, modifier.toString(), knockback, AttributeModifier.Operation.ADDITION));
        }
        if (maxMana != 0) {
            builder.put(PerkAttributes.MAX_MANA.get(), new AttributeModifier(uuid, modifier.toString(), maxMana, AttributeModifier.Operation.ADDITION));
        }
        if (manaRegen != 0) {
            builder.put(PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, modifier.toString(), manaRegen, AttributeModifier.Operation.ADDITION));
        }
        if (spellPower != 0) {
            builder.put(PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid, modifier.toString(), spellPower, AttributeModifier.Operation.ADDITION));
        }
        
        return builder.build();
    }
    
    public static ArsArmorSetConfig build(ForgeConfigSpec.Builder builder, String name, DefenseValues values, ArcanistStats stats, Capabilities capabilities) {
        String localizedName = name.substring(0, 1).toUpperCase() + name.substring(1);
        builder.push(name);
        builder.comment("Config for " + localizedName + " armor");
        
        builder.push("armor_values");
        ForgeConfigSpec.IntValue head = builder.worldRestart().comment("Hat").defineInRange("hat", values.head(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.IntValue chest = builder.worldRestart().comment("Robes").defineInRange("robes", values.chest(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.IntValue legs = builder.worldRestart().comment("Leggings").defineInRange("leggings", values.legs(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.IntValue boots = builder.worldRestart().comment("Boots").defineInRange("boots", values.boots(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.IntValue toughness = builder.worldRestart().comment("Toughness").defineInRange("toughness", values.toughness(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.DoubleValue knockback = builder.worldRestart().comment("Knockback Resistance").defineInRange("knockback_resistance", values.knockbackResistance(), 0D, Integer.MAX_VALUE);
        builder.pop();
        
        builder.push("arcanist_stats");
        ForgeConfigSpec.IntValue manaBoost = builder.worldRestart().comment("Max Mana Bonus").defineInRange("mana_boost", stats.manaBoost(), 0, Integer.MAX_VALUE);
        ForgeConfigSpec.DoubleValue manaRegen = builder.worldRestart().comment("Mana Regen Bonus").defineInRange("mana_regen", stats.manaRegen(), 0D, Integer.MAX_VALUE);
        ForgeConfigSpec.DoubleValue spellPower = builder.worldRestart().comment("Spell Power Bonus").defineInRange("spell_power", stats.spellPower(), 0D, Integer.MAX_VALUE);
        builder.pop();
        
        builder.push("capabilities");
        ForgeConfigSpec.BooleanValue preventDrowning = builder.worldRestart().comment("Should Helmet Prevent Drowning?").define("prevent_drowning", capabilities.preventDrowning());
        ForgeConfigSpec.BooleanValue preventKinetic = builder.worldRestart().comment("Should Helmet Prevent Kinetic Damage?").define("prevent_kinetic", capabilities.preventKinetic());
        ForgeConfigSpec.BooleanValue preventFire = builder.worldRestart().comment("Should Chestplate Prevent Fire Damage?").define("prevent_fire", capabilities.preventFire());
        ForgeConfigSpec.BooleanValue preventDragonsBreath = builder.worldRestart().comment("Should Chestplate Prevent Dragon's Breath Damage?").define("prevent_dragons_breath", capabilities.preventDragonsBreath());
        ForgeConfigSpec.BooleanValue preventWither = builder.worldRestart().comment("Should Leggings Prevent Wither?").define("prevent_wither", capabilities.preventWither());
        ForgeConfigSpec.BooleanValue preventLevitation = builder.worldRestart().comment("Should Leggings Prevent Levitation?").define("prevent_levitation", capabilities.preventLevitation());
        ForgeConfigSpec.BooleanValue preventFallDamage = builder.worldRestart().comment("Should Boots Prevent Fall Damage?").define("prevent_fall_damage", capabilities.preventFallDamage());
        builder.pop();
        
        ArsArmorSetConfig config = new ArsArmorSetConfig(name,
            head, chest, legs, boots, toughness, knockback,
            manaBoost, manaRegen, spellPower,
            preventDrowning, preventKinetic, preventFire, preventDragonsBreath, preventWither, preventLevitation, preventFallDamage
        );
        builder.pop();
        return config;
    }
    
    public record DefenseValues(int head, int chest, int legs, int boots, int toughness, double knockbackResistance) {};
    public record ArcanistStats(int manaBoost, double manaRegen, double spellPower) {};
    public record Capabilities(boolean preventDrowning, boolean preventKinetic, boolean preventFire, boolean preventDragonsBreath, boolean preventWither, boolean preventLevitation, boolean preventFallDamage) {};
}