package me.jungdab.jdturrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jungdab.jdturrets.client.model.BasicTurretModel;
import me.jungdab.jdturrets.entity.BasicTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BasicTurretRenderer extends GeoEntityRenderer<BasicTurret> {
    public BasicTurretRenderer(EntityRendererProvider.Context context) {
        super(context, new BasicTurretModel());
    }

    @Override
    protected void applyRotations(BasicTurret animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
    }

    @Override
    public int getPackedOverlay(BasicTurret animatable, float u, float partialTick) {
        if(animatable.hurtTime > 0 || animatable.deathTime > 0) return OverlayTexture.pack(0.3f, false);
        else return OverlayTexture.NO_OVERLAY;
    }
}
