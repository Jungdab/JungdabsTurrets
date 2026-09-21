package me.jungdab.jdturrets.client.model;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animatable.processing.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AbstractTurretModel<T extends AbstractTurret> extends DefaultedEntityGeoModel<T> {
    public AbstractTurretModel(String name) {
        super(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    @Override
    public void setCustomAnimations(AnimationState<T> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");
        if (head == null) return;
        float xRot = animationState.getData(DataTickets.ENTITY_PITCH);
        float yRot = animationState.getData(DataTickets.ENTITY_YAW);

        head.setRotX(-xRot * Mth.DEG_TO_RAD);
        head.setRotY(-yRot * Mth.DEG_TO_RAD + Mth.PI);
    }
}
