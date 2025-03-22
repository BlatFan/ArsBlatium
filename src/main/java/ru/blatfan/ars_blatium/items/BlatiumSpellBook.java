package ru.blatfan.ars_blatium.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.hollingsworth.arsnouveau.api.perk.PerkAttributes;
import com.hollingsworth.arsnouveau.api.spell.SpellTier;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import com.hollingsworth.arsnouveau.setup.registry.ItemsRegistry;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import ru.blatfan.ars_blatium.ArsBlatium;
import ru.blatfan.ars_blatium.items.client.BlatiumSpellBookRenderer;
import ru.blatfan.blatium.Blatium;

import java.util.UUID;
import java.util.function.Consumer;

public class BlatiumSpellBook extends SpellBook {
    public BlatiumSpellBook(SpellTier tier) {
        super(
            ItemsRegistry.defaultItemProperties()
                .stacksTo(1)
                .fireResistant()
                .rarity(tier==ArsBlatium.BLATIUM ? Blatium.RARITY_BLATIUM : Blatium.RARITY_NLIUM), tier);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> d = ImmutableMultimap.builder();
        d.putAll(getDefaultAttributeModifiers(slot));
        if (slot != EquipmentSlot.MAINHAND)
            return d.build();
        
        UUID uuid = UUID.fromString("920a3362-e7e7-4206-a72d-7c68475cc80a");
        ResourceLocation modifier = ArsBlatium.prefix("book." + getTier().id.getPath());
        int v = getTier().value/10;
        d.put(PerkAttributes.MANA_REGEN_BONUS.get(), new AttributeModifier(uuid, modifier.toString(), 20*v, AttributeModifier.Operation.ADDITION));
        d.put(PerkAttributes.SPELL_DAMAGE_BONUS.get(), new AttributeModifier(uuid, modifier.toString(), 20*v, AttributeModifier.Operation.ADDITION));
        
        return d.build();
    }
    
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new BlatiumSpellBookRenderer();
            
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return this.renderer;
            }
        });
    }
}
