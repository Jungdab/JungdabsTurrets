package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.init.ModAttributes;
import me.jungdab.jdturrets.init.ModItems;
import me.jungdab.jdturrets.util.ModMth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SniperTurret extends AbstractTurret{
    public SniperTurret(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(ModAttributes.ATTACK_RANGE.get().value(), 30)
                .add(Attributes.ATTACK_SPEED, 3)
                .add(Attributes.ATTACK_DAMAGE, 20);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);

        if(id == 4) {
            this.addTrace(new Vec3(0, 0.25, 1.75), 0.625f, (float) this.getAttributeValue(ModAttributes.ATTACK_RANGE.get()), 100);
        }
    }

    @Override
    public void shootPotato() {
        Vec3 pivotPos = this.position().add(0, 0.625, 0);
        Vec3 finalPos = ModMth.getForwardPosition(pivotPos, this.getRotationVector(), 0, 0.25, 1.75);
        Vec3 vel = this.getForward().normalize().multiply(2,2,2);

        ItemEntity potato0 = new ItemEntity(this.level(), finalPos.x, finalPos.y, finalPos.z, Items.POTATO.getDefaultInstance(), vel.x, vel.y, vel.z);
        this.level().addFreshEntity(potato0);
    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new TurretContainerMenu(i, inventory, this.inventory, this.turretData, 2);
    }

    protected ItemStack getTurretItem() {return ModItems.SNIPER_TURRET_ITEM.get().getDefaultInstance();}
}
