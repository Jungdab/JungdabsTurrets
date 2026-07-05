package me.jungdab.jdturrets.client.tracer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class TraceRenderManager {
    private static final List<ClientTracer> TRACERS = new ArrayList<>();

    public static void addTracer(Vec3 start, Vec3 end, long duration) {
        TRACERS.add(new ClientTracer(start, end, System.currentTimeMillis(),duration));
    }

    public static void render(PoseStack poseStack, Camera camera) {
        TRACERS.removeIf(ClientTracer::isExpired);
        if (TRACERS.isEmpty()) return;

        Vec3 cameraPos = camera.getPosition();

        poseStack.pushPose();

        poseStack.mulPose(Axis.XP.rotationDegrees(camera.getXRot()));
        poseStack.mulPose(Axis.YP.rotationDegrees(camera.getYRot() + 180.0F));

        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f matrix = poseStack.last().pose();

        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();

        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        for(ClientTracer tracer : TRACERS) {
            Vec3 start = tracer.start();
            Vec3 end = tracer.end();
            float alpha = tracer.getAlpha();

            Vec3 lineDir = end.subtract(start).normalize();
            Vec3 camToLine = start.subtract(cameraPos).normalize();

            Vec3 right = lineDir.cross(camToLine).normalize();

            float thickness = 0.01f;
            Vec3 offset = right.scale(thickness);

            Vec3 p1 = start.subtract(offset);
            Vec3 p2 = start.add(offset);
            Vec3 p3 = end.add(offset);
            Vec3 p4 = end.subtract(offset);

            int r = 255, g = 255, b = 30;
            int a = (int)(alpha * 200);

            bufferBuilder.vertex(matrix, (float)p1.x, (float)p1.y, (float)p1.z).color(r, g, b, a).endVertex();
            bufferBuilder.vertex(matrix, (float)p2.x, (float)p2.y, (float)p2.z).color(r, g, b, a).endVertex();;
            bufferBuilder.vertex(matrix, (float)p3.x, (float)p3.y, (float)p3.z).color(r, g, b, a).endVertex();;
            bufferBuilder.vertex(matrix, (float)p4.x, (float)p4.y, (float)p4.z).color(r, g, b, a).endVertex();;
        }

        tesselator.end();

        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        poseStack.popPose();
    }

}
