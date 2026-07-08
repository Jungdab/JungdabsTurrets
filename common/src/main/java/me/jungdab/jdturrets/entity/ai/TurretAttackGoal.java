package me.jungdab.jdturrets.entity.ai;

import me.jungdab.jdturrets.entity.AbstractTurret;
import me.jungdab.jdturrets.init.ModAttributes;
import me.jungdab.jdturrets.init.ModItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class TurretAttackGoal extends Goal {
    protected final AbstractTurret turret;
    private LivingEntity target;

    private int progressTick = 0;
    private boolean isAttack = false;

    public TurretAttackGoal(AbstractTurret turret) {
        this.turret = turret;
    }

    public void tick() {
        LivingEntity target = this.turret.getTarget();

        if(target == null) return;

        if(!isAttack) {
            double distance = this.turret.distanceToSqr(target);
            double attackRange = this.turret.getAttributeValue(ModAttributes.ATTACK_RANGE.get());
            attackRange *= attackRange;
            if(distance <= attackRange) {
                isAttack = true;
                progressTick = 0;
                this.target = target;
            }
        }
        else {
            attackTick();
        }
    }

    private void attackTick() {
        if(this.target == null) return;

        if(progressTick == 0) shootTo();
        if(progressTick > this.turret.getAttributeValue(Attributes.ATTACK_SPEED)*20) isAttack = false;
        progressTick++;
    }

    private void shootTo() {
        if(this.target.isDeadOrDying()) {
            progressTick = (int)((this.turret.getAttributeValue(Attributes.ATTACK_SPEED)*20) / 1.2);
            return;
        }

        if(this.turret.hasChip(ModItems.POTATO_CHIP.get()) > 0) {
            this.turret.shootPotato();
        }

        this.turret.shootTo(this.target);
        this.turret.useAmmo();
    }

    @Override
    public boolean canUse() {
        if(!this.turret.hasAmmo()) return false;

        LivingEntity target = this.turret.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        if(!this.turret.hasAmmo()) return false;

        LivingEntity target = this.turret.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}
