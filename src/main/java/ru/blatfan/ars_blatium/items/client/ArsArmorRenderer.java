package ru.blatfan.ars_blatium.items.client;

import com.hollingsworth.arsnouveau.client.renderer.tile.GenericModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import ru.blatfan.ars_blatium.items.ArsArmorItem;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

import java.util.Optional;

public class ArsArmorRenderer extends GeoArmorRenderer<ArsArmorItem> {
    public ArsArmorRenderer(GeoModel<ArsArmorItem> modelProvider) {
        super(modelProvider);
    }
    
    @Override
    public void renderRecursively(PoseStack poseStack, ArsArmorItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(bone.getName().equalsIgnoreCase("armorRightArmSlim") || bone.getName().equalsIgnoreCase("armorLeftArmSlim")){
            bone.setHidden(true);
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
    @Override
    public void actuallyRender(PoseStack poseStack, ArsArmorItem animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Optional<GeoBone> slimRight = model.getBone("armorRightArmSlim");
        Optional<GeoBone> slimLeft = model.getBone("armorLeftArmSlim");
        slimRight.ifPresent(geoBone -> geoBone.setHidden(true));
        slimLeft.ifPresent(geoBone -> geoBone.setHidden(true));
        model.getBone("armorRightArmSlim").ifPresent(geoBone -> geoBone.setHidden(true));
        model.getBone("armorLeftArmSlim").ifPresent(geoBone -> geoBone.setHidden(true));
        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
    
    @Override
    public ResourceLocation getTextureLocation(ArsArmorItem instance) {
        if(instance != null && model instanceof GenericModel<ArsArmorItem> genericModel){
            return genericModel.textLoc;
        }
        
        return super.getTextureLocation(instance);
    }
}
