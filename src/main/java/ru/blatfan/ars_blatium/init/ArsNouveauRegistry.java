package ru.blatfan.ars_blatium.init;

import com.hollingsworth.arsnouveau.api.perk.ArmorPerkHolder;
import com.hollingsworth.arsnouveau.api.perk.PerkSlot;
import com.hollingsworth.arsnouveau.api.registry.PerkRegistry;
import com.hollingsworth.arsnouveau.setup.registry.APIRegistry;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import ru.blatfan.ars_blatium.items.ArsArmorSet;
import ru.blatfan.ars_blatium.spells.BlackHoleEffect;
import ru.blatfan.ars_blatium.spells.SmallBlackHoleEffect;

import java.util.Arrays;
import java.util.List;

public class ArsNouveauRegistry {
    public static void postInit() {
        addPerkSlots();
    }
    
    public static void spells(){
        APIRegistry.registerSpell(BlackHoleEffect.INSTANCE);
        APIRegistry.registerSpell(SmallBlackHoleEffect.INSTANCE);
    }

    private static void addPerkSlots() {
        ArsArmorSet[] arcanistArmorSets = {ABRegistry.BLATIUM, ABRegistry.NLIUM};
        List<PerkSlot> t5 = Arrays.asList(PerkSlot.ONE, PerkSlot.TWO, PerkSlot.THREE);
        List<PerkSlot> t6 = Arrays.asList(PerkSlot.TWO, PerkSlot.THREE, PerkSlot.THREE);
        List<PerkSlot> t7 = Arrays.asList(PerkSlot.THREE, PerkSlot.THREE, PerkSlot.THREE);
        List<PerkSlot> empty = List.of();
        for (ArsArmorSet arcanistArmorSet : arcanistArmorSets) {
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                Item item = arcanistArmorSet.getArmorFromSlot(slot);
                if (item == null) continue;
                PerkRegistry.registerPerkProvider(item, stack -> {
                    ArmorPerkHolder holder = new ArmorPerkHolder(stack, List.of(empty, empty, empty, empty, t5, t6, t7));
                    holder.setTier(arcanistArmorSet.getTier());
                    return holder;
                });
            }
        }
    }
}
