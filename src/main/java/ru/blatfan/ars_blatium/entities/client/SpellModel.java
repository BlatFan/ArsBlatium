package ru.blatfan.ars_blatium.entities.client;

import net.minecraft.resources.ResourceLocation;
import ru.blatfan.ars_blatium.ArsBlatium;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class SpellModel<T extends GeoAnimatable> extends GeoModel<T> {
    public String path;
    public ResourceLocation modelLocation;
    public ResourceLocation textLoc;
    public ResourceLocation animationLoc;
    public String textPathRoot;
    public String name;
    
    public SpellModel(String name) {
        this.textPathRoot = "spells";
        this.modelLocation = ArsBlatium.prefix("geo/" + name + ".geo.json");
        this.textLoc = ArsBlatium.prefix("textures/" + this.textPathRoot + "/" + name + ".png");
        this.animationLoc = ArsBlatium.prefix("animations/" + name + ".animation.json");
        this.name = name;
    }
    
    public SpellModel(String name, String textPath) {
        this(name);
        this.textPathRoot = textPath;
        this.textLoc = ArsBlatium.prefix("textures/" + this.textPathRoot + "/" + name + ".png");
    }
    
    public SpellModel<T> withEmptyAnim() {
        this.animationLoc = new ResourceLocation("ars_nouveau","animations/empty.json");
        return this;
    }
    
    public ResourceLocation getModelResource(T GeoAnimatable) {
        return this.modelLocation;
    }
    
    public ResourceLocation getTextureResource(T GeoAnimatable) {
        return this.textLoc;
    }
    
    public ResourceLocation getAnimationResource(T GeoAnimatable) {
        return this.animationLoc;
    }
}
