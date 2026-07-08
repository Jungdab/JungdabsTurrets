package me.jungdab.jdturrets.item;

import me.jungdab.jdturrets.entity.AbstractTurret;
import me.jungdab.jdturrets.entity.BasicTurret;
import me.jungdab.jdturrets.entity.SniperTurret;
import me.jungdab.jdturrets.entity.TankTurret;
import me.jungdab.jdturrets.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class TurretItem<T extends AbstractTurret> extends Item {
    private final Supplier<EntityType<T>> turret;

    public TurretItem(Properties properties, Supplier<EntityType<T>> turret) {
        super(properties);

        this.turret = turret;
    }

    @Override
    @NotNull
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();

        if(level.getBlockState(ctx.getClickedPos()).canBeReplaced() ||  !level.getBlockState(ctx.getClickedPos().above()).isAir()) return InteractionResult.PASS;

        Vec3 spawnPos = Vec3.atBottomCenterOf(ctx.getClickedPos().above());
        Player player = ctx.getPlayer();

        if(player == null) return InteractionResult.PASS;

        if(level instanceof ServerLevel sl) {
            if(this.turret.get() == ModEntities.BASIC_TURRET.get()) {
                BasicTurret t = new BasicTurret(ModEntities.BASIC_TURRET.get(), sl);
                t.setYRot(player.getYRot()+180f);
                t.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                t.setOwnerUUID(player.getUUID());
                sl.addFreshEntity(t);
            } else if (this.turret.get() == ModEntities.SNIPER_TURRET.get()) {
                SniperTurret t = new SniperTurret(ModEntities.SNIPER_TURRET.get(), sl);
                t.setYRot(player.getYRot()+180f);
                t.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                t.setOwnerUUID(player.getUUID());
                sl.addFreshEntity(t);
            } else  if (this.turret.get() == ModEntities.TANK_TURRET.get()) {
                TankTurret t = new TankTurret(ModEntities.TANK_TURRET.get(), sl);
                t.setYRot(player.getYRot()+180f);
                t.setPos(spawnPos.x, spawnPos.y, spawnPos.z);
                t.setOwnerUUID(player.getUUID());
                sl.addFreshEntity(t);
            }
        }

        ctx.getItemInHand().shrink(1);
        return InteractionResult.SUCCESS;
    }
}
