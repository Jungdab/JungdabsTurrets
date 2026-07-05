package me.jungdab.jdturrets.client.model;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AbstractTurretModel<T extends AbstractTurret> extends DefaultedEntityGeoModel<T> {
    public AbstractTurretModel(String name) {
        super(new ResourceLocation(Constants.MOD_ID, name));
    }

    @Override
    public void setCustomAnimations(T turret, long instanceId, AnimationState<T> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        float partialTick = animationState.getPartialTick();

        float xRot = turret.getViewXRot(partialTick);
        float yRot = partialTick == 1.0F ? turret.getYRot() : Mth.lerp(partialTick, turret.yRotO, turret.getYRot());

        head.setRotX(-xRot * Mth.DEG_TO_RAD);
        head.setRotY(-yRot * Mth.DEG_TO_RAD + Mth.PI);
    }
}
