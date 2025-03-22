package ru.blatfan.ars_blatium.items.client;

import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import ru.blatfan.ars_blatium.ArsBlatium;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class GenericArmorModel<T extends GeoAnimatable> extends GenericModel<T> {
    public GenericArmorModel(String name) {
        super(name);
        this.textPathRoot = "item";
        this.modelLocation = ArsBlatium.prefix("geo/armor.geo.json");
        this.textLoc = ArsBlatium.prefix("textures/armor/" + name + ".png");
        this.animationLoc = ArsBlatium.prefix("animations/" + name + "_animations.json");
        this.name = name;
    }
}
