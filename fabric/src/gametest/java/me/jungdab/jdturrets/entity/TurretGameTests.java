package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.init.ModItems;
import net.fabricmc.fabric.api.gametest.v1.GameTest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.UUID;

public class TurretGameTests {
    private static void assertTrue(GameTestHelper helper, boolean condition, String message) {
        helper.assertTrue(condition, net.minecraft.network.chat.Component.literal(message));
    }

    @GameTest
    public void allRecipesLoad(GameTestHelper helper) {
        long count = helper.getLevel().getServer().getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.id().identifier().getNamespace().equals("jdturrets"))
                .count();
        assertTrue(helper, count == 24, "All 24 turret recipes must load; found " + count);
        helper.succeed();
    }

    @GameTest
    public void allTurretsPreserveInventoryAndSettings(GameTestHelper helper) {
        List<EntityType<? extends AbstractTurret>> types = List.of(
                ModEntities.BASIC_TURRET.get(), ModEntities.SNIPER_TURRET.get(), ModEntities.TANK_TURRET.get());
        UUID owner = UUID.fromString("9f5f6836-e19c-412c-9f76-c8453ef28bf2");
        for (var type : types) {
            AbstractTurret original = type.create(helper.getLevel(), EntitySpawnReason.COMMAND);
            assertTrue(helper, original != null, "Turret must be registered and constructible");
            original.setOwnerUUID(owner);
            original.targetFilter = 10;
            original.targetingMode = 2;
            original.inventory.setItem(0, new ItemStack(ModItems.IRON_DAMAGE_CHIP.get()));
            original.inventory.setItem(1, new ItemStack(ModItems.SELF_REPAIR_CHIP.get()));
            original.inventory.setItem(2, new ItemStack(ModItems.IRON_RANGE_CHIP.get()));
            for (int slot = 3; slot < 7; slot++) {
                original.inventory.setItem(slot, new ItemStack(ModItems.TURRET_AMMO.get(), slot + 5));
            }
            var saved = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, helper.getLevel().registryAccess());
            original.saveWithoutId(saved);
            AbstractTurret restored = type.create(helper.getLevel(), EntitySpawnReason.LOAD);
            restored.load(TagValueInput.create(ProblemReporter.DISCARDING, helper.getLevel().registryAccess(), saved.buildResult()));
            assertTrue(helper, owner.equals(restored.getOwnerUUID()), "Owner must survive save/load");
            assertTrue(helper, restored.targetFilter == 10 && restored.targetingMode == 2, "Target settings must survive save/load");
            for (int slot = 0; slot < 7; slot++) {
                assertTrue(helper, ItemStack.matches(original.inventory.getItem(slot), restored.inventory.getItem(slot)),
                        "Inventory slot " + slot + " must survive save/load");
            }
            assertTrue(helper, !restored.removeWhenFarAway(100000), "Turrets must not naturally despawn");
        }
        helper.succeed();
    }

    @GameTest
    public void shotDamagesTargetAndAmmoCanBeConsumed(GameTestHelper helper) {
        var turret = ModEntities.BASIC_TURRET.get().create(helper.getLevel(), EntitySpawnReason.COMMAND);
        var target = EntityTypes.COW.create(helper.getLevel(), EntitySpawnReason.COMMAND);
        assertTrue(helper, turret != null && target != null, "Entities must be constructible");
        float health = target.getHealth();
        turret.shootTo(target);
        assertTrue(helper, target.getHealth() < health, "Turret shot must damage target on the server");
        turret.inventory.setItem(3, new ItemStack(ModItems.TURRET_AMMO.get(), 2));
        turret.useAmmo();
        assertTrue(helper, turret.inventory.getItem(3).getCount() == 1, "A shot must consume one round");
        turret.inventory.setItem(0, new ItemStack(ModItems.INFINITY_AMMO_CHIP.get()));
        turret.useAmmo();
        assertTrue(helper, turret.inventory.getItem(3).getCount() == 1, "Infinity chip must preserve ammo");
        helper.succeed();
    }

    @GameTest
    public void upgradesApplyWithoutStackingEveryTick(GameTestHelper helper) {
        var turret = ModEntities.BASIC_TURRET.get().create(helper.getLevel(), EntitySpawnReason.COMMAND);
        double damage = turret.getAttributeValue(Attributes.ATTACK_DAMAGE);
        turret.inventory.setItem(0, new ItemStack(ModItems.IRON_DAMAGE_CHIP.get()));
        turret.setNoAi(true);
        turret.tick();
        double upgraded = turret.getAttributeValue(Attributes.ATTACK_DAMAGE);
        assertTrue(helper, upgraded > damage, "Damage chip must improve attack damage");
        turret.tick();
        assertTrue(helper, turret.getAttributeValue(Attributes.ATTACK_DAMAGE) == upgraded, "Upgrade must not accumulate each tick");
        turret.inventory.setItem(0, ItemStack.EMPTY);
        turret.tick();
        assertTrue(helper, turret.getAttributeValue(Attributes.ATTACK_DAMAGE) == damage, "Removing chip must remove upgrade");
        helper.succeed();
    }
}
