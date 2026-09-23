package me.jungdab.jdturrets.client.renderer;

import me.jungdab.jdturrets.client.model.TankTurretModel;
import me.jungdab.jdturrets.entity.TankTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import com.geckolib.renderer.base.GeoRenderState;

public class TankTurretRenderer<R extends LivingEntityRenderState & GeoRenderState>
        extends AbstractTurretRenderer<TankTurret, R> {
    public TankTurretRenderer(EntityRendererProvider.Context context) {
        super(context, new TankTurretModel());
    }
}