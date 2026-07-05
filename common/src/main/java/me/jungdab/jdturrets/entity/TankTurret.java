package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.init.ModAttributes;
import me.jungdab.jdturrets.init.ModDamageTypes;
import me.jungdab.jdturrets.init.ModItems;
import me.jungdab.jdturrets.util.ModMth;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TankTurret extends AbstractTurret {
    public TankTurret(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttribute() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0)
                .add(Attributes.MOVEMENT_SPEED, 0)
                .add(ModAttributes.ATTACK_RANGE.get().value(), 8)
                .add(Attributes.ATTACK_SPEED, 2)
                .add(Attributes.ATTACK_DAMAGE, 10);
    }

    @Override
    public void shootTo(Entity entity) {
        if (!(this.level() instanceof ServerLevel sl)) return;

        this.level().broadcastEntityEvent(this, (byte) 4);

        Vec3 pos = entity.position();

        Packet<?> particlePacket = new ClientboundLevelParticlesPacket(ParticleTypes.EXPLOSION, true, pos.x, pos.y, pos.z, 0, 0, 0, 0, 0);
        Packet<?> soundPacket = new ClientboundSoundPacket(Holder.direct(SoundEvents.GENERIC_EXPLODE), SoundSource.PLAYERS, pos.x, pos.y, pos.z, 3, 1, 0);

        for (ServerPlayer player : sl.players()) {
            player.connection.send(particlePacket);
            player.connection.send(soundPacket);
        }

        AABB area = new AABB(
                pos.x - 2, pos.y - 2, pos.z - 2,
                pos.x + 2, pos.y + 2, pos.z + 2
        );

        DamageSource source = ModDamageTypes.turretShoot(this);

        for (LivingEntity target : sl.getEntitiesOfClass(LivingEntity.class, area)) {
            if(target.equals(this)) continue;
            target.hurt(source, (float) this.getAttributeValue(ModAttributes.ATTACK_RANGE.get()));
        }
    }

    @Override
    public void shootPotato() {
        Vec3 pivotPos = this.position().add(0, 0.8125, 0);
        Vec3 finalPos = ModMth.getForwardPosition(pivotPos, this.getRotationVector(), 0, 0, 0.5625);
        Vec3 vel = this.getForward().normalize().multiply(2,2,2);

        ItemEntity potato0 = new ItemEntity(this.level(), finalPos.x, finalPos.y, finalPos.z, Items.POTATO.getDefaultInstance(), vel.x, vel.y, vel.z);
        this.level().addFreshEntity(potato0);
    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new TurretContainerMenu(i, inventory, this.inventory, this.turretData, 3);
    }

    protected ItemStack getTurretItem() {return ModItems.TANK_TURRET_ITEM.get().getDefaultInstance();}
}
