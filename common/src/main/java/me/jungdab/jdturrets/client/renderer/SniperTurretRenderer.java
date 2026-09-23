package me.jungdab.jdturrets.client.renderer;

import me.jungdab.jdturrets.client.model.SniperTurretModel;
import me.jungdab.jdturrets.entity.SniperTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import com.geckolib.renderer.base.GeoRenderState;

public class SniperTurretRenderer<R extends LivingEntityRenderState & GeoRenderState>
        extends AbstractTurretRenderer<SniperTurret, R> {
    public SniperTurretRenderer(EntityRendererProvider.Context context) {
        super(context, new SniperTurretModel());
    }
}