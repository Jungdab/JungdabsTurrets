package me.jungdab.jdturrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jungdab.jdturrets.client.model.SniperTurretModel;
import me.jungdab.jdturrets.client.model.TankTurretModel;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TankTurretRenderer extends GeoEntityRenderer<TankTurret> {
    public TankTurretRenderer(EntityRendererProvider.Context context) {
        super(context, new TankTurretModel());
    }

    @Override
    protected void applyRotations(TankTurret animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
    }

    @Override
    public int getPackedOverlay(TankTurret animatable, float u, float partialTick) {
        if(animatable.hurtTime > 0 || animatable.deathTime > 0) return OverlayTexture.pack(0.3f, false);
        else return OverlayTexture.NO_OVERLAY;
    }
}
