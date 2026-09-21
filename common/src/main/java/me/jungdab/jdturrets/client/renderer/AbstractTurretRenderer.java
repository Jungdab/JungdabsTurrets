package me.jungdab.jdturrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class AbstractTurretRenderer<T extends AbstractTurret, R extends LivingEntityRenderState & GeoRenderState>
        extends GeoEntityRenderer<T, R> {
    protected AbstractTurretRenderer(EntityRendererProvider.Context context, GeoModel<T> model) {
        super(context, model);
    }

    @Override
    protected void applyRotations(R state, PoseStack poseStack, float nativeScale, CameraRenderState cameraState) {
        // The head bone rotates; the stationary turret base must not rotate with a living entity's body.
    }

    @Override
    public void addRenderData(T turret, Void relatedObject, R state, float partialTick) {
        state.addGeckolibData(DataTickets.ENTITY_PITCH, turret.getViewXRot(partialTick));
        state.addGeckolibData(DataTickets.ENTITY_YAW, Mth.lerp(partialTick, turret.yRotO, turret.getYRot()));
    }

    @Override
    public int getPackedOverlay(T turret, Void relatedObject, float u, float partialTick) {
        return turret.hurtTime > 0 || turret.deathTime > 0
                ? OverlayTexture.pack(0.3f, false) : OverlayTexture.NO_OVERLAY;
    }
}
