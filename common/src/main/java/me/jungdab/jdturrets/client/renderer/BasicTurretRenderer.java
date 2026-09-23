package me.jungdab.jdturrets.client.renderer;

import me.jungdab.jdturrets.client.model.BasicTurretModel;
import me.jungdab.jdturrets.entity.BasicTurret;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import com.geckolib.renderer.base.GeoRenderState;

public class BasicTurretRenderer<R extends LivingEntityRenderState & GeoRenderState>
        extends AbstractTurretRenderer<BasicTurret, R> {
    public BasicTurretRenderer(EntityRendererProvider.Context context) {
        super(context, new BasicTurretModel());
    }
}