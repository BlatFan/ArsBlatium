package ru.blatfan.ars_blatium.items;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import ru.blatfan.ars_blatium.config.ArsArmorSetConfig;
import ru.blatfan.ars_blatium.init.ABRegistry;

public class ArsArmorSet {
    private final RegistryObject<Item> head;
    private final RegistryObject<Item> chest;
    private final RegistryObject<Item> legs;
    private final RegistryObject<Item> feet;
    private final String name;
    private final int tier;
    private final ArsArmorSetConfig config;

    public ArsArmorSet(ArsArmorSetConfig config, int tier) {
        this.config = config;
        this.name = config.name();
        this.tier = tier;
        this.feet = ABRegistry.R.item(name + "_boots", () -> new ArsArmorItem(config, ArmorItem.Type.BOOTS, tier));
        this.legs = ABRegistry.R.item(name + "_leggings", () -> new ArsArmorItem(config, ArmorItem.Type.LEGGINGS, tier));
        this.chest = ABRegistry.R.item(name + "_robes", () -> new ArsArmorItem(config, ArmorItem.Type.CHESTPLATE, tier));
        this.head = ABRegistry.R.item(name + "_hat", () -> new ArsArmorItem(config, ArmorItem.Type.HELMET, tier));
    }

    public ArsArmorSetConfig getConfig() {
        return this.config;
    }

    public String getName() {
        return this.name;
    }

    public int getTier() {
        return this.tier;
    }

    public Item getHat() {
        return head.get();
    }

    public Item getChest() {
        return chest.get();
    }

    public Item getLegs() {
        return legs.get();
    }

    public Item getBoots() {
        return feet.get();
    }

    public Item getArmorFromSlot(EquipmentSlot slot) {
        return switch (slot) {
            case CHEST -> getChest();
            case LEGS -> getLegs();
            case FEET -> getBoots();
            case HEAD -> getHat();
            default -> null;
        };
    }
}
