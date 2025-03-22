package ru.blatfan.ars_blatium.items.client;

import com.hollingsworth.arsnouveau.client.renderer.item.SpellBookRenderer;
import com.hollingsworth.arsnouveau.common.items.SpellBook;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.ars_blatium.ArsBlatium;

public class BlatiumSpellBookRenderer extends SpellBookRenderer {
    @Override
    public ResourceLocation getTextureLocation(SpellBook o) {
        if(o.getTier()== ArsBlatium.BLATIUM) return (ArsBlatium.prefix("textures/item/blatium_spellbook.png"));
        if(o.getTier()== ArsBlatium.NLIUM) return (ArsBlatium.prefix("textures/item/nlium_spellbook.png"));
        return super.getTextureLocation(o);
    }
}
