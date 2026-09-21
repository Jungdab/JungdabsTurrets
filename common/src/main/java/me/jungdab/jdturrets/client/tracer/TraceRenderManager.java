package me.jungdab.jdturrets.client.tracer;


import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

public class TraceRenderManager {
    private static final List<ClientTracer> TRACERS = new ArrayList<>();

    // 패킷 핸들러에서 호출할 메서드
    public static void addTracer(Vec3 start, Vec3 end, long duration) {
        TRACERS.add(new ClientTracer(start, end, System.currentTimeMillis(),duration)); // 0.15초 동안 지속
    }

    public static void render(PoseStack poseStack, Camera camera, MultiBufferSource.BufferSource buffers) {
        TRACERS.removeIf(ClientTracer::isExpired);
        if (TRACERS.isEmpty()) return;

        Vec3 cameraPos = camera.getPosition();

        poseStack.pushPose();
        poseStack.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
        Matrix4f matrix = poseStack.last().pose();

        VertexConsumer bufferBuilder = buffers.getBuffer(RenderType.debugQuads());

        for(ClientTracer tracer : TRACERS) {
            Vec3 start = tracer.start();
            Vec3 end = tracer.end();
            float alpha = tracer.getAlpha();


            // -------------------------------------------------------------
            // [핵심] 플레이어 카메라 방향을 반영한 빌보드 오프셋 계산
            // -------------------------------------------------------------
            Vec3 lineDir = end.subtract(start).normalize(); // 레이저 진행 방향
            Vec3 camToLine = start.subtract(cameraPos).normalize(); // 카메라에서 레이저를 바라보는 방향

            // 두 벡터의 외적을 구하면, 레이저 진행 방향과 카메라 시선 모두에 수직인 오프셋 벡터가 나옵니다.
            Vec3 right = lineDir.cross(camToLine).normalize();

            float thickness = 0.01f; // 궤적 두께
            Vec3 offset = right.scale(thickness);

            // 이제 이 p1~p4 사각형은 항상 카메라를 정면으로 바라봅니다.
            Vec3 p1 = start.subtract(offset);
            Vec3 p2 = start.add(offset);
            Vec3 p3 = end.add(offset);
            Vec3 p4 = end.subtract(offset);

            int r = 255, g = 255, b = 30;
            int a = (int)(alpha * 200);

            bufferBuilder.addVertex(matrix, (float)p1.x, (float)p1.y, (float)p1.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p2.x, (float)p2.y, (float)p2.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p3.x, (float)p3.y, (float)p3.z).setColor(r, g, b, a);
            bufferBuilder.addVertex(matrix, (float)p4.x, (float)p4.y, (float)p4.z).setColor(r, g, b, a);

        }

        buffers.endBatch(RenderType.debugQuads());
        poseStack.popPose();
    }

}

