package me.jungdab.jdturrets.entity.ai;

import me.jungdab.jdturrets.entity.AbstractTurret;
import me.jungdab.jdturrets.init.ModAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class TurretTargetGoal extends Goal {
    private final AbstractTurret turret;

    public TurretTargetGoal(AbstractTurret turret) {
        this.turret = turret;
    }

    @Override
    public boolean canUse() {
        if(!this.turret.isPlaced()) return false;
        if(!this.turret.hasAmmo()) return false;

        LivingEntity target = findTarget();
        this.turret.setTarget(target);
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if(!this.turret.hasAmmo()) return false;
        LivingEntity target = this.turret.getTarget();

        LivingEntity bestTarget = findTarget();
        if (bestTarget != target) {
            this.turret.setTarget(bestTarget);
        }
        return true;
    }

    private LivingEntity findTarget() {
        LivingEntity closetTurret = null;
        double closestDist = Double.MAX_VALUE;

        double range = this.turret.getAttributeValue(ModAttributes.ATTACK_RANGE.get());

        for(LivingEntity entity : this.turret.level().getEntitiesOfClass(LivingEntity.class, this.turret.getBoundingBox().inflate(range, range, range))) {
            if(!entity.isAlive() || entity.equals(this.turret)) continue;

            if(!this.turret.getSensing().hasLineOfSight(entity)) continue;

            if(entity instanceof Player player) {
                if(player.isCreative() || player.isSpectator() || this.turret.ownedBy(entity)) continue;
                if(!this.turret.isTarget(AbstractTurret.TARGET_PLAYER)) continue;
            }

            UUID uuid = this.turret.getOwnerUUID();

            if(entity instanceof AbstractTurret otherTurret) {
                if(!this.turret.isTarget(AbstractTurret.TARGET_TURRET)) continue;
                if(uuid != null && otherTurret.ownedBy(uuid)) continue;
            }

            if(!this.turret.isTarget(AbstractTurret.TARGET_FRIENDLY_MOB) && (entity instanceof Animal || entity instanceof AbstractVillager || entity instanceof AbstractGolem)) continue;
            if(!this.turret.isTarget(AbstractTurret.TARGET_HOSTILE_MOB) && (entity instanceof Monster)) continue;

            double dist = this.turret.distanceToSqr(entity);
            if (dist < closestDist) {
                closestDist = dist;
                closetTurret = entity;
            }
        }
        return closetTurret;
    }
}
