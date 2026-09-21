package me.jungdab.jdturrets.client.model;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AbstractTurretModel<T extends AbstractTurret> extends DefaultedEntityGeoModel<T> {
    public AbstractTurretModel(String name) {
        super(Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
