package ru.blatfan.ars_blatium.init;

import com.hollingsworth.arsnouveau.ArsNouveau;
import com.hollingsworth.arsnouveau.setup.registry.CreativeTabRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;
import ru.blatfan.ars_blatium.ArsBlatium;
import ru.blatfan.ars_blatium.config.ArsArmorSetConfig;
import ru.blatfan.ars_blatium.config.Config;
import ru.blatfan.ars_blatium.entities.BlatiumHoleEntity;
import ru.blatfan.ars_blatium.items.ArsArmorSet;
import ru.blatfan.ars_blatium.items.BlatiumSpellBook;
import ru.blatfan.ars_blatium.spells.BlatiumGlyphes;
import ru.blatfan.blatapi.common.registry.BlatRegister;
import ru.blatfan.blatapi.fluffy_fur.util.IntegrationUtil;

public class ABRegistry {
    public static final BlatRegister R = new BlatRegister(ArsBlatium.MODID);
    
    public static final ArsArmorSet BLATIUM = new ArsArmorSet(Config.BLATIUM, 4);
    public static final RegistryObject<Item> BLATIUM_SPELL_BOOK = R.item("blatium_spell_book", ()-> new BlatiumSpellBook(ArsBlatium.BLATIUM));
    public static final ArsArmorSet NLIUM = new ArsArmorSet(Config.NLIUM, 6);
    public static final RegistryObject<Item> NLIUM_SPELL_BOOK = R.item("nlium_spell_book", ()-> new BlatiumSpellBook(ArsBlatium.NLIUM));
    
    public static final RegistryObject<EntityType<BlatiumHoleEntity>> HOLE = ABRegistry.R.entity_type("blatium_hole", MobCategory.MISC, 2, 2, 10, BlatiumHoleEntity::new);
    
    public static final RegistryObject<CreativeModeTab> TAB = R.creative_mode_tab("arcanist_tab", () ->
        CreativeModeTab.builder()
            .withTabsBefore(CreativeTabRegistry.GLYPHS.getKey())
            .title(Component.translatable("itemGroup.ars_blatium"))
            .icon(() -> BLATIUM.getHat().getDefaultInstance())
            .displayItems((parameters, output) -> {
                R.ITEMS.getEntries().stream().map(item -> item.get().getDefaultInstance()).forEach(output::accept);
                output.accept(IntegrationUtil.getItem(ArsNouveau.MODID, BlatiumGlyphes.hole));
                output.accept(IntegrationUtil.getItem(ArsNouveau.MODID, BlatiumGlyphes.small_hole));
            })
            .build()
    );
}
