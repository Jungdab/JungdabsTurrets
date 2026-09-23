package me.jungdab.jdturrets.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.jungdab.jdturrets.client.tracer.TraceRenderManager;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "submitEntities", at = @At("TAIL"))
    private void submit(PoseStack poseStack, LevelRenderState levelState, SubmitNodeCollector collector, CallbackInfo ci) {
        TraceRenderManager.submit(poseStack, levelState.cameraRenderState.pos, collector);
    }
}
