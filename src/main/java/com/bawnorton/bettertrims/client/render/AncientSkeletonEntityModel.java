package com.bawnorton.bettertrims.client.render;

import com.bawnorton.bettertrims.entity.AncientSkeletonEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

public final class AncientSkeletonEntityModel extends BipedEntityModel<AncientSkeletonEntity> {
    public AncientSkeletonEntityModel(ModelPart root) {
        super(root);
    }

    public void animateModel(AncientSkeletonEntity ancientSkeleton, float limbAngle, float limbDistance, float tickDelta) {
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        super.animateModel(ancientSkeleton, limbAngle, limbDistance, tickDelta);
    }

    public void setAngles(AncientSkeletonEntity ancientSkeleton, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        super.setAngles(ancientSkeleton, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
        if (ancientSkeleton.isAttacking()) {
            float k = MathHelper.sin(this.handSwingProgress * (float) Math.PI);
            float l = MathHelper.sin((1.0F - (1.0F - this.handSwingProgress) * (1.0F - this.handSwingProgress)) * (float) Math.PI);
            this.rightArm.roll = 0.0F;
            this.leftArm.roll = 0.0F;
            this.rightArm.yaw = -(0.1F - k * 0.6F);
            this.leftArm.yaw = 0.1F - k * 0.6F;
            this.rightArm.pitch = (float) (-Math.PI / 2);
            this.leftArm.pitch = (float) (-Math.PI / 2);
            this.rightArm.pitch -= k * 1.2F - l * 0.4F;
            this.leftArm.pitch -= k * 1.2F - l * 0.4F;
            CrossbowPosing.swingArms(this.rightArm, this.leftArm, animationProgress);
        }
    }

    @Override
    public void setArmAngle(Arm arm, MatrixStack matrices) {
        float f = arm == Arm.RIGHT ? 1.0F : -1.0F;
        ModelPart modelPart = this.getArm(arm);
        modelPart.pivotX += f;
        modelPart.rotate(matrices);
        modelPart.pivotX -= f;
    }
}
