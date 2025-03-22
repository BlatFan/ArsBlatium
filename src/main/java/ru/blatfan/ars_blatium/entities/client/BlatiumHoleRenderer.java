package ru.blatfan.ars_blatium.entities.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import ru.blatfan.ars_blatium.entities.BlatiumHoleEntity;
import ru.blatfan.blatapi.fluffy_fur.client.render.RenderBuilder;
import ru.blatfan.blatapi.fluffy_fur.registry.client.FluffyFurRenderTypes;
import ru.blatfan.blatapi.utils.ColorHelper;
import ru.blatfan.blatium.Blatium;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BlatiumHoleRenderer extends GeoEntityRenderer<BlatiumHoleEntity> {
    public BlatiumHoleRenderer(EntityRendererProvider.Context p_174008_) {
        super(p_174008_, new SpellModel<BlatiumHoleEntity>("blatium_hole").withEmptyAnim());
    }
    
    @Override
    public void render(BlatiumHoleEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        withScale(entity.time()/10f);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        RenderBuilder.create()
            .replaceBufferSource(bufferSource)
            .setRenderType(FluffyFurRenderTypes.ADDITIVE)
            .setLight(15)
            .setColor(ColorHelper.getColor(Blatium.COLOR_BLATIUM))
            .renderDragon(poseStack, 0, 1.4, 0, 5, partialTick, entity.getName().getString().length())
            .renderDragon(poseStack, 0, 1.4, 0, 5, partialTick+100, entity.getName().getString().length())
            .endBatch();
    }
    
    @Override
    protected int getBlockLightLevel(BlatiumHoleEntity p_114496_, BlockPos p_114497_) {
        return 15;
    }
}
