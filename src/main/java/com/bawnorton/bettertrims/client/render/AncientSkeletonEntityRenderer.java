package com.bawnorton.bettertrims.client.render;

import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.util.Identifier;

public final class AncientSkeletonEntityRenderer extends BipedEntityRenderer<AncientSkeletonEntity, AncientSkeletonEntityModel> {
    //? if >=1.21 {
    private static final Identifier TEXTURE = Identifier.ofVanilla("textures/entity/skeleton/skeleton.png");
    //?} else {
    /*private static final Identifier TEXTURE = new Identifier("textures/entity/skeleton/skeleton.png");
    *///?}

    public AncientSkeletonEntityRenderer(EntityRendererFactory.Context ctx) {
        super(ctx, new AncientSkeletonEntityModel(ctx.getPart(EntityModelLayers.SKELETON)), 0.5f);
        addFeature(new ArmorFeatureRenderer<>(
                this,
                new AncientSkeletonEntityModel(ctx.getPart(EntityModelLayers.SKELETON_INNER_ARMOR)),
                new AncientSkeletonEntityModel(ctx.getPart(EntityModelLayers.SKELETON_OUTER_ARMOR)),
                ctx.getModelManager()
        ));
    }

    @Override
    public Identifier getTexture(AncientSkeletonEntity entity) {
        return TEXTURE;
    }
}
