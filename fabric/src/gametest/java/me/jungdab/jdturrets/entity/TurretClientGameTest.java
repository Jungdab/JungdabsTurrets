package me.jungdab.jdturrets.entity;

import me.jungdab.jdturrets.client.screen.TurretContainerScreen;
import me.jungdab.jdturrets.client.tracer.TraceRenderManager;
import me.jungdab.jdturrets.init.ModItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import com.geckolib.cache.GeckoLibResources;

import java.util.List;

/** Exercises the client resource loading and rendering paths that server tests cannot cover. */
public class TurretClientGameTest implements FabricClientGameTest {
    @Override
    public void runTest(ClientGameTestContext context) {
        context.runOnClient(client -> {
            for (String name : List.of("basic_turret", "sniper_turret", "tank_turret")) {
                var id = Identifier.fromNamespaceAndPath("jdturrets", "entity/" + name);
                if (!GeckoLibResources.getBakedModels().cache().containsKey(id)) {
                    throw new AssertionError("Missing baked turret model: " + id);
                }
                if (!GeckoLibResources.getBakedAnimations().cache().containsKey(id)) {
                    throw new AssertionError("Missing baked turret animations: " + id);
                }
            }
        });
        try (var world = context.worldBuilder().create()) {
            world.getServer().runCommand("gamemode creative @p");
            world.getServer().runCommand("tp @p 0 -60 0 0 10");
            world.getClientLevel().waitForChunksRender();
            world.getServer().runOnServer(server -> {
                var player = server.getPlayerList().getPlayers().getFirst();
                var items = List.of(ModItems.BASIC_TURRET_ITEM.get(), ModItems.SNIPER_TURRET_ITEM.get(), ModItems.TANK_TURRET_ITEM.get());
                for (int index = 0; index < items.size(); index++) {
                    int x = (index - 1) * 3;
                    var stack = new ItemStack(items.get(index));
                    player.setItemInHand(InteractionHand.MAIN_HAND, stack);
                    var hit = new BlockHitResult(new Vec3(x + 0.5, -60, 5.5), Direction.UP, new BlockPos(x, -61, 5), false);
                    items.get(index).useOn(new UseOnContext(player, InteractionHand.MAIN_HAND, hit));
                    if (!stack.isEmpty()) throw new AssertionError("Turret placement did not consume item");
                }
            });
            context.waitTicks(60);
            context.runOnClient(client -> {
                long count = java.util.stream.StreamSupport.stream(client.level.entitiesForRendering().spliterator(), false)
                        .filter(AbstractTurret.class::isInstance).count();
                if (count != 3) throw new AssertionError("Expected all three placed turrets on client, found " + count);
                TraceRenderManager.addTracer(new Vec3(-2.5, -59, 5.5), new Vec3(2.5, -59, 5.5), 10000);
            });
            context.waitTicks(2);
            context.takeScreenshot("three-turrets-and-trace");
            for (int index = 0; index < 6; index++) {
                int turretIndex = index % 3;
                world.getServer().runOnServer(server -> {
                    var player = server.getPlayerList().getPlayers().getFirst();
                    var turrets = server.overworld().getEntitiesOfClass(AbstractTurret.class, player.getBoundingBox().inflate(12));
                    if (turrets.size() != 3) throw new AssertionError("Expected three server turrets");
                    player.openMenu(turrets.get(turretIndex));
                });
                context.waitForScreen(TurretContainerScreen.class);
                context.waitTicks(10);
                double[] animationTimes = new double[2];
                context.runOnClient(client -> animationTimes[0] = previewAnimationTime(client));
                if (animationTimes[0] <= 0 || animationTimes[0] >= 1) throw new AssertionError("Preview did not start animating: " + animationTimes[0]);
                context.takeScreenshot("turret-menu-" + index + "-early");
                context.waitTicks(15);
                context.runOnClient(client -> animationTimes[1] = previewAnimationTime(client));
                if (animationTimes[1] <= animationTimes[0] + 0.25) throw new AssertionError("Preview animation stopped advancing");
                context.takeScreenshot("turret-menu-" + index + "-middle");
                context.waitTicks(40);
                context.runOnClient(client -> {
                    if (previewAnimationTime(client) < 1.45) throw new AssertionError("Preview did not finish unfolding");
                });
                context.takeScreenshot("turret-menu-" + index);
                context.runOnClient(client -> client.player.closeContainer());
                context.waitTick();
            }
        }
    }

    private static double previewAnimationTime(net.minecraft.client.Minecraft client) {
        try {
            var field = TurretContainerScreen.class.getDeclaredField("DUMMY_TURRETS");
            field.setAccessible(true);
            var previews = (java.util.Map<?, ?>) field.get(null);
            var screen = (TurretContainerScreen) client.screen;
            var preview = (AbstractTurret) previews.get(screen.getMenu().getTurretType());
            if (preview == null) throw new AssertionError("Missing preview entity");
            var controller = preview.getAnimatableInstanceCache().getManagerForId(preview.getId()).getAnimationControllers().get("Turret");
            double time = controller.getCurrentAnimationTime();
            System.out.println("PREVIEW ticks=" + preview.tickCount + " animationTime=" + time);
            return time;
        } catch (ReflectiveOperationException exception) {
            throw new AssertionError("Cannot inspect preview", exception);
        }
    }
}
