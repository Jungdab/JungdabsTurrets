package me.jungdab.jdturrets.clienttest;

import me.jungdab.jdturrets.entity.AbstractTurret;
import net.minecraft.world.Container;

import me.jungdab.jdturrets.client.screen.TurretContainerScreen;
import me.jungdab.jdturrets.client.tracer.TraceRenderManager;
import me.jungdab.jdturrets.init.ModEntities;
import me.jungdab.jdturrets.init.ModItems;
import me.jungdab.jdturrets.network.C2STurretConfigPacket;
import me.jungdab.jdturrets.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.util.ProblemReporter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/** Development-only integrated client/server smoke test, shared by both loaders. */
public final class ClientSmokeTest {
    private static int phase, ticks, resumeAt, menuIndex;
    private static long started;
    private static long startedEpoch;
    private static double earlyPreviewTime;
    private static CompletableFuture<Void> serverTask;
    private static CompletableFuture<Void> screenshotTask;
    private static final List<Integer> turretIds = new ArrayList<>();

    public static void tick(Minecraft client) {
        if (!Boolean.getBoolean("jdturrets.clientSmokeTest") || phase == 99) return;
        if (started == 0) {
            started = System.nanoTime();
            startedEpoch = System.currentTimeMillis();
        }
        check(System.nanoTime() - started < 300_000_000_000L, "Client smoke test timed out at phase " + phase);
        if (++ticks < resumeAt || client.gui.overlay() != null) return;
        if (screenshotTask != null) {
            if (!screenshotTask.isDone()) return;
            screenshotTask.join();
            screenshotTask = null;
        }
        if (serverTask != null) {
            if (!serverTask.isDone()) return;
            serverTask.join();
            serverTask = null;
        }
        switch (phase) {
            case 0 -> {
                for (String name : List.of("basic_turret", "sniper_turret", "tank_turret")) {
                    var id = net.minecraft.resources.Identifier.fromNamespaceAndPath("jdturrets", "entity/" + name);
                    for (String animation : List.of("place", "idle", "shoot", "destroy")) {
                        check(com.geckolib.cache.GeckoLibResources.getBakedAnimations().getAnimation(id, new net.minecraft.resources.Identifier[0], animation) != null,
                                "Missing animation " + animation + " for " + id);
                    }
                }
                phase = 1;
                client.options.pauseOnLostFocus = false;
                client.options.renderDistance().set(4);
                var previousScreen = client.gui.screen();
                CreateWorldScreen.openFresh(client, () -> client.gui.setScreen(previousScreen));
                check(client.gui.screen() instanceof CreateWorldScreen, "World creation screen must open");
                var screen = (CreateWorldScreen) client.gui.screen();
                var settings = screen.getUiState();
                var flat = settings.getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).getOrThrow(WorldPresets.FLAT);
                settings.setWorldType(new WorldCreationUiState.WorldTypeEntry(flat));
                settings.setName("Turret smoke " + System.currentTimeMillis());
                settings.setSeed("1");
                settings.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
                settings.setAllowCommands(true);
                settings.setGenerateStructures(false);
                settings.getGameRules().set(GameRules.SPAWN_MOBS, false, null);
                var create = screen.children().stream().filter(Button.class::isInstance).map(Button.class::cast)
                        .filter(button -> button.getMessage().getString().equals(Component.translatable("selectWorld.create").getString()))
                        .findFirst().orElseThrow();
                create.onPress(new net.minecraft.client.input.MouseButtonInfo(0, 0));
            }
            case 1 -> {
                if (client.level == null || client.player == null || client.gui.screen() != null) return;
                serverTask = client.getSingleplayerServer().submit(() -> placeTurrets(client.getSingleplayerServer()));
                advance(2, 80);
            }
            case 2 -> {
                long count = java.util.stream.StreamSupport.stream(client.level.entitiesForRendering().spliterator(), false)
                        .filter(AbstractTurret.class::isInstance).count();
                check(count == 3, "All three placed turrets must reach the client");
                TraceRenderManager.addTracer(new Vec3(-2.5, -59, 5.5), new Vec3(2.5, -59, 5.5), 10000);
                Services.NETWORK.sendToServer(new C2STurretConfigPacket(turretIds.getFirst(), C2STurretConfigPacket.TARGET_FILTER, 10));
                Services.NETWORK.sendToServer(new C2STurretConfigPacket(turretIds.getFirst(), C2STurretConfigPacket.TARGETING_MODE, 2));
                advance(3, 20);
            }
            case 3 -> {
                screenshot(client, "world");
                serverTask = client.getSingleplayerServer().submit(() -> verifyGameplay(client.getSingleplayerServer()));
                advance(4, 5);
            }
            case 4 -> {
                serverTask = client.getSingleplayerServer().submit(() -> {
                    var server = client.getSingleplayerServer();
                    var turret = (AbstractTurret) server.overworld().getEntity(turretIds.get(menuIndex % 3));
                    server.getPlayerList().getPlayers().getFirst().openMenu(turret);
                });
                advance(8, 10);
            }
            case 8 -> {
                if (!(client.gui.screen() instanceof TurretContainerScreen)) return;
                earlyPreviewTime = previewAnimationTime(client);
                check(earlyPreviewTime > 0 && earlyPreviewTime < 1, "Preview must start animating, including when reopened");
                screenshot(client, "menu-" + menuIndex + "-early");
                advance(9, 15);
            }
            case 9 -> {
                check(previewAnimationTime(client) > earlyPreviewTime + 0.25, "Preview animation must advance between rendered frames");
                screenshot(client, "menu-" + menuIndex + "-middle");
                advance(5, 40);
            }
            case 5 -> {
                check(client.gui.screen() instanceof TurretContainerScreen, "Turret configuration screen must open");
                check(client.player.containerMenu.getSlot(0).getItem().is(ModItems.IRON_DAMAGE_CHIP.get()), "Client menu must receive the installed chip");
                check(client.player.containerMenu.getSlot(3).getItem().is(ModItems.TURRET_AMMO.get()), "Client menu must receive turret ammunition");
                check(previewAnimationTime(client) >= 1.45, "Preview must finish unfolding");
                screenshot(client, "menu-" + menuIndex);
                advance(7, 1);
            }
            case 7 -> {
                client.player.closeContainer();
                menuIndex++;
                advance(menuIndex < 6 ? 4 : 6, 20);
            }
            case 6 -> {
                try {
                    for (String name : List.of("world", "menu-0", "menu-1", "menu-2", "menu-3", "menu-4", "menu-5")) {
                        var path = client.gameDirectory.toPath().resolve("screenshots/smoke-" + name + ".png");
                        check(Files.isRegularFile(path) && Files.getLastModifiedTime(path).toMillis() >= startedEpoch, "Fresh screenshot missing: " + name);
                    }
                    Files.writeString(client.gameDirectory.toPath().resolve("smoke-result.txt"),
                            "PASS: three turret placements, client rendering, trace, all configuration screens, advancing preview animations on first open and reopen, C2S settings packets, recipes, damage, ammunition and NBT save/load\n");
                } catch (java.io.IOException exception) {
                    throw new AssertionError("Could not record smoke test results", exception);
                }
                phase = 99;
                client.stop();
            }
            default -> throw new AssertionError("Unknown smoke phase " + phase);
        }
    }

    private static void placeTurrets(MinecraftServer server) {
        var player = server.getPlayerList().getPlayers().getFirst();
        server.getCommands().performPrefixedCommand(server.createCommandSourceStack(), "tp @p 0 -60 0 0 10");
        var items = List.of(ModItems.BASIC_TURRET_ITEM.get(), ModItems.SNIPER_TURRET_ITEM.get(), ModItems.TANK_TURRET_ITEM.get());
        for (int index = 0; index < items.size(); index++) {
            int x = (index - 1) * 3;
            var stack = new ItemStack(items.get(index));
            player.setItemInHand(InteractionHand.MAIN_HAND, stack);
            var hit = new BlockHitResult(new Vec3(x + 0.5, -60, 5.5), Direction.UP, new BlockPos(x, -61, 5), false);
            items.get(index).useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
            check(stack.isEmpty(), "Placement must consume turret item");
        }
        var turrets = server.overworld().getEntitiesOfClass(AbstractTurret.class, player.getBoundingBox().inflate(12));
        check(turrets.size() == 3, "Expected three server turrets");
        for (var turret : turrets) turretIds.add(turret.getId());
    }

    private static void verifyGameplay(MinecraftServer server) {
        var turret = (AbstractTurret) server.overworld().getEntity(turretIds.getFirst());
        check(turret.targetFilter == 10 && turret.targetingMode == 2, "Client settings packets must update the server");
        check(server.getRecipeManager().getRecipes().stream().filter(recipe -> recipe.id().identifier().getNamespace().equals("jdturrets")).count() == 24,
                "All 24 mod recipes must load");
        var shotTurret = ModEntities.BASIC_TURRET.get().create(server.overworld(), EntitySpawnReason.COMMAND);
        var cow = EntityTypes.COW.create(server.overworld(), EntitySpawnReason.COMMAND);
        float health = cow.getHealth();
        shotTurret.shootTo(cow);
        check(cow.getHealth() < health, "Turret shot must damage target");
        inventory(shotTurret, server).setItem(3, new ItemStack(ModItems.TURRET_AMMO.get(), 2));
        shotTurret.useAmmo();
        check(inventory(shotTurret, server).getItem(3).getCount() == 1, "Shot must consume ammo");
        for (int id : turretIds) {
            var original = (AbstractTurret) server.overworld().getEntity(id);
            inventory(original, server).setItem(0, new ItemStack(ModItems.IRON_DAMAGE_CHIP.get()));
            for (int slot = 3; slot < 7; slot++) inventory(original, server).setItem(slot, new ItemStack(ModItems.TURRET_AMMO.get(), slot + 5));
            var saved = TagValueOutput.createWithContext(ProblemReporter.DISCARDING, server.registryAccess());
            original.saveWithoutId(saved);
            var restored = (AbstractTurret) original.getType().create(server.overworld(), EntitySpawnReason.LOAD);
            restored.load(TagValueInput.create(ProblemReporter.DISCARDING, server.registryAccess(), saved.buildResult()));
            check(original.getOwnerUUID().equals(restored.getOwnerUUID()), "Owner must survive save/load");
            check(original.targetFilter == restored.targetFilter && original.targetingMode == restored.targetingMode, "Settings must survive save/load");
            for (int slot = 0; slot < 7; slot++) check(ItemStack.matches(inventory(original, server).getItem(slot), inventory(restored, server).getItem(slot)), "Inventory must survive save/load");
        }
    }

    private static void screenshot(Minecraft client, String name) {
        screenshotTask = new CompletableFuture<>();
        var completed = screenshotTask;
        Screenshot.grab(client.gameDirectory, "smoke-" + name + ".png", client.gameRenderer.mainRenderTarget(), 1, message -> completed.complete(null));
    }

    private static double previewAnimationTime(Minecraft client) {
        try {
            var field = TurretContainerScreen.class.getDeclaredField("DUMMY_TURRETS");
            field.setAccessible(true);
            var previews = (java.util.Map<?, ?>) field.get(null);
            var screen = (TurretContainerScreen) client.gui.screen();
            var preview = (AbstractTurret) previews.get(screen.getMenu().getTurretType());
            check(preview != null, "Preview entity must exist");
            var controller = preview.getAnimatableInstanceCache().getManagerForId(preview.getId()).getAnimationControllers().get("Turret");
            double time = controller.getCurrentAnimationTime();
            System.out.println("PREVIEW " + menuIndex + " ticks=" + preview.tickCount + " animationTime=" + time);
            return time;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Cannot inspect preview", exception);
        }
    }

    private static Container inventory(AbstractTurret turret, MinecraftServer server) {
        var player = server.getPlayerList().getPlayers().getFirst();
        return turret.createMenu(0, player.getInventory(), player).getSlot(0).container;
    }

    private static void advance(int next, int delay) { phase = next; resumeAt = ticks + delay; }
    private static void check(boolean condition, String message) { if (!condition) throw new AssertionError(message); }
}
