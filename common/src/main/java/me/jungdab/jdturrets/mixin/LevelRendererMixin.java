package me.jungdab.jdturrets.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jungdab.jdturrets.client.tracer.TraceRenderManager;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import java.util.List;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderEntities", at = @At("TAIL"))
    private void render(PoseStack poseStack, MultiBufferSource.BufferSource buffers, Camera camera, DeltaTracker deltaTracker, List<Entity> entities, CallbackInfo ci) {
        TraceRenderManager.render(poseStack, camera, buffers);
    }
}

