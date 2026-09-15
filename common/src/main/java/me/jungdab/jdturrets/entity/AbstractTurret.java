package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.Constants;
import me.jungdab.jdturrets.client.tracer.TraceRenderManager;
import me.jungdab.jdturrets.entity.ai.TurretAttackGoal;
import me.jungdab.jdturrets.entity.ai.TurretTargetGoal;
import me.jungdab.jdturrets.entity.inventory.TurretContainerMenu;
import me.jungdab.jdturrets.init.ModAttributes;
import me.jungdab.jdturrets.init.ModDamageTypes;
import me.jungdab.jdturrets.init.ModItems;
import me.jungdab.jdturrets.init.ModSounds;
import me.jungdab.jdturrets.util.ModMth;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;


import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class AbstractTurret extends Mob implements GeoEntity, HasCustomInventoryScreen, MenuProvider {

    private static final int INVENTORY_FORMAT_VERSION = 1;

    public static final int TARGET_PLAYER = 0;
    public static final int TARGET_FRIENDLY_MOB = 1;
    public static final int TARGET_HOSTILE_MOB = 2;
    public static final int TARGET_TURRET = 3;

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation PLACE = RawAnimation.begin().thenPlay("place");
    private static final RawAnimation SHOOT = RawAnimation.begin().thenPlay("shoot");
    private static final RawAnimation DESTROY = RawAnimation.begin().thenPlay("destroy");

    private static final ResourceLocation[] SLOT_MODIFIER_IDS = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "turret_upgrade_slot_0"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "turret_upgrade_slot_1"),
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "turret_upgrade_slot_2")
    };

    @Nullable
    private UUID ownerUUID;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> IS_PLACED = SynchedEntityData.defineId(AbstractTurret.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> OWNER_NAME = SynchedEntityData.defineId(AbstractTurret.class, EntityDataSerializers.STRING);

    public byte targetFilter = 0;
    public int targetingMode = 0;

    protected Container inventory;
    protected final ContainerData turretData;

    public AbstractTurret(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);

        this.setTarget(TARGET_PLAYER, true);
        this.setTarget(TARGET_FRIENDLY_MOB, true);
        this.setTarget(TARGET_HOSTILE_MOB, true);

        this.inventory = new SimpleContainer(7);
        this.turretData = new SimpleContainerData(9);
    }

    private void updateUpgrades() {
        AttributeInstance attackDamageAttr = this.getAttribute(Attributes.ATTACK_DAMAGE);
        AttributeInstance attackSpeedAttr = this.getAttribute(Attributes.ATTACK_SPEED);
        AttributeInstance attackRangeAttr = this.getAttribute(ModAttributes.ATTACK_RANGE.get());
        AttributeInstance maxHealthAttr = this.getAttribute(Attributes.MAX_HEALTH);

        for(ResourceLocation id : SLOT_MODIFIER_IDS) {
            if(attackDamageAttr != null) attackDamageAttr.removeModifier(id);
            if(attackSpeedAttr != null) attackSpeedAttr.removeModifier(id);
            if(attackRangeAttr != null) attackRangeAttr.removeModifier(id);
            if(maxHealthAttr != null) maxHealthAttr.removeModifier(id);
        }

        for (int slot = 0; slot <= 2; slot++) {
            ItemStack stack = this.inventory.getItem(slot);
            if(stack.isEmpty()) continue;

            ItemAttributeModifiers attributeModifiers = stack.get(DataComponents.ATTRIBUTE_MODIFIERS);
            if(attributeModifiers == null) continue;

            ResourceLocation currentSlotId = SLOT_MODIFIER_IDS[slot];

            attributeModifiers.modifiers().forEach(entry -> {
                AttributeInstance attribute = this.getAttribute(entry.attribute());
                if(attribute != null) {
                    AttributeModifier original = entry.modifier();
                    attribute.addOrUpdateTransientModifier(new AttributeModifier(currentSlotId, original.amount(), original.operation()));
                }
            });
        }
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.inventory != null) {
            for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
                ItemStack itemstack = this.inventory.getItem(i);
                if (!itemstack.isEmpty() && !EnchantmentHelper.has(itemstack, EnchantmentEffectComponents.PREVENT_EQUIPMENT_DROP)) {
                    this.spawnAtLocation(itemstack);
                }
            }
        }
    }

    protected ItemStack getTurretItem() {return ItemStack.EMPTY;}

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(Items.IRON_INGOT)) {
            float f = this.getHealth();
            this.heal(25.0F);
            if (this.getHealth() != f) {
                float f1 = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, f1);
                itemstack.consume(1, player);
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if(itemstack.is(ModItems.WRENCH.get())) {
            float currentHealth = this.getHealth();
            float maxHealth = this.getMaxHealth();

            if (currentHealth >= maxHealth) {
                this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, 1.0F);
                if(!this.level().isClientSide) {
                    this.discard();
                    this.dropEquipment();
                    this.spawnAtLocation(this.getTurretItem());
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }

        if(!this.level().isClientSide && player instanceof ServerPlayer sp) {
            sp.openMenu(this);
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new TurretAttackGoal(this));
        this.targetSelector.addGoal(0, new TurretTargetGoal(this));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_PLACED, false);
        builder.define(OWNER_NAME, "");
    }

    @Override
    public void tick() {
        super.tick();

        if(this.tickCount == 1 && !this.isPlaced()) this.level().playSound(null, this.blockPosition(), ModSounds.TURRET_PLACE.get(), SoundSource.MASTER, 1, 1);
        if(this.tickCount >= 40 && !this.isPlaced()) this.setPlaced(true);

        LivingEntity target = this.getTarget();
        if(target != null) {
            this.lookAt(target, 180, 180);
        }

        if(!this.level().isClientSide) {
            this.turretData.set(0, (int)(this.getHealth() * 10));
            this.turretData.set(1, (int)(this.getAttributeValue(Attributes.MAX_HEALTH) * 10));
            this.turretData.set(2, (int)(this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 10));
            this.turretData.set(3, (int)(this.getAttributeValue(Attributes.ATTACK_SPEED) * 10));
            this.turretData.set(4, (int)(this.getAttributeValue(ModAttributes.ATTACK_RANGE.get()) * 10));
            this.turretData.set(6, this.targetFilter);
            this.turretData.set(7, this.targetingMode);
            this.turretData.set(8, this.getId());

            updateUpgrades();

            if(this.tickCount % 20 == 0 && this.hasChip(ModItems.SELF_REPAIR_CHIP.get()) > 0) {
                this.setHealth(this.getHealth() + this.hasChip(ModItems.SELF_REPAIR_CHIP.get()));
            }
        }
    }

    public void lookAt(Entity entity, float maxYRotIncrease, float maxXRotIncrease) {
        double d0 = entity.getX() - this.getX();
        double d2 = entity.getZ() - this.getZ();
        double d1 = (entity.getBoundingBox().minY + entity.getBoundingBox().maxY) / (double)2.0F - this.getEyeY();

        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        float f = (float)(Mth.atan2(d2, d0) * (double)180.0F / (double)(float)Math.PI) - 90.0F;
        float f1 = (float)(-(Mth.atan2(d1, d3) * (double)180.0F / (double)(float)Math.PI));
        this.setXRot(this.rotlerp(this.getXRot(), f1, maxXRotIncrease));
        this.setYRot(this.rotlerp(this.getYRot(), f, maxYRotIncrease));
    }

    private float rotlerp(float angle, float targetAngle, float maxIncrease) {
        float f = Mth.wrapDegrees(targetAngle - angle);
        if (f > maxIncrease) {
            f = maxIncrease;
        }
        if (f < -maxIncrease) {
            f = -maxIncrease;
        }
        return angle + f;
    }

    public void shootTo(Entity entity) {
        entity.hurt(ModDamageTypes.turretShoot(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        this.level().playSound(null, this.blockPosition(), ModSounds.TURRET_SHOOT.get(), SoundSource.MASTER, 2, 1);
        this.level().broadcastEntityEvent(this, (byte) 4);
    }

    @Override
    public void handleEntityEvent(byte id) {
        super.handleEntityEvent(id);

        if(id == 4) {
            this.triggerAnim("Turret", "shoot");
        }
    }

    protected void addTrace(Vec3 pos, float height, float distance, long duration) {
        Vec3 pivotPos = this.position().add(0, height, 0);
        Vec3 finalPos = ModMth.getForwardPosition(pivotPos, this.getRotationVector(), -pos.x, pos.y, pos.z);
        TraceRenderManager.addTracer(finalPos, finalPos.add(this.getForward().multiply(distance, distance, distance)), duration);
    }

    public boolean ownedBy(UUID uuid) {
        return uuid.equals(this.ownerUUID);
    }
    public boolean ownedBy(Entity entity) {
        return entity.getUUID().equals(this.ownerUUID);
    }

    public void setOwnerUUID(UUID uuid) {
        this.ownerUUID = uuid;
        updateOwnerName();
    }

    private void updateOwnerName() {
        UUID u = this.getOwnerUUID();
        if(u == null) return;
        MinecraftServer ms = this.getServer();
        if(ms == null) return;
        ServerPlayer p = ms.getPlayerList().getPlayer(u);
        if(p == null) return;

        this.entityData.set(OWNER_NAME, p.getScoreboardName());
    }

    public String getOwnerName() {
        return this.entityData.get(OWNER_NAME);
    }

    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    public boolean isPlaced() {
        return this.entityData.get(IS_PLACED);
    }

    private void setPlaced(boolean b) {
        this.entityData.set(IS_PLACED, b);
    }

    public boolean isTarget(int idx) {
        return (targetFilter & (1 << idx)) != 0;
    }

    public void setTarget(int idx, boolean value) {
        if (value) {
            targetFilter |= (1 << idx);
        } else {
            targetFilter &= ~(1 << idx);
        }
    }

    public void shootPotato() {
    }

    public int hasChip(Item chipItem) {
        int count = 0;

        for(int i = 0; i < 3; i++) {
            ItemStack stack = this.inventory.getItem(i);
            if(stack.is(chipItem)) count++;
        }

        return count;
    }

    public boolean hasAmmo() {
        for(int i = 3; i < 7; i++) {
            ItemStack stack = this.inventory.getItem(i);
            if(stack.is(ModItems.TURRET_AMMO.get())) return true;
        }

        return false;
    }

    public void useAmmo() {
        if(this.hasChip(ModItems.INFINITY_AMMO_CHIP.get()) > 0) return;

        for(int i = 3; i < 7; i++) {
            ItemStack stack = this.inventory.getItem(i);
            if(stack.is(ModItems.TURRET_AMMO.get())) {
                stack.shrink(1);
                return;
            }
        }
    }


    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSounds.TURRET_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.TURRET_DESTROY.get();
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.hasUUID("Owner")) {
            this.ownerUUID = compound.getUUID("Owner");
        }
        this.setPlaced(compound.getBoolean("placed"));
        this.entityData.set(OWNER_NAME, compound.getString("OwnerName"));

        if (compound.contains("TargetFilter")) {
            this.targetFilter = compound.getByte("TargetFilter");
        }
        if (compound.contains("TargetingMode")) {
            this.targetingMode = compound.getInt("TargetingMode");
        }

        ListTag listtag = compound.getList("Items", 10);
        boolean currentInventoryFormat = compound.getInt("InventoryFormat") >= INVENTORY_FORMAT_VERSION;
        for(int i = 0; i < listtag.size(); ++i) {
            CompoundTag compoundtag = listtag.getCompound(i);
            int savedSlot = compoundtag.getByte("Slot") & 255;
            int inventorySlot = currentInventoryFormat ? savedSlot : savedSlot + 1;
            if (inventorySlot < this.inventory.getContainerSize()) {
                this.inventory.setItem(inventorySlot, ItemStack.parse(this.registryAccess(), compoundtag).orElse(ItemStack.EMPTY));
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        this.updateOwnerName();

        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
        compound.putBoolean("placed", this.isPlaced());
        compound.putString("OwnerName", this.entityData.get(OWNER_NAME));
        compound.putByte("TargetFilter", this.targetFilter);
        compound.putInt("TargetingMode", this.targetingMode);
        compound.putInt("InventoryFormat", INVENTORY_FORMAT_VERSION);

        ListTag listtag = new ListTag();
        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte)i);
                listtag.add(itemstack.save(this.registryAccess(), compoundtag));
            }
        }
        compound.put("Items", listtag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(this, "Turret", 0, state -> {
                    if(this.isDeadOrDying()) return state.setAndContinue(DESTROY);
                    if(!this.isPlaced()) return state.setAndContinue(PLACE);
                    return state.setAndContinue(IDLE);
                }).triggerableAnim("shoot", SHOOT)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void openCustomInventoryScreen(Player player) {

    }

    @Override
    public @org.jetbrains.annotations.Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new TurretContainerMenu(i, inventory, this.inventory, this.turretData, 0);
    }
}
